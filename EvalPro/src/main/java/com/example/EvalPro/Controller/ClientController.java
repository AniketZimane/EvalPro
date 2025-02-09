package com.example.EvalPro.Controller;

import com.example.EvalPro.Dao.*;
import com.example.EvalPro.Entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sound.midi.Soundbank;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class ClientController {

    @Autowired
    SemesterRepo semesterRepo;
    @Autowired
    ResultRepo resultRepo;
    @Autowired
    PatternRepo patternRepo;
    @Autowired
    StreamRepo streamRepo;
    @Autowired
    RevaluationRequestRepo revaluationRequestRepo;
    @GetMapping("/")
    public String getHomePage() {
        return "Chatbot";
    }

    @GetMapping("/revolution/{studentId}")
    public String getRevaluationPage(@PathVariable("studentId") String studentId, Model model) {
        List<Result> resultList = resultRepo.findByStudentId(studentId);
        String jsonString = "";
        Long semId = null;
        Long streamId = null;

        // Extracting Semester & Stream
        for (Result data : resultList) {
            semId = Long.parseLong(data.getSem());
            streamId = Long.parseLong(data.getStream());
            jsonString = data.getSubjectMarksJson(); // Get JSON string from Result object
        }

        // Handling Null values
        if (semId == null || streamId == null || jsonString.isEmpty()) {
            model.addAttribute("error", "Invalid student data");
            return "errorPage"; // Redirect to error page if data is missing
        }

        Semester semester = semesterRepo.getReferenceById(semId);
        Stream stream = streamRepo.getReferenceById(streamId);

        // Parsing the JSON string into a Map safely
        Map<String, Double> scores = new HashMap<>();
        try {
            scores = Arrays.stream(jsonString.replaceAll("[{}]", "").split(", "))
                    .map(entry -> entry.split("="))
                    .filter(entry -> entry.length == 2) // Ensure each entry has both key & value
                    .collect(Collectors.toMap(entry -> entry[0].trim(), entry -> Double.parseDouble(entry[1].trim())));
        } catch (Exception e) {
            System.out.println("Inside error");
            e.printStackTrace();
            model.addAttribute("error", "Failed to parse subject marks");
            return "errorPage";
        }

        // Adding attributes to the model
        model.addAttribute("scores", scores);
        model.addAttribute("seatNumber", studentId);
        model.addAttribute("sem", semester.getName());
        model.addAttribute("course", stream.getName());

        return "RevaluationApplication";
    }



    @GetMapping("/ResultDisplay/")
    public String getResultPage()
    {
        return "ResultDisplay";
    }

    @PostMapping("/get/student/result/")
    public String getStudentDetails(
            @RequestParam("seatNumber") String seatNumber,
            @RequestParam("motherName") String motherName,RedirectAttributes redirectAttributes,
            Model model) {


        Date currentDate=new Date();
        // Simulated database check (Replace with actual DB logic)

        List<Result> resultList=resultRepo.findByStudentIdAndMotherName(seatNumber,motherName);
        System.out.println(resultList);
        String jsonString ="";
        String sem="";
        Long patternId=null;
        Long streamId=null;

        for (Result data:resultList) {
            jsonString=data.getSubjectMarksJson();
            sem=data.getSem();
            patternId=Long.parseLong(data.getPattern());
            streamId=Long.parseLong(data.getStream());

        }
        if (resultList.size()>0 || resultList !=null) {
            try {
                Map<String, Double> scores = Arrays.stream(jsonString.replaceAll("[{}]", "").split(", "))
                        .map(entry -> entry.split("="))
                        .collect(Collectors.toMap(entry -> entry[0], entry -> Double.parseDouble(entry[1])));

                // Add to model for Thymeleaf
                UniversityPattern pattern=patternRepo.getReferenceById(patternId);
                Stream streamData=streamRepo.getReferenceById(streamId);
                model.addAttribute("scores", scores);
                model.addAttribute("sem", sem);
                model.addAttribute("studentId", seatNumber);
                model.addAttribute("motherName", motherName);
                model.addAttribute("currDate", currentDate.toString());
                model.addAttribute("pattern", pattern.getName());
                model.addAttribute("Stream", streamData.getName());
                return "Result";
            } catch (Exception e) {
                e.printStackTrace();
                return "errorPage";
            }



        } else {
            // Stay on the same page and show error message
            redirectAttributes.addFlashAttribute("errorMessage", "Result Not Found. Please try again.");
            return "errorPage"; // Replace with the actual page URL
        }
    }

    @PostMapping("/submitRevaluation/")
    public String submitRevaluation(
            @RequestParam("fName") String fName,
            @RequestParam("seatNumber") String seatNumber,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("semester") String semester,
            @RequestParam(value = "subjects", required = false) List<String> subjects,
            @RequestParam("reason") String reason,
            Model model) {

        // Convert List to CSV format (e.g., "Mathematics,Physics,Chemistry")
        String subjectsString = (subjects != null) ? String.join(",", subjects) : "";

        // Save the request
        RevaluationRequest request = new RevaluationRequest();
        request.setfName(fName);
        request.setSeatNumber(seatNumber);
        request.setEmail(email);
        request.setPhone(phone);
        request.setSemester(semester);
        request.setSubjects(subjectsString); // Save as CSV String
        request.setReason(reason);
        try{
            revaluationRequestRepo.save(request);
            model.addAttribute("name",fName);
            model.addAttribute("msg", "Revaluation request submitted successfully!");
            return "PaynowTemplate";
        }catch (Exception e)
        {
            model.addAttribute("emsg", "Unable to submit request please check the details and try again");
            return "RevaluationApplication";
        }

    }

    @ResponseBody
    @PostMapping("/payment_order/")
    public String handlePostRequestOfSeatCount(@RequestBody String requestBody) throws RazorpayException {
        // Parse JSON request
        JSONObject json = new JSONObject(requestBody);
        int amt = json.getInt("amount"); // Extract the "amount" value

        RazorpayClient razorpay = new RazorpayClient("rzp_test_jD41V8O877iJax", "DbhzaEHcg667w7oDiOZsTpyW");
        JSONObject option = new JSONObject();
        option.put("amount", amt * 100);
        option.put("currency", "INR");
        option.put("receipt", "order_rcptid_11");

        Order order = razorpay.orders.create(option);
        System.out.println("Order: " + order);

        return order.toString();
    }

    @GetMapping("/terms/")
    public String getTermPage()
    {
        return "termandcondition";
    }



}
