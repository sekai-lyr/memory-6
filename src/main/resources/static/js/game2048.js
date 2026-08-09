const characters = {
    2: { name: "流光脉冲", color: "#35c7bd", hair: "#34d5c8", hair2: "#129a98", eyes: "#17f4e8", aura: "#b7fff8", type: "twin" },
    4: { name: "皮可火花", color: "#f28ab2", hair: "#ff9fbe", hair2: "#f06d9f", eyes: "#72da75", aura: "#ffe0ec", type: "horn" },
    8: { name: "奈拉余烬", color: "#dc6a5f", hair: "#24151a", hair2: "#f37b62", eyes: "#f7a2b4", aura: "#ffd0c9", type: "ribbon" },
    16: { name: "希艾尔星铃", color: "#4c85e8", hair: "#f5d56a", hair2: "#d99e31", eyes: "#7ee0ff", aura: "#e6f4ff", type: "crown" },
    32: { name: "鲁米潮汐", color: "#64a7ff", hair: "#9fd0ff", hair2: "#4d8ee0", eyes: "#2c6dff", aura: "#d8ecff", type: "crest" },
    64: { name: "索拉溪谷", color: "#f19c4b", hair: "#df8a37", hair2: "#ffe0a3", eyes: "#8fd86d", aura: "#fff0c2", type: "blade" },
    128: { name: "绮拉灰羽", color: "#6e7688", hair: "#181b25", hair2: "#4b5363", eyes: "#7f8ca8", aura: "#d5d9e2", type: "scarf" },
    256: { name: "奥里恩流光", color: "#7890ff", hair: "#f5f7ff", hair2: "#b7c1ff", eyes: "#69f1ff", aura: "#e6eaff", type: "visor" },
    512: { name: "艾拉叶影", color: "#72bf91", hair: "#dfe9d8", hair2: "#a8c59e", eyes: "#70d18c", aura: "#e4ffec", type: "wing" },
    1024: { name: "诺瓦螺旋", color: "#7552b8", hair: "#1b1727", hair2: "#49306f", eyes: "#d94cff", aura: "#f1dcff", type: "sigil" },
    2048: { name: "金辉花影", color: "#a98bef", hair: "#f1d36b", hair2: "#b88732", eyes: "#77b7ff", aura: "#fff0b7", type: "flower" },
    4096: { name: "奇迹世界", color: "#d6a629", hair: "#fff4b0", hair2: "#f27f45", eyes: "#ffffff", aura: "#fff8d2", type: "star" }
};

const $ = (id) => document.getElementById(id);
const boardEl = $("board");
const scoreEl = $("score");
const bestScoreEl = $("bestScore");
const moveCountEl = $("moveCount");
const comboCountEl = $("comboCount");
const maxTileLabel = $("maxTileLabel");
const timeLabel = $("timeLabel");
const hintEl = $("gameHint");
const missionGrid = $("missionGrid");
const compactMissions = $("compactMissions");
const overlay = $("gameOverlay");
const overlayTitle = $("overlayTitle");
const overlayText = $("overlayText");
const runDialog = $("runDialog");
const runDataBox = $("runDataBox");
const dialogTitle = $("dialogTitle");
const cloudDialog = $("cloudDialog");
const cloudSaveList = $("cloudSaveList");
const modeSelect = $("modeSelect");
const themeSelect = $("themeSelect");
const boardSizeSelect = $("boardSizeSelect");
const targetTileSelect = $("targetTileSelect");
const soundToggle = $("soundToggle");
const trackTitle = $("trackTitle");
const trackMood = $("trackMood");
const volumeSlider = $("volumeSlider");
const focusLeftEl = $("focusLeft");
const boostLeftEl = $("boostLeft");
const scanLeftEl = $("scanLeft");
const autoLeftEl = $("autoLeft");

const storageKey = "sekai2048.current";
const settingsKey = "sekai2048.settings";
const slotKey = "sekai2048.slot";
const dailySeed = new Date().toISOString().slice(0, 10).replaceAll("-", "");

const missions = [
    { id: "score1000", title: "初次爆发", text: "分数达到 1000", done: () => score >= 1000 },
    { id: "tile512", title: "角色觉醒", text: "合成一个 512 方块", done: () => maxTile() >= 512 },
    { id: "move50", title: "稳定操作", text: "完成 50 步", done: () => moves >= 50 },
    { id: "combo4", title: "连击高手", text: "连击达到 x4", done: () => combo >= 4 },
    { id: "tile2048", title: "Sekai 大师", text: "合成一个 2048 方块", done: () => maxTile() >= 2048 },
    { id: "fastWin", title: "快速通关", text: "10 分钟内合成 2048", done: () => maxTile() >= 2048 && elapsedSeconds() <= 600 }
];

const tracks = [
    { title: "深夜合成", mood: "安静游戏房间", base: 196, accent: 246 },
    { title: "樱花连击", mood: "轻快街机流行", base: 220, accent: 330 },
    { title: "霓虹专注", mood: "清爽合成循环", base: 174, accent: 261 }
];

const directionLabels = { left: "向左", right: "向右", up: "向上", down: "向下", none: "无" };
const modeLabels = { classic: "经典", chaos: "压力", zen: "练习", daily: "每日挑战" };

let board = [];
let score = 0;
let moves = 0;
let startedAt = Date.now();
let saved = false;
let touchStart = null;
let history = [];
let combo = 1;
let shuffleLeft = 2;
let achieved = new Set();
let timer = null;
let mode = "classic";
let paused = false;
let rngState = 1;
let audioContext = null;
let musicTimer = null;
let currentTrack = 0;
let soundEnabled = true;
let focusLeft = 2;
let boostLeft = 3;
let scanLeft = 3;
let autoLeft = 5;
let boardSize = 4;
let targetTile = 2048;
const vipActive = document.body?.dataset.vip === "true";
let vipGiftClaimed = false;

