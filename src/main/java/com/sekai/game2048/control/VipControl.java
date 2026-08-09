package com.sekai.game2048.control;

import com.sekai.game2048.model.Result;
import com.sekai.game2048.model.User;
import com.sekai.game2048.model.VipCloudSave;
import com.sekai.game2048.model.VipCloudSaveRequest;
import com.sekai.game2048.model.VipCoachReport;
import com.sekai.game2048.model.VipCoachRequest;
import com.sekai.game2048.model.VipDailyGift;
import com.sekai.game2048.model.VipGrowthReport;
import com.sekai.game2048.service.VipService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VipControl {

    @Resource
    private VipService vipService;

    @GetMapping("/vip/daily-gift")
    public Result<VipDailyGift> dailyGiftStatus(HttpSession session) {
        Object value = session.getAttribute("loginUser");
        if (!(value instanceof User user)) {
            return Result.fail("请先登录");
        }
        return vipService.getDailyGiftStatus(user);
    }

    @PostMapping("/vip/daily-gift")
    public Result<VipDailyGift> claimDailyGift(HttpSession session) {
        Object value = session.getAttribute("loginUser");
        if (!(value instanceof User user)) {
            return Result.fail("请先登录");
        }
        return vipService.claimDailyGift(user);
    }

    @GetMapping("/vip/cloud-saves")
    public Result<List<VipCloudSave>> cloudSaves(HttpSession session) {
        Object value = session.getAttribute("loginUser");
        if (!(value instanceof User user)) {
            return Result.fail("请先登录");
        }
        return vipService.listCloudSaves(user);
    }

    @PostMapping("/vip/cloud-saves")
    public Result<VipCloudSave> saveCloudSave(@RequestBody VipCloudSaveRequest request,
                                              HttpSession session) {
        Object value = session.getAttribute("loginUser");
        if (!(value instanceof User user)) {
            return Result.fail("请先登录");
        }
        return vipService.saveCloudSave(user, request);
    }

    @GetMapping("/vip/cloud-saves/{id}")
    public Result<VipCloudSave> getCloudSave(@PathVariable("id") Long id, HttpSession session) {
        Object value = session.getAttribute("loginUser");
        if (!(value instanceof User user)) {
            return Result.fail("请先登录");
        }
        return vipService.getCloudSave(user, id);
    }

    @DeleteMapping("/vip/cloud-saves/{id}")
    public Result<Void> deleteCloudSave(@PathVariable("id") Long id, HttpSession session) {
        Object value = session.getAttribute("loginUser");
        if (!(value instanceof User user)) {
            return Result.fail("请先登录");
        }
        return vipService.deleteCloudSave(user, id);
    }

    @PostMapping("/vip/pro-coach")
    public Result<VipCoachReport> proCoach(@RequestBody VipCoachRequest request, HttpSession session) {
        Object value = session.getAttribute("loginUser");
        if (!(value instanceof User user)) {
            return Result.fail("请先登录");
        }
        return vipService.analyzeBoard(user, request);
    }

    @GetMapping("/vip/growth-report")
    public Result<VipGrowthReport> growthReport(HttpSession session) {
        Object value = session.getAttribute("loginUser");
        if (!(value instanceof User user)) {
            return Result.fail("请先登录");
        }
        return vipService.getGrowthReport(user);
    }
}
