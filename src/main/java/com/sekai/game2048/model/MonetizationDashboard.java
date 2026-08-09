package com.sekai.game2048.model;

import java.util.ArrayList;
import java.util.List;

public class MonetizationDashboard {

    private List<MonetizationOffer> offers = new ArrayList<>();
    private List<MonetizationLead> myLeads = new ArrayList<>();

    public List<MonetizationOffer> getOffers() {
        return offers;
    }

    public void setOffers(List<MonetizationOffer> offers) {
        this.offers = offers;
    }

    public List<MonetizationLead> getMyLeads() {
        return myLeads;
    }

    public void setMyLeads(List<MonetizationLead> myLeads) {
        this.myLeads = myLeads;
    }
}