function startGame() {
    boardSize = Number(boardSizeSelect.value || boardSize || 4);
    targetTile = Number(targetTileSelect.value || targetTile || 2048);
    board = Array.from({ length: boardSize }, () => Array(boardSize).fill(0));
    score = 0;
    moves = 0;
    startedAt = Date.now();
    saved = false;
    history = [];
    combo = 1;
    shuffleLeft = mode === "chaos" ? 1 : 2;
    focusLeft = mode === "chaos" ? 1 : 2;
    boostLeft = mode === "zen" ? 5 : 3;
    scanLeft = 3;
    autoLeft = mode === "zen" ? 8 : 5;
    applyVipBoosts();
    achieved = new Set();
    paused = false;
    rngState = mode === "daily" ? Number(dailySeed) : Date.now();
    overlay.classList.add("hidden");
    addRandomTile();
    addRandomTile();
    startTimer();
    render();
    saveProgress();
}

function continueGame() {
    loadSettings();
    const cached = localStorage.getItem(storageKey);
    if (!cached) {
        startGame();
        return;
    }
    try {
        const data = JSON.parse(cached);
        board = data.board;
        score = data.score || 0;
        moves = data.moves || 0;
        startedAt = Date.now() - (data.elapsedSeconds || 0) * 1000;
        mode = data.mode || "classic";
        boardSize = data.boardSize || data.board?.length || 4;
        targetTile = data.targetTile || 2048;
        combo = data.combo || 1;
        shuffleLeft = data.shuffleLeft ?? 2;
        focusLeft = data.focusLeft ?? 2;
        boostLeft = data.boostLeft ?? 3;
        scanLeft = data.scanLeft ?? 3;
        autoLeft = data.autoLeft ?? 5;
        rngState = data.rngState || Date.now();
        achieved = new Set(data.achieved || []);
        modeSelect.value = mode;
        boardSizeSelect.value = String(boardSize);
        targetTileSelect.value = String(targetTile);
        saved = false;
        history = [];
        paused = false;
        startTimer();
        render();
        hintEl.textContent = "已恢复上次未完成的对局。";
    } catch (error) {
        startGame();
    }
}

function random() {
    rngState = (rngState * 1664525 + 1013904223) >>> 0;
    return rngState / 4294967296;
}

function addRandomTile() {
    const empty = [];
    for (let r = 0; r < boardSize; r += 1) {
        for (let c = 0; c < boardSize; c += 1) {
            if (board[r][c] === 0) {
                empty.push([r, c]);
            }
        }
    }
    if (!empty.length) {
        return;
    }
    const [row, col] = empty[Math.floor(random() * empty.length)];
    const chanceForFour = mode === "chaos" ? 0.28 : 0.1;
    board[row][col] = random() < chanceForFour ? 4 : 2;
}

function render() {
    boardEl.innerHTML = "";
    boardEl.style.setProperty("--board-size", String(boardSize));
    boardEl.classList.toggle("size-5", boardSize === 5);
    board.flat().forEach((value, index) => {
        const tile = document.createElement("div");
        tile.className = value ? "tile" : "tile empty";
        tile.dataset.index = String(index);
        if (value) {
            const character = getCharacter(value);
            tile.style.setProperty("--tile-color", character.color);
            tile.style.setProperty("--hair", character.hair);
            tile.style.setProperty("--hair2", character.hair2);
            tile.style.setProperty("--eyes", character.eyes);
            tile.style.setProperty("--aura", character.aura);
            tile.dataset.type = character.type;
            tile.innerHTML = `
                <span class="tile-value">${value}</span>
                <span class="character-portrait" aria-hidden="true">
                    <span class="aura"></span>
                    <span class="hair back"></span>
                    <span class="face">
                        <span class="eye left"></span>
                        <span class="eye right"></span>
                        <span class="mouth"></span>
                    </span>
                    <span class="bang bang-one"></span>
                    <span class="bang bang-two"></span>
                    <span class="accessory"></span>
                </span>
                <span class="tile-name">${character.name}</span>
            `;
        }
        boardEl.appendChild(tile);
    });
    scoreEl.textContent = score;
    moveCountEl.textContent = moves;
    comboCountEl.textContent = `x${combo}`;
    maxTileLabel.textContent = maxTile();
    timeLabel.textContent = formatTime(elapsedSeconds());
    renderSkillCounts();
    if (score > Number(bestScoreEl.textContent || 0)) {
        bestScoreEl.textContent = score;
    }
    renderTrack();
    renderMissions();
    checkAchievements();
    if (hasWon() && !paused) {
        showOverlay(`已达成 ${targetTile}`, "可以保存成绩，也可以继续挑战更高分。", false);
    } else if (mode !== "zen" && isGameOver() && !paused) {
        showOverlay("游戏结束", "已经没有可移动方向了。保存成绩后再来一局吧。", false);
    }
}

function renderSkillCounts() {
    focusLeftEl.textContent = focusLeft;
    boostLeftEl.textContent = boostLeft;
    scanLeftEl.textContent = scanLeft;
    autoLeftEl.textContent = autoLeft;
    $("focusSkillBtn").disabled = focusLeft <= 0;
    $("boostSkillBtn").disabled = boostLeft <= 0;
    $("scanSkillBtn").disabled = scanLeft <= 0;
    $("autoSkillBtn").disabled = autoLeft <= 0;
    refreshVipGiftButton();
}

function applyVipBoosts() {
    if (!vipActive) {
        return;
    }
    focusLeft += 1;
    boostLeft += 2;
    scanLeft += 2;
    autoLeft += 3;
}

function refreshVipGiftButton() {
    const button = $("vipGiftBtn");
    if (!button) {
        return;
    }
    button.disabled = vipGiftClaimed;
    button.textContent = vipGiftClaimed ? "今日礼包已领取" : "领取 VIP 礼包";
}

async function loadVipGiftStatus() {
    if (!vipActive || !$("vipGiftBtn")) {
        return;
    }
    try {
        const response = await fetch("/vip/daily-gift");
        const result = await response.json();
        if (result.success && result.data) {
            vipGiftClaimed = result.data.alreadyClaimed === true;
            refreshVipGiftButton();
        }
    } catch (error) {
        hintEl.textContent = "VIP 礼包状态加载失败。";
    }
}

