package com.sekai.game2048.model;

public class MonetizationLeadRequest {

    private String offerCode;
    private String contactName;
    private String contactInfo;
    private String companyName;
    private Integer budgetCents;
    private String message;

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getBudgetCents() {
        return budgetCents;
    }

    public void setBudgetCents(Integer budgetCents) {
        this.budgetCents = budgetCents;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
