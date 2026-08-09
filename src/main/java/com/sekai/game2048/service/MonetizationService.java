package com.sekai.game2048.service;

import com.sekai.game2048.model.MonetizationDashboard;
import com.sekai.game2048.model.MonetizationLead;
import com.sekai.game2048.model.MonetizationLeadRequest;
import com.sekai.game2048.model.MonetizationOffer;
import com.sekai.game2048.model.Result;
import com.sekai.game2048.model.User;

import java.util.List;

public interface MonetizationService {

    List<MonetizationOffer> listOffers();

    MonetizationDashboard buildDashboard(Long userId);

    List<MonetizationLead> listRecentLeads(int limit);

    Result<MonetizationLead> createLead(User user, MonetizationLeadRequest request);
}