async function loadServerStats() {
    const box = $("serverStats");
    if (!box) return;
    try {
        const response = await fetch("/game2048/stats");
        const result = await response.json();
        if (!result.success) {
            box.innerHTML = "<span>请先登录。</span>";
            return;
        }
        const stats = result.data;
        box.innerHTML = `
            <div><strong>${stats.totalRuns}</strong><small>总局数</small></div>
            <div><strong>${stats.bestScore}</strong><small>最高分</small></div>
            <div><strong>${stats.averageScore}</strong><small>平均分</small></div>
            <div><strong>${stats.bestTile}</strong><small>最大方块</small></div>
            <div><strong>${formatTime(stats.totalSeconds || 0)}</strong><small>总用时</small></div>
            <div><strong>${stats.totalMoves}</strong><small>总步数</small></div>
            <div><strong>${stats.bestCharacter || "暂无"}</strong><small>最佳角色</small></div>
            <div><strong>${stats.favoriteCharacter || "暂无"}</strong><small>常见角色</small></div>
        `;
    } catch (error) {
        box.innerHTML = "<span>统计数据加载失败。</span>";
    }
}

function renderTrack() {
    const track = $("characterTrack");
    track.innerHTML = "";
    Object.keys(characters).slice(0, 11).forEach((value) => {
        const item = document.createElement("span");
        item.textContent = `${value} ${characters[value].name}`;
        track.appendChild(item);
    });
}

function getCharacter(value) {
    return characters[value] || characters[4096];
}

function move(direction) {
    if (paused) {
        return;
    }
    pushHistory();
    const before = JSON.stringify(board);
    const scoreBefore = score;
    if (direction === "left") board = board.map(slideLine);
    if (direction === "right") board = board.map((line) => slideLine([...line].reverse()).reverse());
    if (direction === "up") board = transpose(transpose(board).map(slideLine));
    if (direction === "down") board = transpose(transpose(board).map((line) => slideLine([...line].reverse()).reverse()));

    if (JSON.stringify(board) !== before) {
        moves += 1;
        saved = false;
        combo = score > scoreBefore ? Math.min(combo + 1, 9) : 1;
        addRandomTile();
        playBeep(score > scoreBefore ? 660 : 330, 0.05);
        render();
        saveProgress();
    } else {
        history.pop();
        combo = 1;
    }
}

function slideLine(line) {
    const values = line.filter(Boolean);
    const merged = [];
    for (let i = 0; i < values.length; i += 1) {
        if (values[i] === values[i + 1]) {
            const nextValue = values[i] * 2;
            merged.push(nextValue);
            score += nextValue * combo;
            i += 1;
        } else {
            merged.push(values[i]);
        }
    }
    while (merged.length < boardSize) merged.push(0);
    return merged;
}

function pushHistory() {
    history.push({
        board: board.map((row) => [...row]),
        score,
        moves,
        combo,
        shuffleLeft,
        focusLeft,
        boostLeft,
        scanLeft,
        autoLeft,
        rngState
    });
    if (history.length > 20) history.shift();
}

function undoMove() {
    const previous = history.pop();
    if (!previous) {
        hintEl.textContent = "当前没有可撤销的步骤。";
        return;
    }
    board = previous.board;
    score = previous.score;
    moves = previous.moves;
    combo = previous.combo;
    shuffleLeft = previous.shuffleLeft;
    focusLeft = previous.focusLeft ?? focusLeft;
    boostLeft = previous.boostLeft ?? boostLeft;
    scanLeft = previous.scanLeft ?? scanLeft;
    autoLeft = previous.autoLeft ?? autoLeft;
    rngState = previous.rngState;
    saved = false;
    overlay.classList.add("hidden");
    render();
    saveProgress();
    hintEl.textContent = "已撤销上一步。";
}

function clearTileMarks() {
    boardEl.querySelectorAll(".tile").forEach((tile) => {
        tile.classList.remove("hint-target", "scan-target", "skill-flash");
    });
}

function useFocusSkill() {
    if (focusLeft <= 0) return;
    const filled = board.flat().filter(Boolean);
    if (filled.length <= 1) {
        hintEl.textContent = "专注技能需要棋盘上至少有两个方块。";
        return;
    }
    pushHistory();
    const lowest = Math.min(...filled);
    const candidates = [];
    board.forEach((row, r) => row.forEach((value, c) => {
        if (value === lowest) candidates.push([r, c]);
    }));
    const [r, c] = candidates[Math.floor(random() * candidates.length)];
    board[r][c] = 0;
    focusLeft -= 1;
    moves += 1;
    combo = 1;
    saved = false;
    render();
    flashIndex(r * boardSize + c);
    saveProgress();
    hintEl.textContent = `专注已移除一个 ${lowest} 方块。`;
}

function useBoostSkill() {
    if (boostLeft <= 0) return;
    const candidates = [];
    board.forEach((row, r) => row.forEach((value, c) => {
        if (value > 0 && value <= 128) candidates.push([r, c, value]);
    }));
    if (!candidates.length) {
        hintEl.textContent = "强化技能需要一个 128 或以下的方块。";
        return;
    }
    pushHistory();
    candidates.sort((a, b) => a[2] - b[2]);
    const [r, c, value] = candidates[0];
    board[r][c] = value * 2;
    score += board[r][c];
    boostLeft -= 1;
    moves += 1;
    saved = false;
    render();
    flashIndex(r * boardSize + c);
    saveProgress();
    hintEl.textContent = `强化成功：${value} 升级为 ${value * 2}。`;
}

function useScanSkill() {
    if (scanLeft <= 0) return;
    clearTileMarks();
    const marked = new Set();
    for (let r = 0; r < boardSize; r += 1) {
        for (let c = 0; c < boardSize; c += 1) {
            const value = board[r][c];
            if (!value) continue;
            if (board[r]?.[c + 1] === value) {
                marked.add(r * boardSize + c);
                marked.add(r * boardSize + c + 1);
            }
            if (board[r + 1]?.[c] === value) {
                marked.add(r * boardSize + c);
                marked.add((r + 1) * boardSize + c);
            }
        }
    }
    scanLeft -= 1;
    renderSkillCounts();
    if (!marked.size) {
        hintEl.textContent = "扫描没有发现相邻可合成方块。";
        return;
    }
    marked.forEach((index) => boardEl.querySelector(`[data-index="${index}"]`)?.classList.add("scan-target"));
    hintEl.textContent = `扫描已标出 ${marked.size} 个可合成方块。`;
}

