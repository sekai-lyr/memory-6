package com.sekai.game2048.model;

import java.util.List;

public class MonetizationOffer {

    private String code;
    private String leadType;
    private String title;
    private String priceLabel;
    private String summary;
    private List<String> features;
    private String checkoutUrl;

    public MonetizationOffer() {
    }

    public MonetizationOffer(String code, String leadType, String title, String priceLabel,
                             String summary, List<String> features, String checkoutUrl) {
        this.code = code;
        this.leadType = leadType;
        this.title = title;
        this.priceLabel = priceLabel;
        this.summary = summary;
        this.features = features;
        this.checkoutUrl = checkoutUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLeadType() {
        return leadType;
    }

    public void setLeadType(String leadType) {
        this.leadType = leadType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriceLabel() {
        return priceLabel;
    }

    public void setPriceLabel(String priceLabel) {
        this.priceLabel = priceLabel;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }
}
