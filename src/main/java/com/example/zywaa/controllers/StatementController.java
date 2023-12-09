package com.example.zywaa.controllers;


import com.example.zywaa.entities.StatementRequest;
import com.example.zywaa.services.CsvStatementService;
import com.example.zywaa.services.EmailService;
import com.example.zywaa.services.PDFGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/statements")
@RestController

public class StatementController {

    @Autowired
    PDFGenerationService pdfGenerationService;
    @Autowired
    EmailService emailService ;
@Autowired
CsvStatementService csvStatementService;

    @PostMapping("/generate")
    public ResponseEntity<?> generatePdf(@RequestBody Map<String, String> payload) {
        try {
            String userEmail = payload.get("user_email");
            LocalDate startDate = LocalDate.parse(payload.get("start_date"));
            LocalDate endDate = LocalDate.parse(payload.get("end_date"));

            ByteArrayInputStream bis = pdfGenerationService.generatePdfReport(userEmail, startDate, endDate);


            byte[] pdfBytes = bis.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "statement.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Unable to generate PDF report: " + e.getMessage());
        }
    }

    private static final Logger log = LoggerFactory.getLogger(StatementController.class);

    @PostMapping("/sendPdf")
    public ResponseEntity<?> sendPdf(@RequestBody Map<String, String> payload) {
        try {
            String userEmail = payload.get("user_email");
            LocalDate startDate = LocalDate.parse(payload.get("start_date"));
            LocalDate endDate = LocalDate.parse(payload.get("end_date"));

            ByteArrayInputStream bis = pdfGenerationService.generatePdfReport(userEmail, startDate, endDate);

            byte[] pdfBytes = bis.readAllBytes();

            ByteArrayInputStream attachmentStream = new ByteArrayInputStream(pdfBytes);

            String emailSubject = "Your Statement from " + startDate + " to " + endDate;
            String emailBody = "Please find attached your statement for the requested period.";

            emailService.sendEmailWithAttachment(userEmail, emailSubject, emailBody, attachmentStream, "statement.pdf");

            return ResponseEntity.ok("Email sent successfully to " + userEmail);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to generate or send PDF report: " + e.getMessage());
        }
    }





    @GetMapping("/statements")
    public ResponseEntity<?> getStatements(
            @RequestParam String userEmail,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            List<StatementRequest> statements = csvStatementService.findStatementsByUserAndDateRange(userEmail, start, end);

            if (statements.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(statements);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Unable to retrieve statements due to: " + e.getMessage());
        }
    }

}