function useAutoSkill() {
    if (autoLeft <= 0) return;
    const ranked = ["left", "right", "up", "down"]
        .map((direction) => ({ direction, ...simulateMove(direction) }))
        .filter((item) => item.changed)
        .sort((a, b) => b.gain - a.gain || b.empty - a.empty);
    if (!ranked.length) {
        hintEl.textContent = "自动技能没有找到可移动方向。";
        return;
    }
    autoLeft -= 1;
    renderSkillCounts();
    move(ranked[0].direction);
    hintEl.textContent = `自动已选择${formatDirection(ranked[0].direction)}。`;
}

function flashIndex(index) {
    window.requestAnimationFrame(() => {
        const tile = boardEl.querySelector(`[data-index="${index}"]`);
        if (tile) tile.classList.add("skill-flash");
    });
}

function shuffleBoard() {
    if (shuffleLeft <= 0) {
        hintEl.textContent = "洗牌次数已经用完。";
        return;
    }
    pushHistory();
    const values = board.flat().filter(Boolean).sort(() => random() - 0.5);
    board = Array.from({ length: boardSize }, () => Array(boardSize).fill(0));
    values.forEach((value, index) => {
        board[Math.floor(index / boardSize)][index % boardSize] = value;
    });
    shuffleLeft -= 1;
    moves += 1;
    combo = 1;
    saved = false;
    render();
    saveProgress();
    hintEl.textContent = `已重新洗牌，还剩 ${shuffleLeft} 次。`;
}

function showHint() {
    const ranked = ["left", "right", "up", "down"]
        .map((direction) => ({ direction, ...simulateMove(direction) }))
        .filter((item) => item.changed)
        .sort((a, b) => b.gain - a.gain || b.empty - a.empty);
    if (!ranked.length) {
        hintEl.textContent = "当前没有可移动方向。";
        return;
    }
    const best = ranked[0];
    hintEl.textContent = `建议${formatDirection(best.direction)}。预计得分 +${best.gain}，移动后空格 ${best.empty} 个。`;
    boardEl.querySelectorAll(".tile").forEach((tile) => tile.classList.remove("hint-target"));
    const targetIndex = best.direction === "left" || best.direction === "up" ? 0 : boardSize * boardSize - 1;
    boardEl.querySelector(`[data-index="${targetIndex}"]`)?.classList.add("hint-target");
}

function analyzeBoard() {
    const empty = board.flat().filter((value) => value === 0).length;
    const mergeOptions = ["left", "right", "up", "down"].map(simulateMove).filter((item) => item.changed).length;
    const cornerMax = [board[0][0], board[0][3], board[3][0], board[3][3]].includes(maxTile());
    const snakeScore = board.flat().reduce((total, value, index) => total + value * (16 - index), 0);
    const risk = Math.max(0, 100 - empty * 12 - mergeOptions * 10 - (cornerMax ? 18 : 0));
    let advice = "局面比较稳定。尽量把最大方块固定在角落，慢慢合成。";
    if (risk >= 70) advice = "风险偏高。优先保留空格，不要把大方块拆散。";
    else if (!cornerMax) advice = "最大方块还不在角落，先把它推向一个角落再追分。";
    else if (mergeOptions <= 1) advice = "可走方向很少。锁死前可以考虑洗牌或撤销。";
    return { empty, mergeOptions, cornerMax, snakeScore, risk, advice };
}

function renderCoach() {
    const box = $("coachBox");
    const report = analyzeBoard();
    activateSideTab("stats");
    box.innerHTML = `
        <strong>普通分析：风险 ${report.risk}/100</strong>
        <span>空格：${report.empty} 个。可走方向：${report.mergeOptions} 个。最大方块在角落：${report.cornerMax ? "是" : "否"}。</span>
        <span>${report.advice}</span>
    `;
    hintEl.textContent = report.advice;
}

async function runVipProCoach() {
    if (!vipActive) {
        return;
    }
    activateSideTab("stats");
    const box = $("coachBox");
    box.innerHTML = "<strong>VIP 高级分析</strong><span>正在让服务器模拟上下左右四个方向...</span>";
    try {
        const response = await fetch("/vip/pro-coach", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ board, score, moves, boardSize, targetTile, mode })
        });
        const result = await response.json();
        if (!result.success) {
            const message = result.message || "VIP 高级分析暂时不可用。";
            box.innerHTML = `<strong>VIP 高级分析</strong><span>${escapeHtml(message)}</span>`;
            hintEl.textContent = message;
            return;
        }
        renderVipCoachReport(result.data);
    } catch (error) {
        box.innerHTML = "<strong>VIP 高级分析</strong><span>服务器分析失败。</span>";
        hintEl.textContent = "VIP 高级分析失败，请稍后再试。";
    }
}

function renderVipCoachReport(report) {
    const box = $("coachBox");
    if (!report) {
        box.innerHTML = "<strong>VIP 高级分析</strong><span>没有拿到分析结果。</span>";
        return;
    }
    const movesHtml = (report.moves || []).map((move) => `
        <article class="vip-coach-move ${move.legal ? "" : "disabled"}">
            <strong>${formatDirection(move.direction)}</strong>
            <span>预计得分 +${move.gain || 0} / 风险 ${move.riskAfter ?? "--"} / 空格 ${move.emptyCells ?? "--"}</span>
            <small>${escapeHtml(move.reason || "")}</small>
        </article>
    `).join("");
    const plansHtml = (report.plans || []).map((plan, index) => `
        <article class="vip-coach-plan">
            <strong>路线 ${index + 1}：${escapeHtml(plan.route || "暂无")}</strong>
            <span>累计预计 +${plan.totalGain || 0} / 末端风险 ${plan.finalRisk ?? "--"} / 末端空格 ${plan.finalEmptyCells ?? "--"}</span>
            <small>${escapeHtml(plan.reason || "")}</small>
        </article>
    `).join("");
    box.innerHTML = `
        <strong>VIP 高级分析：建议${formatDirection(report.bestDirection || "none")}</strong>
        <span>${escapeHtml(report.summary || "服务器分析完成。")}</span>
        <div class="vip-coach-metrics">
            <span>风险 <b>${report.risk ?? "--"}/100</b></span>
            <span>空格 <b>${report.emptyCells ?? "--"}</b></span>
            <span>可合成 <b>${report.mergePairs ?? "--"}</b></span>
            <span>最大方块 <b>${report.maxTile ?? "--"}</b></span>
            <span>在角落 <b>${report.maxTileInCorner ? "是" : "否"}</b></span>
        </div>
        <span>${escapeHtml(report.planSummary || "每走一步后都可以重新分析。")}</span>
        <div class="vip-coach-plans">${plansHtml || "<span>暂无可用路线。</span>"}</div>
        <div class="vip-coach-moves">${movesHtml}</div>
        <span>${escapeHtml(report.advice || "")}</span>
    `;
    hintEl.textContent = report.advice || "VIP 高级分析完成。";
}

