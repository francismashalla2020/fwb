package com.thinkbold.fwb.models;

public class Get_payment_history {

    private String loan_type;
    private String amount;
    private String payment_date;
    private String description;
    private String loan_amount;
    private String balance;
    private String status;

    public Get_payment_history(String loan_type, String amount, String payment_date,
                               String description, String loan_amount, String balance, String status) {
        this.loan_type = loan_type;
        this.amount = amount;
        this.payment_date = payment_date;
        this.description = description;
        this.loan_amount = loan_amount;
        this.balance = balance;
        this.status = status;
    }

    public String getLoan_type() {
        return loan_type;
    }

    public void setLoan_type(String loan_type) {
        this.loan_type = loan_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(String loan_amount) {
        this.loan_amount = loan_amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
