package com.example.zywaa.services;


import com.example.zywaa.entities.StatementRequest;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class PDFGenerationService {




        private static final String CSV_FILE_PATH = "C:/Users/user/Downloads/zywa/banking_statements.csv";

    public ByteArrayInputStream generatePdfReport(String userEmail, LocalDate startDate, LocalDate endDate) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(96);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = {1f, 1f};
            table.setWidths(columnWidths);

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

            Font cellFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);

            PdfPCell cellDateHeader = new PdfPCell(new Phrase("Date of Transaction", headerFont));
            PdfPCell cellAmountHeader = new PdfPCell(new Phrase("Amount", headerFont));

            BaseColor headerBackgroundColor = new BaseColor(0, 121, 182);
            cellDateHeader.setBackgroundColor(headerBackgroundColor);
            cellAmountHeader.setBackgroundColor(headerBackgroundColor);
            cellDateHeader.setBorderWidth(1);
            cellAmountHeader.setBorderWidth(1);
            cellDateHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellDateHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellAmountHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellAmountHeader.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(cellDateHeader);
            table.addCell(cellAmountHeader);

            List<StatementRequest> statements = fetchStatementsFromCsv(userEmail, startDate, endDate);

            for (StatementRequest statement : statements) {
                PdfPCell cellDate = new PdfPCell(new Phrase(statement.getDateOfTransaction().toString(), cellFont));
                PdfPCell cellAmount = new PdfPCell(new Phrase(statement.getAmount().toString(), cellFont));

                cellDate.setBorderWidth(1);
                cellAmount.setBorderWidth(1);

                cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellDate.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);

                table.addCell(cellDate);
                table.addCell(cellAmount);
            }

            document.add(table);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
        private List<StatementRequest> fetchStatementsFromCsv(String userEmail, LocalDate startDate, LocalDate endDate) {
            try (Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
                 CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

                List<CSVRecord> records = parser.getRecords();

                return records.stream()
                        .map(record -> new StatementRequest(
                                record.get("user_email"),
                                LocalDate.parse(record.get("date_of_transaction")),
                                record.get("amount")))
                        .filter(statement ->
                                statement.getUser_email().equalsIgnoreCase(userEmail) &&
                                        !statement.getDateOfTransaction().isBefore(startDate) &&
                                        !statement.getDateOfTransaction().isAfter(endDate))
                        .collect(Collectors.toList());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


    }