async function loadVipGrowthReport() {
    if (!vipActive) {
        return;
    }
    activateSideTab("stats");
    const box = $("vipGrowthBox");
    if (!box) {
        return;
    }
    box.innerHTML = "<strong>VIP 成长报告</strong><span>正在读取你的历史成绩并生成训练计划...</span>";
    try {
        const response = await fetch("/vip/growth-report");
        const result = await response.json();
        if (!result.success) {
            const message = result.message || "VIP 成长报告暂时不可用。";
            box.innerHTML = `<strong>VIP 成长报告</strong><span>${escapeHtml(message)}</span>`;
            hintEl.textContent = message;
            return;
        }
        renderVipGrowthReport(result.data);
    } catch (error) {
        box.innerHTML = "<strong>VIP 成长报告</strong><span>成长报告生成失败。</span>";
        hintEl.textContent = "VIP 成长报告生成失败，请稍后再试。";
    }
}

function renderVipGrowthReport(report) {
    const box = $("vipGrowthBox");
    if (!box || !report) {
        return;
    }
    const trend = Number(report.trendPercent || 0);
    const trendText = `${trend > 0 ? "+" : ""}${trend}%`;
    const insightsHtml = (report.insights || []).map((item) => `<li>${escapeHtml(item)}</li>`).join("");
    const tasksHtml = (report.tasks || []).map((task) => {
        const progress = Math.max(0, Number(task.progress || 0));
        const target = Math.max(1, Number(task.target || 1));
        const percent = Math.min(100, Math.round(progress * 100 / target));
        return `
            <article class="growth-task ${task.completed ? "done" : ""}">
                <div>
                    <strong>${escapeHtml(task.title || "训练任务")}</strong>
                    <span>${escapeHtml(task.description || "")}</span>
                    <small>${escapeHtml(task.rewardHint || "")}</small>
                </div>
                <b>${progress}/${target}</b>
                <i style="--progress:${percent}%"></i>
            </article>
        `;
    }).join("");
    box.innerHTML = `
        <strong>${escapeHtml(report.levelName || "VIP 成长报告")}</strong>
        <span>${escapeHtml(report.headline || "")}</span>
        <div class="growth-metrics">
            <span>最高分 <b>${report.bestScore || 0}</b></span>
            <span>最大方块 <b>${report.bestTile || 2}</b></span>
            <span>近期均分 <b>${report.recentAverageScore || 0}</b></span>
            <span>趋势 <b class="${trend >= 0 ? "good" : "bad"}">${trendText}</b></span>
            <span>稳定分 <b>${report.consistencyScore || 0}/100</b></span>
            <span>效率分 <b>${report.efficiencyScore || 0}/100</b></span>
        </div>
        <div class="growth-target">
            <strong>下一步：${escapeHtml(report.nextAction || "")}</strong>
            <span>弱点判断：${escapeHtml(report.primaryWeakness || "暂无")}</span>
            <span>目标：${report.nextTargetScore || 1000} 分 / ${report.nextTargetTile || 512} 方块</span>
        </div>
        <ul class="growth-insights">${insightsHtml}</ul>
        <div class="growth-tasks">${tasksHtml || "<span>保存几局成绩后，这里会出现训练任务。</span>"}</div>
    `;
    hintEl.textContent = report.nextAction || "VIP 成长报告已生成。";
}

function formatDirection(direction) {
    return directionLabels[String(direction || "none").toLowerCase()] || String(direction || "无").toUpperCase();
}

function formatMode(value) {
    return modeLabels[String(value || "classic").toLowerCase()] || value || "经典";
}

function simulateMove(direction) {
    const originalBoard = board.map((row) => [...row]);
    const originalCombo = combo;
    let testBoard = originalBoard.map((row) => [...row]);
    let gain = 0;
    const mergeLine = (line) => {
        const values = line.filter(Boolean);
        const merged = [];
        for (let i = 0; i < values.length; i += 1) {
            if (values[i] === values[i + 1]) {
                const nextValue = values[i] * 2;
                gain += nextValue * originalCombo;
                merged.push(nextValue);
                i += 1;
            } else {
                merged.push(values[i]);
            }
        }
        while (merged.length < boardSize) merged.push(0);
        return merged;
    };
    if (direction === "left") testBoard = testBoard.map(mergeLine);
    if (direction === "right") testBoard = testBoard.map((line) => mergeLine([...line].reverse()).reverse());
    if (direction === "up") testBoard = transpose(transpose(testBoard).map(mergeLine));
    if (direction === "down") testBoard = transpose(transpose(testBoard).map((line) => mergeLine([...line].reverse()).reverse()));
    return {
        changed: JSON.stringify(testBoard) !== JSON.stringify(originalBoard),
        gain,
        empty: testBoard.flat().filter((value) => value === 0).length
    };
}

function transpose(grid) {
    return grid[0].map((_, index) => grid.map((row) => row[index]));
}

function isGameOver() {
    if (board.flat().includes(0)) return false;
    for (let r = 0; r < boardSize; r += 1) {
        for (let c = 0; c < boardSize; c += 1) {
            if (board[r][c] === board[r]?.[c + 1] || board[r][c] === board[r + 1]?.[c]) return false;
        }
    }
    return true;
}

function hasWon() {
    return board.flat().some((value) => value >= targetTile);
}

function maxTile() {
    return Math.max(...board.flat(), 2);
}

function elapsedSeconds() {
    return Math.round((Date.now() - startedAt) / 1000);
}

function formatTime(totalSeconds) {
    const minutes = String(Math.floor(totalSeconds / 60)).padStart(2, "0");
    const seconds = String(totalSeconds % 60).padStart(2, "0");
    return `${minutes}:${seconds}`;
}

