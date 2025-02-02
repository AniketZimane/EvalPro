package com.example.EvalPro.Controller;

import com.example.EvalPro.Dao.*;
import com.example.EvalPro.Entity.*;
import com.example.EvalPro.Entity.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    SemesterRepo semesterRepo;
    @Autowired
    YearsRepo yearsRepo;
    @Autowired
    PatternRepo universityPattern;
    @Autowired
    ResultRepo resultRepo;
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
    public String getEvaluationRequest()
    {
        return "EvaluationRequest";
    }

    @GetMapping("/dashboard/")
    public String getAdminDashboard()
    {
        return "AdminDashboard";
    }

}
