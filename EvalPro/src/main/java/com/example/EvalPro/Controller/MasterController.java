package com.example.EvalPro.Controller;

import com.example.EvalPro.Dao.*;
import com.example.EvalPro.Entity.*;
import com.example.EvalPro.Entity.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class MasterController {

    @Autowired
    SubjectRepo subjectRepo;
    @Autowired
    StreamRepo streamRepo;
    @Autowired
    RegistrationRepo registrationRepo;
    @Autowired
    SemesterRepo semesterRepo;
    @Autowired
    YearsRepo yearsRepo;
    @Autowired
    PatternRepo universityPattern;
    @Autowired
    ResultRepo resultRepo;
    @Autowired
    RevaluationRequestRepo revaluationRequestRepo;
    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("paper/analysis/")
    public String getAiAnlysisPage()
    {
        return "AiAnsManupulation";
    }
    @GetMapping("/result/")
    public String getDashboardPage(Model model) {
        List<Subject> subjectList=subjectRepo.findAll();
        List<Stream> streamList=streamRepo.findAll();
        List<UniversityPattern> universityPatternList=universityPattern.findAll();
        List<Years> yearsList=yearsRepo.findAll();
        model.addAttribute("subjectList",subjectList);
        model.addAttribute("streamList",streamList);
        model.addAttribute("universityPatternList",universityPatternList);
        model.addAttribute("yearsList",yearsList);
        return "resultUpload";
    }
    @GetMapping("/sem/")
    public String getsemPage() {
        return "sem";
    }
    @GetMapping("/pattern/")
    public String getPatternPage() {
        return "pattern";
    }
    @GetMapping("/subject/")
    public String getSubjectPage() {
        return "subject";
    }
    @GetMapping("/academic-year/")
    public String getAcademyYearPage() {
        return "Acadmicyear";
    }
    @GetMapping("/academic-steam/")
    public String getStreamPage() {
        return "stream";
    }

    @GetMapping("/getSemesters")
    public ResponseEntity<List<Semester>> getSemesters(@RequestParam Long streamId) {
        System.out.println("streamId"+streamId);
        List<Semester> semesters = semesterRepo.findByStreamId(streamId);
        return ResponseEntity.ok(semesters);
    }

    @GetMapping("/getSubjects")
    public ResponseEntity<List<String>> getSubjects(@RequestParam Long stream_id, @RequestParam Long sem_id) {
        System.out.println("Stream ID: " + stream_id + ", Semester ID: " + sem_id);

        List<String> subjectNames = subjectRepo.findSubjectNamesByStreamAndSemester(stream_id, sem_id);
        return ResponseEntity.ok(subjectNames);

    }

    @PostMapping("/uploadExcel")
    public ResponseEntity<String> uploadResult(
            @RequestParam("file") MultipartFile file,
            @RequestParam("pattern") String pattern,
            @RequestParam("stream") String stream,
            @RequestParam("year") String year,
            @RequestParam("sem") String sem) {

        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Get first sheet

            List<Result> results = new ArrayList<>();
            Row headerRow = sheet.getRow(0); // First row for subject names

            // Extract subject names dynamically (after "Mother Name" column)
            List<String> subjectNames = new ArrayList<>();
            for (int i = 3; i < headerRow.getLastCellNum(); i++) { // Starts after "Mother Name"
                subjectNames.add(headerRow.getCell(i).getStringCellValue());
            }

            // Read student data from row 1 onwards
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Result result = new Result();
                result.setStudentId(row.getCell(0).getStringCellValue());
                result.setMotherName(row.getCell(1).getStringCellValue());
                result.setPattern(pattern);
                result.setStream(stream);
                result.setYear(year);
                result.setSem(sem);
                result.setPublishDate(new Date().toString()); // Set current date

                // Store subjects & marks dynamically in JSON format
                Map<String, Double> subjectMarks = new LinkedHashMap<>();
                for (int j = 3; j < row.getLastCellNum(); j++) { // Starts from Subject columns
                    Cell cell = row.getCell(j);
                    double mark = cell != null ? cell.getNumericCellValue() : 0.0;
                    subjectMarks.put(subjectNames.get(j - 3), mark);
                }
                result.setSubjectMarksJson(subjectMarks.toString()); // Convert Map to JSON String

                results.add(result);
            }

            resultRepo.saveAll(results); // Save all data
            workbook.close();
            return ResponseEntity.ok("Excel Data Uploaded Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        }
    }

    @GetMapping("/evaluation/request/")
    public String getEvaluationRequest(Model model)
    {
        List<RevaluationRequest> revaluationRequests=revaluationRequestRepo.findAll();
        model.addAttribute("evaluationList",revaluationRequests);
        return "EvaluationRequest";
    }

    @GetMapping("/dashboard/")
    public String getAdminDashboard()
    {
        return "AdminDashboard";
    }

    @GetMapping("/delete/{id}/")
    public String deleteRecord(Model model, @PathVariable Long id) {
        revaluationRequestRepo.deleteById(id);
        List<RevaluationRequest> revaluationRequests=revaluationRequestRepo.findAll();
        model.addAttribute("evaluationList",revaluationRequests);
        return "EvaluationRequest";
    }

    @GetMapping("/rechecking/record/")
    public String getRecheckingRequest(Model model)
    {
        List<RevaluationRequest> revaluationRequests=revaluationRequestRepo.findAll();
        model.addAttribute("revaluationRequests",revaluationRequests);
        return "recheckingRequest";
    }

    @GetMapping("/evaluation/record/")
    public String getEvaluation(Model model)
    {
        List<RevaluationRequest> revaluationRequests=revaluationRequestRepo.findUnevaluatedRevaluationRequests();
        model.addAttribute("revaluationRequests",revaluationRequests);
        return "recheckingRequest";
    }

    @GetMapping("/moderate/record/")
    public String getModerate(Model model)
    {
        List<RevaluationRequest> revaluationRequests=revaluationRequestRepo.findEvaluatedButNotModeratedRequests();
        model.addAttribute("revaluationRequests",revaluationRequests);
        return "recheckingRequest";
    }

    @GetMapping("/verify/record/")
    public String getVerification(Model model)
    {
        List<RevaluationRequest> revaluationRequests=revaluationRequestRepo.findEvaluatedModeratedButNotVerifiedRequests();
        model.addAttribute("revaluationRequests",revaluationRequests);
        return "recheckingRequest";
    }

    @GetMapping("/status/update/{id}/")
    public String getUpdate(@PathVariable Long id, Model model) {
        System.out.println(id);
        Optional<RevaluationRequest> optionalRequest = revaluationRequestRepo.findById(id);

        if (optionalRequest.isPresent()) {
            RevaluationRequest revaluationRequest = optionalRequest.get();
            System.out.println(revaluationRequest.getSeatNumber());
            System.out.println(revaluationRequest.getIsModerate());
            System.out.println(revaluationRequest.getIsEvaluate());
            System.out.println(revaluationRequest.getIsVerify());
            // Add required fields to the model
            model.addAttribute("email", revaluationRequest.getEmail());
            model.addAttribute("name", revaluationRequest.getfName());
            model.addAttribute("seatNumber", revaluationRequest.getSeatNumber());
            model.addAttribute("isModerate", revaluationRequest.getIsModerate());
            model.addAttribute("isEvaluate", revaluationRequest.getIsEvaluate());
            model.addAttribute("isVerify", revaluationRequest.getIsVerify());

            return "finalVerificationForm"; // Returning the template
        } else {
            model.addAttribute("error", "Revaluation request not found.");
            return "errorPage"; // Redirect to an error page if ID not found
        }
    }

    @PostMapping("/update/status/data/")
    public String updateRevaluationStatus(
            @RequestParam("studentId") String studentId,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("isEvaluate") String isEvaluate,
            @RequestParam("isModerate") String isModerate,
            @RequestParam("isVerify") String isVerify,
            @RequestParam(value = "marksUpdated", required = false) String marksUpdated,
            @RequestParam(value = "marks", required = false) String marks,
            @RequestParam(value = "remark", required = false) String reason) {

        if ("no".equalsIgnoreCase(marksUpdated)) {
            marks = null; // Reset marks if "No" is selected
            reason = "Not Updated"; // Set remark to "Not Updated"
        }

        List<RevaluationRequest> revaluationRequests = revaluationRequestRepo.findRevaluationRequestsBySeatNumber(studentId);
        for (RevaluationRequest data : revaluationRequests) {
            RevaluationRequest request = new RevaluationRequest(
                    data.getId(), name, studentId, email, data.getPhone(), data.getCourse(), data.getSemester(),
                    data.getSubjects(), reason + (marks != null ? marks : ""), data.getIsPayment(), isVerify, isEvaluate,
                    "Eval123", isModerate, "moderator123", data.getRemark());

            revaluationRequestRepo.save(request);
            System.out.println("Data updated");

            // **Send Email Notification**
            try {
                sendRevaluationEmail(email, name, studentId, isEvaluate, isModerate, isVerify, marksUpdated, marks, reason);
                System.out.println("Email sent successfully to " + email);
            } catch (Exception e) {
                System.err.println("Email sending failed: " + e.getMessage());
            }
        }

        return "finalVerificationForm"; // Redirect after update
    }

    // **Method to send an email notification**
    private void sendRevaluationEmail(String toEmail, String name, String studentId, String isEvaluate,
                                      String isModerate, String isVerify, String marksUpdated, String marks, String reason)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String htmlContent = "<html><body>"
                + "<h2>Revaluation Status Update</h2>"
                + "<p>Dear <b>" + name + "</b>,</p>"
                + "<p>Your request for revaluation/moderation/verification has been processed. Below are the updated details:</p>"
                + "<div style='background-color: #eef5ff; padding: 15px; border-left: 4px solid #007bff; border-radius: 5px;'>"
                + "<p><b>Student ID:</b> " + studentId + "</p>"
                + "<p><b>Evaluation Status:</b> " + isEvaluate + "</p>"
                + "<p><b>Moderation Status:</b> " + isModerate + "</p>"
                + "<p><b>Verification Status:</b> " + isVerify + "</p>"
                + "<p><b>Marks Updated:</b> " + marksUpdated + "</p>"
                + "<p><b>Updated Marks:</b> " + (marks != null ? marks : "Not Updated") + "</p>"
                + "<p><b>Remarks:</b> " + reason + "</p>"
                + "</div>"
                + "<p>If you have any questions, please contact the administration.</p>"
                + "<p style='color: #777;'>Regards,<br>University Revaluation Department</p>"
                + "</body></html>";

        helper.setTo(toEmail);
        helper.setSubject("Revaluation Status Update");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }





}