function startTimer() {
    window.clearInterval(timer);
    timer = window.setInterval(() => {
        if (!paused) timeLabel.textContent = formatTime(elapsedSeconds());
    }, 1000);
}

function showOverlay(title, text, canResume = true) {
    overlayTitle.textContent = title;
    overlayText.textContent = text;
    $("resumeBtn").classList.toggle("hidden", !canResume);
    overlay.classList.remove("hidden");
}

function togglePause(forceState) {
    paused = typeof forceState === "boolean" ? forceState : !paused;
    if (paused) {
        showOverlay("已暂停", "休息一下，然后继续合成。", true);
        $("pauseBtn").textContent = "继续";
    } else {
        overlay.classList.add("hidden");
        startedAt = Date.now() - Number(timeLabel.textContent.split(":")[0]) * 60000 - Number(timeLabel.textContent.split(":")[1]) * 1000;
        $("pauseBtn").textContent = "暂停";
    }
}

async function saveRecord() {
    if (saved) {
        hintEl.textContent = "这局成绩已经保存过了。";
        return;
    }
    const tile = maxTile();
    const payload = {
        score,
        maxTile: tile,
        maxCharacter: getCharacter(tile).name,
        moveCount: moves,
        durationSeconds: elapsedSeconds(),
        boardState: JSON.stringify(board)
    };
    const response = await fetch("/game2048/record", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });
    const result = await response.json();
    saved = result.success;
    hintEl.textContent = result.message || "成绩已保存。";
    if (saved) {
        localStorage.removeItem(storageKey);
        loadServerStats();
    }
}

function renderMissions() {
    const html = missions.map((mission) => {
        const done = mission.done();
        return `<div class="${done ? "mission-card done" : "mission-card"}"><strong>${mission.title}</strong><span>${done ? "已完成" : mission.text}</span></div>`;
    }).join("");
    missionGrid.innerHTML = html;
    compactMissions.innerHTML = html;
}

function checkAchievements() {
    missions.forEach((mission) => {
        if (mission.done() && !achieved.has(mission.id)) {
            achieved.add(mission.id);
            showAchievement(mission.title, mission.text);
        }
    });
}

function showAchievement(title, text) {
    const toast = document.createElement("div");
    toast.className = "achievement-toast";
    toast.innerHTML = `<strong>任务完成：${title}</strong><span>${text}</span>`;
    document.body.appendChild(toast);
    window.setTimeout(() => toast.remove(), 2600);
}

function saveProgress() {
    localStorage.setItem(storageKey, JSON.stringify({
        board, score, moves, combo, shuffleLeft, mode, rngState, boardSize, targetTile,
        focusLeft, boostLeft, scanLeft, autoLeft,
        elapsedSeconds: elapsedSeconds(),
        achieved: [...achieved]
    }));
}

function currentRunData() {
    return {
        board, score, moves, combo, shuffleLeft, mode, rngState, boardSize, targetTile,
        focusLeft, boostLeft, scanLeft, autoLeft,
        elapsedSeconds: elapsedSeconds(),
        achieved: [...achieved]
    };
}

function cloudSavePayload(slotName) {
    return {
        slotName,
        runData: JSON.stringify(currentRunData()),
        score,
        maxTile: maxTile(),
        moveCount: moves,
        durationSeconds: elapsedSeconds(),
        mode,
        boardSize,
        targetTile
    };
}

function applyRunData(data) {
    board = data.board;
    score = data.score || 0;
    moves = data.moves || 0;
    combo = data.combo || 1;
    shuffleLeft = data.shuffleLeft ?? 2;
    boardSize = data.boardSize || data.board?.length || 4;
    targetTile = data.targetTile || 2048;
    focusLeft = data.focusLeft ?? 2;
    boostLeft = data.boostLeft ?? 3;
    scanLeft = data.scanLeft ?? 3;
    autoLeft = data.autoLeft ?? 5;
    mode = data.mode || "classic";
    rngState = data.rngState || Date.now();
    achieved = new Set(data.achieved || []);
    startedAt = Date.now() - (data.elapsedSeconds || 0) * 1000;
    modeSelect.value = mode;
    boardSizeSelect.value = String(boardSize);
    targetTileSelect.value = String(targetTile);
    render();
    saveProgress();
}

function exportRun() {
    dialogTitle.textContent = "导出本局";
    runDataBox.value = btoa(unescape(encodeURIComponent(JSON.stringify(currentRunData()))));
    runDialog.showModal();
}

function importRun() {
    dialogTitle.textContent = "导入本局";
    runDataBox.value = "";
    runDialog.showModal();
}

function applyImport() {
    try {
        applyRunData(JSON.parse(decodeURIComponent(escape(atob(runDataBox.value.trim())))));
        runDialog.close();
        hintEl.textContent = "本局数据已导入。";
    } catch (error) {
        hintEl.textContent = "导入失败，请检查数据是否正确。";
    }
}

function clearAutosave() {
    localStorage.removeItem(storageKey);
    hintEl.textContent = "自动存档已清除。";
}

async function saveVipCloudSlot() {
    if (!vipActive) {
        return;
    }
    const defaultName = `存档 ${new Date().toLocaleString()}`;
    const slotName = window.prompt("给这次云端存档起个名字：", defaultName);
    if (!slotName) {
        return;
    }
    try {
        const response = await fetch("/vip/cloud-saves", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(cloudSavePayload(slotName))
        });
        const result = await response.json();
        hintEl.textContent = result.message || "云端存档已保存。";
        if (result.success) {
            await loadVipCloudSaves(false);
        }
    } catch (error) {
        hintEl.textContent = "云端保存失败。";
    }
}

async function loadVipCloudSaves(openDialog = true) {
    if (!vipActive || !cloudSaveList) {
        return;
    }
    cloudSaveList.innerHTML = "<span class=\"muted\">加载中...</span>";
    if (openDialog) {
        cloudDialog.showModal();
    }
    try {
        const response = await fetch("/vip/cloud-saves");
        const result = await response.json();
        if (!result.success) {
            cloudSaveList.innerHTML = `<span class="muted">${result.message || "云端存档暂时不可用。"}</span>`;
            return;
        }
        renderVipCloudSaves(result.data || []);
    } catch (error) {
        cloudSaveList.innerHTML = "<span class=\"muted\">云端存档加载失败。</span>";
    }
}

