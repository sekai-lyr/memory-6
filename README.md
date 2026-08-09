# 🎮 Sekai 2048 · 原创角色合成游戏

> **原创角色 2048：合成升级 · 排行榜 · VIP 订阅 · AI 教练**
> Original-character 2048: merge, leaderboard, VIP subscriptions & AI coach

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![MyBatis](https://img.shields.io/badge/MyBatis-3-lightgrey)](https://mybatis.org/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3-005F0F)](https://www.thymeleaf.org/)
[![VIP](https://img.shields.io/badge/VIP-订阅系统-gold)](#)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

原创角色版 2048：每个数字格子对应一个原创角色，合成时升级为更高阶角色，支持分数排行榜与**完整 VIP 订阅体系**。

An original-character 2048 game with merging upgrades, leaderboards, and a full VIP subscription system.

## 🎮 玩法特色 Gameplay

- 原创角色设定（已替换真实动漫角色，规避版权风险）Original characters
- 合成升级：低阶角色 → 高阶角色 Merge & upgrade
- 分数排行榜 Leaderboard
- 每局技能：Focus / Boost / Scan / Auto In-game skills

## 👑 VIP 权益 VIP Benefits

- 首页和商店页 VIP 徽章展示
- 商店 Plus 月卡"已开通 / 已拥有"
- 游戏页免广告 Ad-free
- VIP Gold 专属主题 Gold theme
- 每局额外技能次数：Focus +1、Boost +2、Scan +2、Auto +3
- 服务端每日礼包：+256 分数、1 Boost、1 Scan Daily gift
- **VIP 云端存档**：最多 8 个服务器槽位 Cloud save (8 slots)
- **VIP Pro Coach**：服务端模拟四方向三步路线，输出最佳走法、风险评分、空格数、可合并机会 AI coach
- **VIP 成长报告**：历史成绩趋势、弱点、下一目标与训练任务 Growth report

## 🛒 商业化准备 Monetization

- 隐私政策 `/privacy`、服务条款 `/terms`
- 可配置广告位（环境变量开关）
- 商店页 `/store`：会员、角色包、赞助位
- 商业化线索后台 `/admin/monetization/leads?token=你的后台token`
- 数据库配置环境变量化

## 🗄️ 数据库 Database

数据库 `sekai_friend`，表前缀 `sekai_2048_`（user / game_record / monetization_lead / vip_daily_gift / vip_cloud_save）。

初始化脚本：`build-data/init.sql`；为现有库追加 VIP 字段并给 `sekai` 账号开通一年 VIP：

```powershell
Get-Content -Encoding UTF8 -Raw build-data/upgrade-vip.sql | mysql -uroot -p123456
```

## ▶️ 启动 Run

```powershell
mvn spring-boot:run
```

访问 `http://localhost:8086/login`，默认端口 `8086`。

## ⚙️ 环境变量 Environment

```powershell
$env:DB_URL="jdbc:mysql://127.0.0.1:3306/sekai_friend?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true"
$env:DB_USERNAME="sekai_app"
$env:DB_PASSWORD="请替换为强密码"
$env:SERVER_PORT="8086"
$env:SEKAI_COOKIE_SECURE="true"
$env:THYMELEAF_CACHE="true"
$env:SEKAI_ADMIN_TOKEN="请替换为强随机后台token"
```

广告位（默认关闭）：

```powershell
$env:SEKAI_ADS_ENABLED="true"
$env:SEKAI_ADS_CLIENT="ca-pub-xxxxxxxxxxxxxxxx"
$env:SEKAI_ADS_SLOT="1234567890"
```

支付链接（接好支付后配置）：

```powershell
$env:SEKAI_PLUS_CHECKOUT_URL="https://example.com/pay/plus"
$env:SEKAI_PACK_CHECKOUT_URL="https://example.com/pay/character-pack"
$env:SEKAI_SPONSOR_CHECKOUT_URL="https://example.com/sponsor"
```

## 📄 License

[MIT](LICENSE) © 2026 [sekai-lyr](https://github.com/sekai-lyr)

---

**⭐ If this project helped you, star it! 如果这个项目对你有帮助，欢迎 Star！**
