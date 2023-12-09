package com.example.zywaa.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class StatementRequest {


    private int id;
    private String  amount;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate DateOfTransaction;


    private String User_email;

    public StatementRequest(String userEmail, LocalDate dateOfTransaction, String amount) {
        this.User_email = userEmail;
        this.DateOfTransaction = dateOfTransaction;
        this.amount = amount;
    }


    public StatementRequest() {

    }
}