function renderVipCloudSaves(saves) {
    if (!saves.length) {
        cloudSaveList.innerHTML = "<span class=\"muted\">还没有云端存档。</span>";
        return;
    }
    cloudSaveList.innerHTML = saves.map((save) => `
        <article class="cloud-save-item">
            <div>
                <strong>${escapeHtml(save.slotName)}</strong>
                <span>分数 ${save.score || 0} · 最大 ${save.maxTile || 2} · ${escapeHtml(formatMode(save.mode))} · ${save.boardSize || 4}x${save.boardSize || 4}</span>
                <small>${formatCloudTime(save.updateTime)}</small>
            </div>
            <button class="primary-btn" type="button" data-cloud-load="${save.id}">读取</button>
            <button class="secondary-btn" type="button" data-cloud-delete="${save.id}">删除</button>
        </article>
    `).join("");
}

async function loadVipCloudSave(id) {
    try {
        const response = await fetch(`/vip/cloud-saves/${id}`);
        const result = await response.json();
        if (!result.success || !result.data?.runData) {
            hintEl.textContent = result.message || "云端存档读取失败。";
            return;
        }
        applyRunData(JSON.parse(result.data.runData));
        cloudDialog.close();
        hintEl.textContent = `已读取云端存档：${result.data.slotName}。`;
    } catch (error) {
        hintEl.textContent = "云端存档读取失败。";
    }
}

async function deleteVipCloudSave(id) {
    if (!window.confirm("确定删除这个云端存档吗？")) {
        return;
    }
    try {
        const response = await fetch(`/vip/cloud-saves/${id}`, { method: "DELETE" });
        const result = await response.json();
        hintEl.textContent = result.message || "云端存档已删除。";
        await loadVipCloudSaves(false);
    } catch (error) {
        hintEl.textContent = "云端存档删除失败。";
    }
}

function formatCloudTime(value) {
    if (!value) {
        return "未知时间";
    }
    return String(value).replace("T", " ").slice(0, 16);
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&#039;");
}

async function claimVipGift() {
    if (!vipActive) {
        return;
    }
    if (vipGiftClaimed) {
        hintEl.textContent = "今天的 VIP 礼包已经领取过了。";
        refreshVipGiftButton();
        return;
    }
    const button = $("vipGiftBtn");
    if (button) button.disabled = true;
    try {
        const response = await fetch("/vip/daily-gift", { method: "POST" });
        const result = await response.json();
        if (!result.success) {
            hintEl.textContent = result.message || "VIP 礼包领取失败。";
            vipGiftClaimed = false;
            refreshVipGiftButton();
            return;
        }
        if (result.data?.alreadyClaimed) {
            vipGiftClaimed = true;
            hintEl.textContent = result.message || "今天的 VIP 礼包已经领取过了。";
            refreshVipGiftButton();
            return;
        }
        score += result.data?.scoreBonus || 0;
        boostLeft += result.data?.boostBonus || 0;
        scanLeft += result.data?.scanBonus || 0;
        saved = false;
        vipGiftClaimed = true;
        render();
        saveProgress();
        hintEl.textContent = `${result.message}: +${result.data?.scoreBonus || 0} 分，+${result.data?.boostBonus || 0} 强化，+${result.data?.scanBonus || 0} 扫描。`;
    } catch (error) {
        vipGiftClaimed = false;
        refreshVipGiftButton();
        hintEl.textContent = "VIP 礼包领取失败。";
    }
}

function saveSlot() {
    const name = window.prompt("本地存档名称：", "存档-1");
    if (!name) return;
    const slots = JSON.parse(localStorage.getItem(slotKey) || "{}");
    slots[name] = currentRunData();
    localStorage.setItem(slotKey, JSON.stringify(slots));
    hintEl.textContent = `已保存本地存档：${name}。`;
}

function loadSlot() {
    const slots = JSON.parse(localStorage.getItem(slotKey) || "{}");
    const names = Object.keys(slots);
    if (!names.length) {
        hintEl.textContent = "还没有本地存档。";
        return;
    }
    const name = window.prompt(`选择要读取的本地存档：${names.join(", ")}`, names[0]);
    if (!name || !slots[name]) return;
    applyRunData(slots[name]);
    hintEl.textContent = `已读取本地存档：${name}。`;
}

async function loadBestRecord() {
    try {
        const response = await fetch("/game2048/best-record");
        const result = await response.json();
        if (!result.success || !result.data || !result.data.boardState) {
            hintEl.textContent = "还没有保存过最佳棋盘。";
            return;
        }
        board = JSON.parse(result.data.boardState);
        boardSize = board.length || 4;
        targetTile = Math.max(2048, result.data.maxTile || 2048);
        score = result.data.score || 0;
        moves = result.data.moveCount || 0;
        combo = 1;
        shuffleLeft = 1;
        mode = "zen";
        modeSelect.value = mode;
        boardSizeSelect.value = String(boardSize);
        targetTileSelect.value = String([1024, 2048, 4096].includes(targetTile) ? targetTile : 2048);
        startedAt = Date.now() - (result.data.durationSeconds || 0) * 1000;
        achieved = new Set();
        saved = false;
        render();
        saveProgress();
        hintEl.textContent = "已在练习模式读取你的最佳棋盘。";
    } catch (error) {
        hintEl.textContent = "最佳棋盘读取失败。";
    }
}

function loadSettings() {
    try {
        const settings = JSON.parse(localStorage.getItem(settingsKey) || "{}");
        const preferredTheme = settings.theme || (vipActive ? "vip" : "midnight");
        themeSelect.value = preferredTheme === "vip" && !vipActive ? "midnight" : preferredTheme;
        soundToggle.checked = settings.soundEnabled !== false;
        volumeSlider.value = settings.volume ?? 35;
        soundEnabled = soundToggle.checked;
        applyTheme(themeSelect.value);
    } catch (error) {
        applyTheme("midnight");
    }
}

function saveSettings() {
    localStorage.setItem(settingsKey, JSON.stringify({
        theme: themeSelect.value,
        soundEnabled: soundToggle.checked,
        volume: volumeSlider.value
    }));
}

function applyTheme(theme) {
    document.body.dataset.theme = theme;
}

