package com.example.zywaa.services;

import com.example.zywaa.entities.StatementRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service

public class CsvStatementService {
    private static final String CSV_FILE_PATH = "C:/Users/user/Downloads/zywa/banking_statements.csv";

    public List<StatementRequest> findStatementsByUserAndDateRange(String userEmail, LocalDate startDate, LocalDate endDate) {
        List<StatementRequest> statements = new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {

                String email = csvRecord.get("user_email");
                LocalDate dateOfTransaction = LocalDate.parse(csvRecord.get("date_of_transaction"));
                String amount = csvRecord.get("amount");

                if (email.equals(userEmail) && !dateOfTransaction.isBefore(startDate) && !dateOfTransaction.isAfter(endDate)) {
                    StatementRequest statement = new StatementRequest();
                    statement.setUser_email(email);
                    statement.setDateOfTransaction(dateOfTransaction);
                    statement.setAmount(amount);
                    statements.add(statement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return statements;
    }

}
