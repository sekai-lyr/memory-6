package com.sekai.game2048.service.impl;

import com.sekai.game2048.dataobject.Sekai2048MonetizationLeadDO;
import com.sekai.game2048.mapper.Sekai2048MonetizationLeadMapper;
import com.sekai.game2048.model.MonetizationDashboard;
import com.sekai.game2048.model.MonetizationLead;
import com.sekai.game2048.model.MonetizationLeadRequest;
import com.sekai.game2048.model.MonetizationOffer;
import com.sekai.game2048.model.Result;
import com.sekai.game2048.model.User;
import com.sekai.game2048.service.MonetizationService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MonetizationServiceImpl implements MonetizationService {

    @Resource
    private Sekai2048MonetizationLeadMapper leadMapper;

    @Value("${sekai.checkout.plus-url:}")
    private String plusCheckoutUrl;

    @Value("${sekai.checkout.pack-url:}")
    private String packCheckoutUrl;

    @Value("${sekai.checkout.sponsor-url:}")
    private String sponsorCheckoutUrl;

    @Override
    public List<MonetizationOffer> listOffers() {
        return List.of(
                new MonetizationOffer(
                        "PLUS_MONTHLY",
                        "MEMBERSHIP",
                        "Sekai Plus 月卡",
                        "¥12 / 月",
                        "面向高频玩家的轻会员，提供更清爽的游戏体验和可见身份权益。",
                        List.of("免广告游戏视图", "排行榜 VIP 标识", "VIP Gold 专属主题", "每日礼包和每局额外技能次数"),
                        trimToNull(plusCheckoutUrl)
                ),
                new MonetizationOffer(
                        "CHARACTER_PACK_01",
                        "CHARACTER_PACK",
                        "原创角色扩展包",
                        "¥18 / 包",
                        "把原创角色包作为一次性付费内容，后续可以不断加新包。",
                        List.of("12 个新角色名额", "专属颜色和头像样式预留", "购买后可人工开通或接支付自动开通"),
                        trimToNull(packCheckoutUrl)
                ),
                new MonetizationOffer(
                        "SPONSOR_SLOT",
                        "SPONSOR",
                        "排行榜赞助位",
                        "¥99 起 / 周",
                        "适合接品牌、社群、服务器、二次元周边店等轻赞助。",
                        List.of("首页和排行榜曝光位", "赞助文案审核流程预留", "可按周、月或活动档期报价"),
                        trimToNull(sponsorCheckoutUrl)
                )
        );
    }

    @Override
    public MonetizationDashboard buildDashboard(Long userId) {
        MonetizationDashboard dashboard = new MonetizationDashboard();
        dashboard.setOffers(listOffers());
        if (userId == null) {
            return dashboard;
        }
        try {
            dashboard.setMyLeads(convertList(leadMapper.selectRecentByUserId(userId, 8)));
        } catch (DataAccessException ignored) {
            dashboard.setMyLeads(List.of());
        }
        return dashboard;
    }

    @Override
    public List<MonetizationLead> listRecentLeads(int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 100));
        try {
            return convertList(leadMapper.selectRecent(safeLimit));
        } catch (DataAccessException ignored) {
            return List.of();
        }
    }

    @Override
    @Transactional
    public Result<MonetizationLead> createLead(User user, MonetizationLeadRequest request) {
        if (user == null || user.getId() == null) {
            return Result.fail("请先登录");
        }
        if (request == null) {
            return Result.fail("请填写购买或赞助信息");
        }

        Map<String, MonetizationOffer> offers = listOffers().stream()
                .collect(Collectors.toMap(MonetizationOffer::getCode, Function.identity()));
        MonetizationOffer offer = offers.get(trimToNull(request.getOfferCode()));
        if (offer == null) {
            return Result.fail("请选择有效的变现项目");
        }

        String contactName = trimToMax(request.getContactName(), 64);
        String contactInfo = trimToMax(request.getContactInfo(), 120);
        if (contactName == null || contactInfo == null) {
            return Result.fail("请填写联系人和联系方式");
        }

        Sekai2048MonetizationLeadDO lead = new Sekai2048MonetizationLeadDO();
        lead.setUserId(user.getId());
        lead.setNickName(trimToDefault(user.getNickName(), user.getUserName()));
        lead.setLeadType(offer.getLeadType());
        lead.setOfferCode(offer.getCode());
        lead.setContactName(contactName);
        lead.setContactInfo(contactInfo);
        lead.setCompanyName(trimToMax(request.getCompanyName(), 120));
        lead.setBudgetCents(safeBudget(request.getBudgetCents()));
        lead.setMessage(trimToMax(request.getMessage(), 500));
        lead.setStatus("NEW");

        try {
            int inserted = leadMapper.insert(lead);
            if (inserted <= 0 || lead.getId() == null) {
                return Result.fail("提交失败，请稍后再试");
            }
        } catch (DataAccessException error) {
            return Result.fail("商业化数据表未初始化，请先执行 build-data/init.sql");
        }
        return Result.ok(lead.convertToModel(), "已收到，我们会按这个联系方式跟进");
    }

    private List<MonetizationLead> convertList(List<Sekai2048MonetizationLeadDO> leads) {
        return leads.stream().map(Sekai2048MonetizationLeadDO::convertToModel).toList();
    }

    private Integer safeBudget(Integer value) {
        if (value == null) {
            return 0;
        }
        return Math.max(0, Math.min(value, 99999900));
    }

    private String trimToDefault(String value, String defaultValue) {
        String trimmed = trimToNull(value);
        return trimmed == null ? defaultValue : trimmed;
    }

    private String trimToMax(String value, int maxLength) {
        String trimmed = trimToNull(value);
        if (trimmed == null || trimmed.length() <= maxLength) {
            return trimmed;
        }
        return trimmed.substring(0, maxLength);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