function setupTabs() {
    document.querySelectorAll(".side-tabs button").forEach((button) => {
        button.addEventListener("click", () => {
            activateSideTab(button.dataset.tab);
        });
    });
}

function activateSideTab(tab) {
    document.querySelectorAll(".side-tabs button").forEach((button) => {
        button.classList.toggle("active", button.dataset.tab === tab);
    });
    document.querySelectorAll(".tab-content").forEach((panel) => {
        panel.classList.toggle("active", panel.dataset.panel === tab);
    });
}

function ensureAudio() {
    if (!audioContext) audioContext = new AudioContext();
}

function playBeep(frequency, duration) {
    if (!soundEnabled) return;
    ensureAudio();
    const oscillator = audioContext.createOscillator();
    const gain = audioContext.createGain();
    oscillator.frequency.value = frequency;
    gain.gain.value = Number(volumeSlider.value) / 900;
    oscillator.connect(gain);
    gain.connect(audioContext.destination);
    oscillator.start();
    oscillator.stop(audioContext.currentTime + duration);
}

function toggleMusic() {
    if (musicTimer) {
        window.clearInterval(musicTimer);
        musicTimer = null;
        $("playMusicBtn").textContent = "播放";
        return;
    }
    ensureAudio();
    $("playMusicBtn").textContent = "停止";
    const tick = () => {
        const track = tracks[currentTrack];
        playBeep(track.base, 0.08);
        window.setTimeout(() => playBeep(track.accent, 0.06), 220);
    };
    tick();
    musicTimer = window.setInterval(tick, 760);
}

function setTrack(delta) {
    currentTrack = (currentTrack + delta + tracks.length) % tracks.length;
    trackTitle.textContent = tracks[currentTrack].title;
    trackMood.textContent = tracks[currentTrack].mood;
}

document.addEventListener("keydown", (event) => {
    const keys = { ArrowLeft: "left", a: "left", A: "left", ArrowRight: "right", d: "right", D: "right", ArrowUp: "up", w: "up", W: "up", ArrowDown: "down", s: "down", S: "down" };
    if (keys[event.key]) {
        event.preventDefault();
        move(keys[event.key]);
    }
    if (event.ctrlKey && event.key.toLowerCase() === "z") {
        event.preventDefault();
        undoMove();
    }
    if (event.code === "Space") {
        event.preventDefault();
        togglePause();
    }
});

boardEl.addEventListener("touchstart", (event) => {
    const touch = event.changedTouches[0];
    touchStart = { x: touch.clientX, y: touch.clientY };
});

boardEl.addEventListener("touchend", (event) => {
    if (!touchStart) return;
    const touch = event.changedTouches[0];
    const dx = touch.clientX - touchStart.x;
    const dy = touch.clientY - touchStart.y;
    if (Math.max(Math.abs(dx), Math.abs(dy)) < 28) return;
    move(Math.abs(dx) > Math.abs(dy) ? (dx > 0 ? "right" : "left") : (dy > 0 ? "down" : "up"));
});

$("saveBtn").addEventListener("click", saveRecord);
$("restartBtn").addEventListener("click", startGame);
$("overlayRestartBtn").addEventListener("click", startGame);
$("resumeBtn").addEventListener("click", () => togglePause(false));
$("pauseBtn").addEventListener("click", () => togglePause());
$("undoBtn").addEventListener("click", undoMove);
$("hintBtn").addEventListener("click", showHint);
$("shuffleBtn").addEventListener("click", shuffleBoard);
$("exportBtn").addEventListener("click", exportRun);
$("importBtn").addEventListener("click", importRun);
$("applyImportBtn").addEventListener("click", applyImport);
$("slotSaveBtn").addEventListener("click", saveSlot);
$("slotLoadBtn").addEventListener("click", loadSlot);
$("loadBestBtn").addEventListener("click", loadBestRecord);
$("coachBtn").addEventListener("click", renderCoach);
$("vipProCoachBtn")?.addEventListener("click", runVipProCoach);
$("vipGrowthBtn")?.addEventListener("click", loadVipGrowthReport);
$("vipGiftBtn")?.addEventListener("click", claimVipGift);
$("vipCloudSaveBtn")?.addEventListener("click", saveVipCloudSlot);
$("vipCloudLoadBtn")?.addEventListener("click", () => loadVipCloudSaves(true));
$("focusSkillBtn").addEventListener("click", useFocusSkill);
$("boostSkillBtn").addEventListener("click", useBoostSkill);
$("scanSkillBtn").addEventListener("click", useScanSkill);
$("autoSkillBtn").addEventListener("click", useAutoSkill);
$("clearCacheBtn").addEventListener("click", clearAutosave);
$("playMusicBtn").addEventListener("click", toggleMusic);
$("prevTrackBtn").addEventListener("click", () => setTrack(-1));
$("nextTrackBtn").addEventListener("click", () => setTrack(1));
modeSelect.addEventListener("change", () => {
    mode = modeSelect.value;
    localStorage.removeItem(storageKey);
    startGame();
    hintEl.textContent = mode === "daily" ? `每日挑战种子 ${dailySeed}。今天所有人都是同一套棋盘。` : `已切换到${formatMode(mode)}模式。`;
});
boardSizeSelect.addEventListener("change", () => {
    localStorage.removeItem(storageKey);
    startGame();
    hintEl.textContent = `棋盘已切换为 ${boardSize} x ${boardSize}。`;
});
targetTileSelect.addEventListener("change", () => {
    targetTile = Number(targetTileSelect.value || 2048);
    saveProgress();
    render();
    hintEl.textContent = `目标已切换为 ${targetTile}。`;
});
themeSelect.addEventListener("change", () => {
    applyTheme(themeSelect.value);
    saveSettings();
});
soundToggle.addEventListener("change", () => {
    soundEnabled = soundToggle.checked;
    saveSettings();
});
volumeSlider.addEventListener("input", saveSettings);

cloudSaveList?.addEventListener("click", (event) => {
    const loadId = event.target.dataset.cloudLoad;
    const deleteId = event.target.dataset.cloudDelete;
    if (loadId) {
        loadVipCloudSave(loadId);
    }
    if (deleteId) {
        deleteVipCloudSave(deleteId);
    }
});

setupTabs();
setTrack(0);
continueGame();
loadServerStats();
loadVipGiftStatus();
