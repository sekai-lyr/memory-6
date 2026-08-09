package com.sekai.game2048.service.impl;

import com.sekai.game2048.dataobject.Sekai2048GameRecordDO;
import com.sekai.game2048.dataobject.Sekai2048VipCloudSaveDO;
import com.sekai.game2048.dataobject.Sekai2048VipDailyGiftDO;
import com.sekai.game2048.mapper.Sekai2048GameRecordMapper;
import com.sekai.game2048.mapper.Sekai2048VipCloudSaveMapper;
import com.sekai.game2048.mapper.Sekai2048VipDailyGiftMapper;
import com.sekai.game2048.model.GameStats;
import com.sekai.game2048.model.Result;
import com.sekai.game2048.model.User;
import com.sekai.game2048.model.VipCloudSave;
import com.sekai.game2048.model.VipCloudSaveRequest;
import com.sekai.game2048.model.VipCoachMove;
import com.sekai.game2048.model.VipCoachPlan;
import com.sekai.game2048.model.VipCoachReport;
import com.sekai.game2048.model.VipCoachRequest;
import com.sekai.game2048.model.VipDailyGift;
import com.sekai.game2048.model.VipGrowthReport;
import com.sekai.game2048.model.VipTrainingTask;
import com.sekai.game2048.service.VipService;
import jakarta.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class VipServiceImpl implements VipService {

    private static final ZoneId REWARD_ZONE = ZoneId.of("Asia/Shanghai");
    private static final int SCORE_BONUS = 256;
    private static final int BOOST_BONUS = 1;
    private static final int SCAN_BONUS = 1;
    private static final int CLOUD_SAVE_LIMIT = 8;
    private static final List<String> DIRECTIONS = List.of("left", "right", "up", "down");

    @Resource
    private Sekai2048VipDailyGiftMapper vipDailyGiftMapper;

    @Resource
    private Sekai2048VipCloudSaveMapper vipCloudSaveMapper;

    @Resource
    private Sekai2048GameRecordMapper gameRecordMapper;

    @Override
    public Result<VipDailyGift> getDailyGiftStatus(User user) {
        if (user == null || user.getId() == null) {
            return Result.fail("请先登录");
        }
        if (!user.isVipActive()) {
            return Result.fail("VIP 专属礼包，请先开通会员");
        }

        LocalDate today = LocalDate.now(REWARD_ZONE);
        Sekai2048VipDailyGiftDO existing = vipDailyGiftMapper.selectByUserIdAndRewardDate(user.getId(), today);
        if (existing != null) {
            return Result.ok(alreadyClaimed(today), "今天的 VIP 礼包已经领取过了");
        }
        return Result.ok(available(today), "今天的 VIP 礼包可以领取");
    }

    @Override
    @Transactional
    public Result<VipDailyGift> claimDailyGift(User user) {
        if (user == null || user.getId() == null) {
            return Result.fail("请先登录");
        }
        if (!user.isVipActive()) {
            return Result.fail("VIP 专属礼包，请先开通会员");
        }

        LocalDate today = LocalDate.now(REWARD_ZONE);
        Sekai2048VipDailyGiftDO existing = vipDailyGiftMapper.selectByUserIdAndRewardDate(user.getId(), today);
        if (existing != null) {
            return Result.ok(alreadyClaimed(today), "今天的 VIP 礼包已经领取过了");
        }

        Sekai2048VipDailyGiftDO gift = new Sekai2048VipDailyGiftDO();
        gift.setUserId(user.getId());
        gift.setRewardDate(today);
        gift.setScoreBonus(SCORE_BONUS);
        gift.setBoostBonus(BOOST_BONUS);
        gift.setScanBonus(SCAN_BONUS);

        try {
            vipDailyGiftMapper.insert(gift);
        } catch (DuplicateKeyException ignored) {
            return Result.ok(alreadyClaimed(today), "今天的 VIP 礼包已经领取过了");
        }
        return Result.ok(available(today), "VIP 礼包领取成功");
    }

    @Override
    public Result<List<VipCloudSave>> listCloudSaves(User user) {
        Result<Void> vipCheck = checkVip(user);
        if (!vipCheck.isSuccess()) {
            return Result.fail(vipCheck.getMessage());
        }
        return Result.ok(convertCloudSaves(vipCloudSaveMapper.selectRecentByUserId(user.getId(), CLOUD_SAVE_LIMIT)),
                "云端存档已加载");
    }

    @Override
    @Transactional
    public Result<VipCloudSave> saveCloudSave(User user, VipCloudSaveRequest request) {
        Result<Void> vipCheck = checkVip(user);
        if (!vipCheck.isSuccess()) {
            return Result.fail(vipCheck.getMessage());
        }
        if (request == null) {
            return Result.fail("云端存档数据不能为空");
        }

        String slotName = trimToMax(request.getSlotName(), 64);
        String runData = trimToMax(request.getRunData(), 20000);
        if (slotName == null) {
            return Result.fail("请输入云端存档名称");
        }
        if (runData == null) {
            return Result.fail("云端存档内容不能为空");
        }

        Sekai2048VipCloudSaveDO existing = vipCloudSaveMapper.selectByUserIdAndSlotName(user.getId(), slotName);
        if (existing == null && vipCloudSaveMapper.countByUserId(user.getId()) >= CLOUD_SAVE_LIMIT) {
            return Result.fail("VIP 云端存档最多保存 " + CLOUD_SAVE_LIMIT + " 个槽位");
        }

        Sekai2048VipCloudSaveDO cloudSave = new Sekai2048VipCloudSaveDO();
        cloudSave.setUserId(user.getId());
        cloudSave.setSlotName(slotName);
        cloudSave.setRunData(runData);
        cloudSave.setScore(safeNumber(request.getScore()));
        cloudSave.setMaxTile(Math.max(2, safeNumber(request.getMaxTile())));
        cloudSave.setMoveCount(safeNumber(request.getMoveCount()));
        cloudSave.setDurationSeconds(safeNumber(request.getDurationSeconds()));
        cloudSave.setMode(trimToDefault(request.getMode(), "classic", 32));
        cloudSave.setBoardSize(Math.max(4, Math.min(safeNumber(request.getBoardSize()), 5)));
        cloudSave.setTargetTile(Math.max(1024, safeNumber(request.getTargetTile())));

        vipCloudSaveMapper.upsert(cloudSave);
        Sekai2048VipCloudSaveDO saved = vipCloudSaveMapper.selectByUserIdAndSlotName(user.getId(), slotName);
        return Result.ok(saved.convertToModel(), "云端存档已保存");
    }

    @Override
    public Result<VipCloudSave> getCloudSave(User user, Long id) {
        Result<Void> vipCheck = checkVip(user);
        if (!vipCheck.isSuccess()) {
            return Result.fail(vipCheck.getMessage());
        }
        if (id == null) {
            return Result.fail("请选择云端存档");
        }
        Sekai2048VipCloudSaveDO cloudSave = vipCloudSaveMapper.selectByIdAndUserId(id, user.getId());
        if (cloudSave == null) {
            return Result.fail("云端存档不存在");
        }
        return Result.ok(cloudSave.convertToModel(), "云端存档已加载");
    }

    @Override
    @Transactional
    public Result<Void> deleteCloudSave(User user, Long id) {
        Result<Void> vipCheck = checkVip(user);
        if (!vipCheck.isSuccess()) {
            return Result.fail(vipCheck.getMessage());
        }
        if (id == null) {
            return Result.fail("请选择云端存档");
        }
        int deleted = vipCloudSaveMapper.deleteByIdAndUserId(id, user.getId());
        if (deleted <= 0) {
            return Result.fail("云端存档不存在");
        }
        return Result.ok(null, "云端存档已删除");
    }

    @Override
    public Result<VipCoachReport> analyzeBoard(User user, VipCoachRequest request) {
        Result<Void> vipCheck = checkVip(user);
        if (!vipCheck.isSuccess()) {
            return Result.fail(vipCheck.getMessage());
        }
        if (request == null || request.getBoard() == null || request.getBoard().isEmpty()) {
            return Result.fail("请提供当前棋盘");
        }

        int size = Math.max(4, Math.min(safeNumber(request.getBoardSize()), 5));
        int[][] board = normalizeBoard(request.getBoard(), size);
        int empty = countEmpty(board);
        int mergePairs = countMergePairs(board);
        int maxTile = maxTile(board);
        boolean cornerMax = maxInCorner(board, maxTile);
        int currentRisk = riskScore(empty, mergePairs, cornerMax);

        List<VipCoachMove> moves = DIRECTIONS.stream()
                .map(direction -> analyzeMove(board, direction))
                .sorted(Comparator.comparing(VipCoachMove::isLegal).reversed()
                        .thenComparing(VipCoachMove::getEvaluation, Comparator.reverseOrder()))
                .toList();
        List<VipCoachPlan> plans = buildPlans(board, 3);

        VipCoachMove best = moves.stream()
                .filter(VipCoachMove::isLegal)
                .max(Comparator.comparing(VipCoachMove::getEvaluation))
                .orElse(null);

        VipCoachReport report = new VipCoachReport();
        report.setRisk(currentRisk);
        report.setEmptyCells(empty);
        report.setMergePairs(mergePairs);
        report.setMaxTile(maxTile);
        report.setMaxTileInCorner(cornerMax);
        report.setMoves(moves);
        report.setPlans(plans);
        report.setPlanSummary(buildPlanSummary(plans));
        if (best == null) {
            report.setBestDirection("none");
            report.setSummary("已经没有可移动方向。");
            report.setAdvice("这局已经锁死。可以先保存成绩，然后重新开始。");
            return Result.ok(report, "VIP 高级分析已完成");
        }

        report.setBestDirection(best.getDirection());
        report.setSummary("最佳方向：" + directionLabel(best.getDirection())
                + " · 预计得分 +" + best.getGain()
                + " · 风险 " + best.getRiskAfter() + "/100");
        report.setAdvice(buildAdvice(currentRisk, best, cornerMax, empty, mergePairs, request.getMode()));
        return Result.ok(report, "VIP 高级分析已完成");
    }

    @Override
    public Result<VipGrowthReport> getGrowthReport(User user) {
        Result<Void> vipCheck = checkVip(user);
        if (!vipCheck.isSuccess()) {
            return Result.fail(vipCheck.getMessage());
        }

        List<Sekai2048GameRecordDO> recent = gameRecordMapper.selectRecentByUserId(user.getId(), 20);
        GameStats stats = gameRecordMapper.selectStatsByUserId(user.getId());
        if (stats == null) {
            stats = new GameStats();
        }

        VipGrowthReport report = new VipGrowthReport();
        int totalRuns = Math.toIntExact(Math.min(Integer.MAX_VALUE, gameRecordMapper.countByUserId(user.getId())));
        int bestScore = safeNumber(stats.getBestScore());
        int bestTile = Math.max(2, safeNumber(stats.getBestTile()));
        int recentAverage = averageScore(recent, 0, 5);
        int previousAverage = averageScore(recent, 5, 10);
        int trendPercent = previousAverage <= 0 ? (recentAverage > 0 ? 100 : 0)
                : Math.round((recentAverage - previousAverage) * 100.0f / previousAverage);
        int consistency = consistencyScore(recent, bestScore);
        int efficiency = efficiencyScore(recent);
        int nextTargetScore = nextTargetScore(bestScore, recentAverage);
        int nextTargetTile = nextTargetTile(bestTile);
        String weakness = primaryWeakness(recent, consistency, efficiency, bestTile);

        report.setTotalRuns(totalRuns);
        report.setBestScore(bestScore);
        report.setBestTile(bestTile);
        report.setRecentAverageScore(recentAverage);
        report.setPreviousAverageScore(previousAverage);
        report.setTrendPercent(trendPercent);
        report.setConsistencyScore(consistency);
        report.setEfficiencyScore(efficiency);
        report.setNextTargetScore(nextTargetScore);
        report.setNextTargetTile(nextTargetTile);
        report.setLevelName(levelName(bestScore, bestTile, totalRuns));
        report.setPrimaryWeakness(weakness);
        report.setHeadline(buildGrowthHeadline(totalRuns, trendPercent, bestScore));
        report.setNextAction(buildNextAction(weakness, nextTargetScore, nextTargetTile));
        report.setInsights(buildGrowthInsights(recent, totalRuns, trendPercent, consistency, efficiency, bestScore, bestTile));
        report.setTasks(buildTrainingTasks(recent, recentAverage, nextTargetScore, nextTargetTile, consistency, efficiency));
        return Result.ok(report, "VIP 成长报告已生成");
    }

    private int averageScore(List<Sekai2048GameRecordDO> records, int from, int to) {
        if (records == null || records.size() <= from) {
            return 0;
        }
        int end = Math.min(records.size(), to);
        int total = 0;
        int count = 0;
        for (int index = from; index < end; index += 1) {
            total += safeNumber(records.get(index).getScore());
            count += 1;
        }
        return count == 0 ? 0 : Math.round(total * 1.0f / count);
    }

    private int consistencyScore(List<Sekai2048GameRecordDO> records, int bestScore) {
        if (records == null || records.isEmpty() || bestScore <= 0) {
            return 0;
        }
        int sampleSize = Math.min(10, records.size());
        int stableRuns = 0;
        int target = Math.max(1, Math.round(bestScore * 0.65f));
        for (int index = 0; index < sampleSize; index += 1) {
            if (safeNumber(records.get(index).getScore()) >= target) {
                stableRuns += 1;
            }
        }
        return Math.min(100, Math.round(stableRuns * 100.0f / sampleSize));
    }

    private int efficiencyScore(List<Sekai2048GameRecordDO> records) {
        if (records == null || records.isEmpty()) {
            return 0;
        }
        int sampleSize = Math.min(10, records.size());
        int total = 0;
        for (int index = 0; index < sampleSize; index += 1) {
            Sekai2048GameRecordDO record = records.get(index);
            int score = safeNumber(record.getScore());
            int moves = Math.max(1, safeNumber(record.getMoveCount()));
            int seconds = Math.max(1, safeNumber(record.getDurationSeconds()));
            int scorePerMove = Math.min(70, Math.round(score * 1.0f / moves));
            int pace = Math.min(30, Math.round(score * 12.0f / seconds));
            total += Math.min(100, scorePerMove + pace);
        }
        return Math.round(total * 1.0f / sampleSize);
    }

    private int nextTargetScore(int bestScore, int recentAverage) {
        int base = Math.max(bestScore, recentAverage);
        if (base <= 0) {
            return 1000;
        }
        int target = Math.max(base + 500, Math.round(base * 1.18f));
        return ((target + 99) / 100) * 100;
    }

    private int nextTargetTile(int bestTile) {
        if (bestTile < 512) {
            return 512;
        }
        if (bestTile < 1024) {
            return 1024;
        }
        if (bestTile < 2048) {
            return 2048;
        }
        if (bestTile < 4096) {
            return 4096;
        }
        return 8192;
    }

    private String primaryWeakness(List<Sekai2048GameRecordDO> records, int consistency, int efficiency, int bestTile) {
        if (records == null || records.size() < 3) {
            return "样本不足";
        }
        if (consistency < 45) {
            return "稳定性不足";
        }
        if (efficiency < 45) {
            return "效率偏低";
        }
        if (bestTile < 1024) {
            return "合成深度不足";
        }
        return "冲榜突破";
    }

    private String levelName(int bestScore, int bestTile, int totalRuns) {
        if (totalRuns < 3) {
            return "新手观察期";
        }
        if (bestTile >= 4096 || bestScore >= 30000) {
            return "冲榜高手";
        }
        if (bestTile >= 2048 || bestScore >= 16000) {
            return "高级玩家";
        }
        if (bestTile >= 1024 || bestScore >= 8000) {
            return "进阶玩家";
        }
        return "成长玩家";
    }

    private String buildGrowthHeadline(int totalRuns, int trendPercent, int bestScore) {
        if (totalRuns <= 0) {
            return "还没有保存成绩，先完成并保存一局，VIP 才能给你做真实成长分析。";
        }
        if (totalRuns < 3) {
            return "样本还少，先保存 3 局，系统会开始识别你的稳定区间。";
        }
        if (trendPercent >= 15) {
            return "最近状态明显上升，适合冲击新的最高分。";
        }
        if (trendPercent <= -15) {
            return "最近表现有回落，建议先做稳定训练，不急着冲分。";
        }
        return "你的表现进入稳定期，下一步要靠路线规划突破 " + Math.max(bestScore, 1000) + " 分。";
    }

    private String buildNextAction(String weakness, int nextTargetScore, int nextTargetTile) {
        return switch (weakness) {
            case "样本不足" -> "先保存 3 局成绩，让系统建立你的个人基准。";
            case "稳定性不足" -> "今天先练 3 局稳分，把每局分数保持在近期平均分附近。";
            case "效率偏低" -> "少用无收益移动，优先选择能保留空格的方向。";
            case "合成深度不足" -> "本轮目标不是追总分，而是稳定合成到 " + nextTargetTile + "。";
            default -> "下一目标：冲到 " + nextTargetScore + " 分，并尝试合成 " + nextTargetTile + "。";
        };
    }

    private List<String> buildGrowthInsights(List<Sekai2048GameRecordDO> records,
                                             int totalRuns,
                                             int trendPercent,
                                             int consistency,
                                             int efficiency,
                                             int bestScore,
                                             int bestTile) {
        List<String> insights = new ArrayList<>();
        insights.add("累计保存 " + totalRuns + " 局，最高分 " + bestScore + "，最大方块 " + bestTile + "。");
        insights.add("近期趋势 " + signedPercent(trendPercent) + "，稳定分 " + consistency + "/100，效率分 " + efficiency + "/100。");
        if (records != null && !records.isEmpty()) {
            Sekai2048GameRecordDO latest = records.getFirst();
            insights.add("最近一局：" + safeNumber(latest.getScore()) + " 分，" + safeNumber(latest.getMoveCount())
                    + " 步，最大方块 " + Math.max(2, safeNumber(latest.getMaxTile())) + "。");
        }
        if (consistency < 45) {
            insights.add("你的分数波动比较大，VIP 建议先把低分局抬高，再冲最高分。");
        } else if (efficiency < 45) {
            insights.add("你的局面能撑住，但每步收益偏低，适合练习少走空步。");
        } else {
            insights.add("基础表现不错，适合使用三步路线规划挑战更高分。");
        }
        return insights;
    }

    private List<VipTrainingTask> buildTrainingTasks(List<Sekai2048GameRecordDO> records,
                                                     int recentAverage,
                                                     int nextTargetScore,
                                                     int nextTargetTile,
                                                     int consistency,
                                                     int efficiency) {
        int sampleSize = records == null ? 0 : Math.min(10, records.size());
        int stableTarget = Math.max(800, Math.round(Math.max(recentAverage, 1000) * 0.85f));
        int stableProgress = countScoreAtLeast(records, stableTarget, 10);
        int targetTileProgress = maxRecentTile(records, 10);
        int efficientProgress = countEfficientRuns(records, recentAverage, 10);

        List<VipTrainingTask> tasks = new ArrayList<>();
        tasks.add(trainingTask("稳分训练",
                "最近 10 局中完成 3 局分数不低于 " + stableTarget + "。",
                stableProgress,
                3,
                "完成后说明你的低分局正在减少。"));
        tasks.add(trainingTask("合成深度训练",
                "把最大方块推进到 " + nextTargetTile + "。",
                Math.min(targetTileProgress, nextTargetTile),
                nextTargetTile,
                "完成后适合继续挑战更高目标。"));
        tasks.add(trainingTask("效率训练",
                "最近 10 局中完成 2 局“分数不低于近期平均，且每步收益更高”。",
                efficientProgress,
                2,
                "完成后你的操作会更接近高手局。"));
        tasks.add(trainingTask("VIP 冲刺目标",
                "下一次保存成绩时冲到 " + nextTargetScore + " 分。",
                Math.max(0, Math.min(bestRecentScore(records), nextTargetScore)),
                nextTargetScore,
                consistency >= 65 && efficiency >= 55 ? "你已经接近冲榜状态。" : "建议先配合 VIP 高级分析一起练。"));
        return tasks;
    }

    private VipTrainingTask trainingTask(String title, String description, int progress, int target, String rewardHint) {
        VipTrainingTask task = new VipTrainingTask();
        task.setTitle(title);
        task.setDescription(description);
        task.setProgress(Math.max(0, progress));
        task.setTarget(Math.max(1, target));
        task.setCompleted(progress >= target);
        task.setRewardHint(rewardHint);
        return task;
    }

    private int countScoreAtLeast(List<Sekai2048GameRecordDO> records, int targetScore, int limit) {
        if (records == null) {
            return 0;
        }
        int count = 0;
        for (int index = 0; index < Math.min(limit, records.size()); index += 1) {
            if (safeNumber(records.get(index).getScore()) >= targetScore) {
                count += 1;
            }
        }
        return count;
    }

    private int maxRecentTile(List<Sekai2048GameRecordDO> records, int limit) {
        if (records == null) {
            return 0;
        }
        int maxTile = 0;
        for (int index = 0; index < Math.min(limit, records.size()); index += 1) {
            maxTile = Math.max(maxTile, safeNumber(records.get(index).getMaxTile()));
        }
        return maxTile;
    }

    private int countEfficientRuns(List<Sekai2048GameRecordDO> records, int recentAverage, int limit) {
        if (records == null || records.isEmpty()) {
            return 0;
        }
        int count = 0;
        int targetScore = Math.max(800, recentAverage);
        for (int index = 0; index < Math.min(limit, records.size()); index += 1) {
            Sekai2048GameRecordDO record = records.get(index);
            int moves = Math.max(1, safeNumber(record.getMoveCount()));
            int score = safeNumber(record.getScore());
            if (score >= targetScore && score / moves >= 45) {
                count += 1;
            }
        }
        return count;
    }

    private int bestRecentScore(List<Sekai2048GameRecordDO> records) {
        if (records == null) {
            return 0;
        }
        int best = 0;
        for (Sekai2048GameRecordDO record : records) {
            best = Math.max(best, safeNumber(record.getScore()));
        }
        return best;
    }

    private String signedPercent(int value) {
        return (value > 0 ? "+" : "") + value + "%";
    }

    private Result<Void> checkVip(User user) {
        if (user == null || user.getId() == null) {
            return Result.fail("请先登录");
        }
        if (!user.isVipActive()) {
            return Result.fail("VIP 专属功能，请先开通会员");
        }
        return Result.ok(null, "OK");
    }

    private List<VipCloudSave> convertCloudSaves(List<Sekai2048VipCloudSaveDO> cloudSaves) {
        return cloudSaves.stream().map(Sekai2048VipCloudSaveDO::convertToModel).toList();
    }

    private Integer safeNumber(Integer value) {
        return value == null ? 0 : Math.max(0, value);
    }

    private String trimToDefault(String value, String defaultValue, int maxLength) {
        String trimmed = trimToMax(value, maxLength);
        return trimmed == null ? defaultValue : trimmed;
    }

    private String trimToMax(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed.length() <= maxLength ? trimmed : trimmed.substring(0, maxLength);
    }

    private int[][] normalizeBoard(List<List<Integer>> rawBoard, int size) {
        int[][] board = new int[size][size];
        for (int row = 0; row < size; row += 1) {
            if (row >= rawBoard.size() || rawBoard.get(row) == null) {
                continue;
            }
            List<Integer> rawRow = rawBoard.get(row);
            for (int col = 0; col < size && col < rawRow.size(); col += 1) {
                Integer value = rawRow.get(col);
                board[row][col] = value == null ? 0 : Math.max(0, value);
            }
        }
        return board;
    }

    private VipCoachMove analyzeMove(int[][] board, String direction) {
        MoveSimulation simulation = simulate(board, direction);
        int empty = countEmpty(simulation.board());
        int mergePairs = countMergePairs(simulation.board());
        int maxTile = maxTile(simulation.board());
        boolean cornerMax = maxInCorner(simulation.board(), maxTile);
        int risk = riskScore(empty, mergePairs, cornerMax);

        VipCoachMove move = new VipCoachMove();
        move.setDirection(direction);
        move.setLegal(simulation.changed());
        move.setGain(simulation.gain());
        move.setEmptyCells(empty);
        move.setMergePairs(mergePairs);
        move.setMaxTile(maxTile);
        move.setRiskAfter(risk);
        move.setEvaluation(simulation.changed()
                ? simulation.gain() * 3 + empty * 60 + mergePairs * 30 + (cornerMax ? 140 : 0) + maxTile / 16 - risk * 4
                : -10000);
        move.setReason(reasonForMove(move, cornerMax));
        return move;
    }

    private List<VipCoachPlan> buildPlans(int[][] board, int maxDepth) {
        List<PlanSearch> searches = new ArrayList<>();
        searchPlans(board, new ArrayList<>(), 0, maxDepth, 0, searches);
        return searches.stream()
                .sorted(Comparator.comparing(PlanSearch::evaluation).reversed())
                .limit(3)
                .map(this::convertPlan)
                .toList();
    }

    private void searchPlans(int[][] board,
                             List<String> steps,
                             int depth,
                             int maxDepth,
                             int totalGain,
                             List<PlanSearch> searches) {
        if (depth >= maxDepth) {
            addPlanSearch(steps, board, totalGain, searches);
            return;
        }

        boolean hasLegalMove = false;
        for (String direction : DIRECTIONS) {
            MoveSimulation simulation = simulate(board, direction);
            if (!simulation.changed()) {
                continue;
            }
            hasLegalMove = true;
            steps.add(direction);
            searchPlans(simulation.board(), steps, depth + 1, maxDepth, totalGain + simulation.gain(), searches);
            steps.remove(steps.size() - 1);
        }

        if (!hasLegalMove) {
            addPlanSearch(steps, board, totalGain, searches);
        }
    }

    private void addPlanSearch(List<String> steps, int[][] board, int totalGain, List<PlanSearch> searches) {
        if (steps.isEmpty()) {
            return;
        }
        int empty = countEmpty(board);
        int mergePairs = countMergePairs(board);
        int maxTile = maxTile(board);
        boolean cornerMax = maxInCorner(board, maxTile);
        int risk = riskScore(empty, mergePairs, cornerMax);
        int stability = stabilityScore(board, maxTile, cornerMax);
        int evaluation = totalGain * 3
                + empty * 90
                + mergePairs * 45
                + maxTile / 8
                + stability
                - risk * 5
                - steps.size() * 8;
        searches.add(new PlanSearch(new ArrayList<>(steps), totalGain, risk, empty, mergePairs, maxTile, evaluation));
    }

    private VipCoachPlan convertPlan(PlanSearch search) {
        VipCoachPlan plan = new VipCoachPlan();
        plan.setSteps(search.steps());
        plan.setRoute(routeLabel(search.steps()));
        plan.setTotalGain(search.totalGain());
        plan.setFinalRisk(search.finalRisk());
        plan.setFinalEmptyCells(search.finalEmptyCells());
        plan.setFinalMergePairs(search.finalMergePairs());
        plan.setFinalMaxTile(search.finalMaxTile());
        plan.setEvaluation(search.evaluation());
        plan.setReason(planReason(search));
        return plan;
    }

    private int stabilityScore(int[][] board, int maxTile, boolean cornerMax) {
        int score = cornerMax ? 220 : -80;
        score += monotonicScore(board);
        score -= roughnessPenalty(board) / 16;
        if (maxTile >= 1024 && cornerMax) {
            score += 80;
        }
        return score;
    }

    private int monotonicScore(int[][] board) {
        int score = 0;
        for (int[] row : board) {
            score += monotonicLineScore(row);
        }
        int size = board.length;
        for (int col = 0; col < size; col += 1) {
            int[] line = new int[size];
            for (int row = 0; row < size; row += 1) {
                line[row] = board[row][col];
            }
            score += monotonicLineScore(line);
        }
        return score;
    }

    private int monotonicLineScore(int[] line) {
        boolean ascending = true;
        boolean descending = true;
        int filled = 0;
        for (int index = 0; index < line.length - 1; index += 1) {
            int current = line[index];
            int next = line[index + 1];
            if (current > 0) {
                filled += 1;
            }
            if (next > current && current > 0) {
                descending = false;
            }
            if (next < current && next > 0) {
                ascending = false;
            }
        }
        if (line[line.length - 1] > 0) {
            filled += 1;
        }
        if (filled <= 1) {
            return 12;
        }
        if (ascending || descending) {
            return 36;
        }
        return -14;
    }

    private int roughnessPenalty(int[][] board) {
        int penalty = 0;
        int size = board.length;
        for (int row = 0; row < size; row += 1) {
            for (int col = 0; col < size; col += 1) {
                int value = board[row][col];
                if (value == 0) {
                    continue;
                }
                if (col + 1 < size && board[row][col + 1] > 0) {
                    penalty += Math.abs(value - board[row][col + 1]);
                }
                if (row + 1 < size && board[row + 1][col] > 0) {
                    penalty += Math.abs(value - board[row + 1][col]);
                }
            }
        }
        return penalty;
    }

    private String buildPlanSummary(List<VipCoachPlan> plans) {
        if (plans.isEmpty()) {
            return "当前没有可规划路线。";
        }
        VipCoachPlan bestPlan = plans.getFirst();
        return "三步路线建议：" + bestPlan.getRoute()
                + "。预计累计得分 +" + bestPlan.getTotalGain()
                + "，路线末端风险 " + bestPlan.getFinalRisk() + "/100。每走一步后建议重新分析。";
    }

    private String routeLabel(List<String> steps) {
        return String.join(" → ", steps.stream().map(this::directionLabel).toList());
    }

    private String planReason(PlanSearch search) {
        if (search.finalRisk() <= 30 && search.finalEmptyCells() >= 4) {
            return "路线末端风险低，空格充足，适合稳扎稳打。";
        }
        if (search.totalGain() >= 1024) {
            return "这条路线短期收益高，适合冲分或快速合成。";
        }
        if (search.finalMergePairs() >= 3) {
            return "路线末端保留多个可合成机会，后续选择更多。";
        }
        return "这条路线在风险、空格和合成机会之间最均衡。";
    }

    private MoveSimulation simulate(int[][] board, String direction) {
        int[][] working = copyBoard(board);
        int gain = 0;
        int size = board.length;
        if ("left".equals(direction) || "right".equals(direction)) {
            for (int row = 0; row < size; row += 1) {
                int[] line = new int[size];
                for (int col = 0; col < size; col += 1) {
                    line[col] = "left".equals(direction) ? working[row][col] : working[row][size - 1 - col];
                }
                SlideResult result = slideLine(line);
                gain += result.gain();
                for (int col = 0; col < size; col += 1) {
                    if ("left".equals(direction)) {
                        working[row][col] = result.line()[col];
                    } else {
                        working[row][size - 1 - col] = result.line()[col];
                    }
                }
            }
        } else {
            for (int col = 0; col < size; col += 1) {
                int[] line = new int[size];
                for (int row = 0; row < size; row += 1) {
                    line[row] = "up".equals(direction) ? working[row][col] : working[size - 1 - row][col];
                }
                SlideResult result = slideLine(line);
                gain += result.gain();
                for (int row = 0; row < size; row += 1) {
                    if ("up".equals(direction)) {
                        working[row][col] = result.line()[row];
                    } else {
                        working[size - 1 - row][col] = result.line()[row];
                    }
                }
            }
        }
        return new MoveSimulation(working, gain, !boardsEqual(board, working));
    }

    private SlideResult slideLine(int[] line) {
        List<Integer> values = new ArrayList<>();
        for (int value : line) {
            if (value > 0) {
                values.add(value);
            }
        }
        int[] merged = new int[line.length];
        int index = 0;
        int gain = 0;
        for (int i = 0; i < values.size(); i += 1) {
            if (i + 1 < values.size() && values.get(i).equals(values.get(i + 1))) {
                int next = values.get(i) * 2;
                merged[index] = next;
                gain += next;
                i += 1;
            } else {
                merged[index] = values.get(i);
            }
            index += 1;
        }
        return new SlideResult(merged, gain);
    }

    private int countEmpty(int[][] board) {
        int empty = 0;
        for (int[] row : board) {
            for (int value : row) {
                if (value == 0) {
                    empty += 1;
                }
            }
        }
        return empty;
    }

    private int countMergePairs(int[][] board) {
        int pairs = 0;
        int size = board.length;
        for (int row = 0; row < size; row += 1) {
            for (int col = 0; col < size; col += 1) {
                int value = board[row][col];
                if (value == 0) {
                    continue;
                }
                if (col + 1 < size && board[row][col + 1] == value) {
                    pairs += 1;
                }
                if (row + 1 < size && board[row + 1][col] == value) {
                    pairs += 1;
                }
            }
        }
        return pairs;
    }

    private int maxTile(int[][] board) {
        int max = 0;
        for (int[] row : board) {
            for (int value : row) {
                max = Math.max(max, value);
            }
        }
        return Math.max(max, 2);
    }

    private boolean maxInCorner(int[][] board, int maxTile) {
        int last = board.length - 1;
        return board[0][0] == maxTile
                || board[0][last] == maxTile
                || board[last][0] == maxTile
                || board[last][last] == maxTile;
    }

    private int riskScore(int empty, int mergePairs, boolean maxInCorner) {
        int risk = 96 - empty * 11 - mergePairs * 9 - (maxInCorner ? 18 : 0);
        return Math.max(0, Math.min(100, risk));
    }

    private String reasonForMove(VipCoachMove move, boolean cornerMax) {
        if (!move.isLegal()) {
            return "这个方向不会移动任何方块。";
        }
        if (move.getRiskAfter() <= 35 && move.getEmptyCells() >= 4) {
            return "移动后空格较多，局面比较灵活。";
        }
        if (move.getGain() > 0) {
            return "可以立刻合成，同时保留后续空间。";
        }
        if (cornerMax) {
            return "能保持最大方块在角落附近，继续寻找合成机会。";
        }
        return "当前空间和风险平衡最好。";
    }

    private String buildAdvice(int currentRisk, VipCoachMove best, boolean cornerMax, int empty, int mergePairs, String mode) {
        if (best.getRiskAfter() >= 70) {
            return "风险较高。先" + directionLabel(best.getDirection())
                    + "，下一步优先制造空格，不要急着追分。";
        }
        if (!cornerMax && best.getMaxTile() >= 128) {
            return "最大方块还没有固定在角落。现在先" + directionLabel(best.getDirection())
                    + "，之后把最大方块慢慢推到角落。";
        }
        if (empty <= 2 && mergePairs <= 1) {
            return "空间很紧。建议" + directionLabel(best.getDirection())
                    + "，同时避免把相同数字拆开。";
        }
        if ("daily".equalsIgnoreCase(mode)) {
            return "这是每日挑战。" + directionLabel(best.getDirection())
                    + "对今天这套棋盘最稳。";
        }
        if (best.getRiskAfter() < currentRisk) {
            return directionLabel(best.getDirection())
                    + "可以把风险从 " + currentRisk + " 降到 " + best.getRiskAfter() + "。";
        }
        return "建议" + directionLabel(best.getDirection())
                + "，这是合成收益、空格数量和角落控制最均衡的一步。";
    }

    private String directionLabel(String direction) {
        if (direction == null) {
            return "无";
        }
        return switch (direction) {
            case "left" -> "向左";
            case "right" -> "向右";
            case "up" -> "向上";
            case "down" -> "向下";
            default -> "无";
        };
    }

    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[board.length][board.length];
        for (int row = 0; row < board.length; row += 1) {
            System.arraycopy(board[row], 0, copy[row], 0, board.length);
        }
        return copy;
    }

    private boolean boardsEqual(int[][] left, int[][] right) {
        for (int row = 0; row < left.length; row += 1) {
            for (int col = 0; col < left.length; col += 1) {
                if (left[row][col] != right[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    private VipDailyGift available(LocalDate today) {
        return new VipDailyGift(false, SCORE_BONUS, BOOST_BONUS, SCAN_BONUS, today);
    }

    private VipDailyGift alreadyClaimed(LocalDate today) {
        return new VipDailyGift(true, 0, 0, 0, today);
    }

    private record MoveSimulation(int[][] board, int gain, boolean changed) {
    }

    private record SlideResult(int[] line, int gain) {
    }

    private record PlanSearch(List<String> steps,
                              int totalGain,
                              int finalRisk,
                              int finalEmptyCells,
                              int finalMergePairs,
                              int finalMaxTile,
                              int evaluation) {
    }
}
