package com.thinkbold.fwb.models;

public class Get_Endorse {
    private String referee_id, loan_id, member_id, member_name, amount, approval_status;

    public Get_Endorse(String referee_id, String loan_id, String member_id, String member_name, String amount, String approval_status) {
        this.referee_id = referee_id;
        this.loan_id = loan_id;
        this.member_id = member_id;
        this.member_name = member_name;
        this.amount = amount;
        this.approval_status = approval_status;
    }

    public String getReferee_id() {
        return referee_id;
    }

    public void setReferee_id(String referee_id) {
        this.referee_id = referee_id;
    }

    public String getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(String loan_id) {
        this.loan_id = loan_id;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(String approval_status) {
        this.approval_status = approval_status;
    }
}
