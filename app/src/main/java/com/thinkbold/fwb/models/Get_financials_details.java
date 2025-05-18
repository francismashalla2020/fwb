package com.thinkbold.fwb.models;

public class Get_financials_details {

    private String member_id;
    private String member_name;
    private String shares;

    private String long_term_loan;
    private String long_term_paid;
    private String long_term_loan_balance;
    private String long_term_status;
    private String long_term_last_payment_date;

    private String emergency_loan;
    private String emergency_paid;
    private String emergency_loan_balance;
    private String emergency_status;
    private String emergency_last_payment_date;

    public Get_financials_details(String member_id, String member_name, String shares,
                                  String long_term_loan, String long_term_paid, String long_term_loan_balance,
                                  String long_term_status, String long_term_last_payment_date,
                                  String emergency_loan, String emergency_paid, String emergency_loan_balance,
                                  String emergency_status, String emergency_last_payment_date) {
        this.member_id = member_id;
        this.member_name = member_name;
        this.shares = shares;
        this.long_term_loan = long_term_loan;
        this.long_term_paid = long_term_paid;
        this.long_term_loan_balance = long_term_loan_balance;
        this.long_term_status = long_term_status;
        this.long_term_last_payment_date = long_term_last_payment_date;
        this.emergency_loan = emergency_loan;
        this.emergency_paid = emergency_paid;
        this.emergency_loan_balance = emergency_loan_balance;
        this.emergency_status = emergency_status;
        this.emergency_last_payment_date = emergency_last_payment_date;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getLong_term_loan() {
        return long_term_loan;
    }

    public void setLong_term_loan(String long_term_loan) {
        this.long_term_loan = long_term_loan;
    }

    public String getLong_term_paid() {
        return long_term_paid;
    }

    public void setLong_term_paid(String long_term_paid) {
        this.long_term_paid = long_term_paid;
    }

    public String getLong_term_loan_balance() {
        return long_term_loan_balance;
    }

    public void setLong_term_loan_balance(String long_term_loan_balance) {
        this.long_term_loan_balance = long_term_loan_balance;
    }

    public String getLong_term_status() {
        return long_term_status;
    }

    public void setLong_term_status(String long_term_status) {
        this.long_term_status = long_term_status;
    }

    public String getLong_term_last_payment_date() {
        return long_term_last_payment_date;
    }

    public void setLong_term_last_payment_date(String long_term_last_payment_date) {
        this.long_term_last_payment_date = long_term_last_payment_date;
    }

    public String getEmergency_loan() {
        return emergency_loan;
    }

    public void setEmergency_loan(String emergency_loan) {
        this.emergency_loan = emergency_loan;
    }

    public String getEmergency_paid() {
        return emergency_paid;
    }

    public void setEmergency_paid(String emergency_paid) {
        this.emergency_paid = emergency_paid;
    }

    public String getEmergency_loan_balance() {
        return emergency_loan_balance;
    }

    public void setEmergency_loan_balance(String emergency_loan_balance) {
        this.emergency_loan_balance = emergency_loan_balance;
    }

    public String getEmergency_status() {
        return emergency_status;
    }

    public void setEmergency_status(String emergency_status) {
        this.emergency_status = emergency_status;
    }

    public String getEmergency_last_payment_date() {
        return emergency_last_payment_date;
    }

    public void setEmergency_last_payment_date(String emergency_last_payment_date) {
        this.emergency_last_payment_date = emergency_last_payment_date;
    }
}
