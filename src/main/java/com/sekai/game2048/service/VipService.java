package com.sekai.game2048.service;

import com.sekai.game2048.model.Result;
import com.sekai.game2048.model.User;
import com.sekai.game2048.model.VipCloudSave;
import com.sekai.game2048.model.VipCloudSaveRequest;
import com.sekai.game2048.model.VipCoachReport;
import com.sekai.game2048.model.VipCoachRequest;
import com.sekai.game2048.model.VipDailyGift;
import com.sekai.game2048.model.VipGrowthReport;

import java.util.List;

public interface VipService {

    Result<VipDailyGift> getDailyGiftStatus(User user);

    Result<VipDailyGift> claimDailyGift(User user);

    Result<List<VipCloudSave>> listCloudSaves(User user);

    Result<VipCloudSave> saveCloudSave(User user, VipCloudSaveRequest request);

    Result<VipCloudSave> getCloudSave(User user, Long id);

    Result<Void> deleteCloudSave(User user, Long id);

    Result<VipCoachReport> analyzeBoard(User user, VipCoachRequest request);

    Result<VipGrowthReport> getGrowthReport(User user);
}
