# Sekai 2048

`memory-6` 已改成和 `memory-three` 同类文件夹结构的 Spring Boot + MyBatis + Thymeleaf 项目。

这是一个原创角色版 2048 小游戏：每个数字格子对应一个原创角色，合成时会升级成更高阶角色，并保存分数排行榜。

## 商业化准备

当前版本已经把真实动漫角色替换为原创角色设定，并加入：

- 隐私政策：`/privacy`
- 服务条款：`/terms`
- 可配置广告位：通过环境变量开启
- 商店页：`/store`
- 会员、角色包、赞助位的购买/合作意向收集
- 商业化线索后台：`/admin/monetization/leads?token=你的后台token`
- 数据库配置环境变量化，避免生产密码写死在仓库里

上线前还需要补齐真实运营主体、联系邮箱、域名、HTTPS、备份、监控、强随机后台 token，以及广告平台审核需要的站点信息。

## 数据库

数据库：`sekai_friend`

表前缀：`sekai_2048_`

- `sekai_2048_user`
- `sekai_2048_game_record`
- `sekai_2048_monetization_lead`
- `sekai_2048_vip_daily_gift`
- `sekai_2048_vip_cloud_save`

初始化脚本：`build-data/init.sql`

给现有库追加 VIP 字段并给 `sekai` 账号开通一年 VIP：

```powershell
Get-Content -Encoding UTF8 -Raw build-data/upgrade-vip.sql | mysql -uroot -p123456
```

当前 VIP 权益：

- 首页和商店页展示 VIP 徽章
- 商店 Plus 月卡展示“已开通 / 已拥有”
- 游戏页免广告展示
- VIP Gold 专属主题
- 每局额外技能次数：Focus +1、Boost +2、Scan +2、Auto +3
- 服务端每日礼包：+256 分、+1 Boost、+1 Scan，每个 VIP 账号每天只能领取一次
- VIP 云端存档：最多 8 个服务器槽位，支持跨浏览器保存、加载、删除当前局
- VIP Pro Coach：服务端模拟四个方向和三步路线，输出最佳走法、风险评分、空格数和可合并机会
- VIP 成长报告：根据历史成绩生成趋势、弱点、下一目标和训练任务
- 排行榜展示 VIP 标识

## 启动

```powershell
mvn spring-boot:run
```

访问：

```text
http://localhost:8086/login
```

## 环境变量

本地不配置时会使用默认值；生产环境建议全部显式配置。

```powershell
$env:DB_URL="jdbc:mysql://127.0.0.1:3306/sekai_friend?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true"
$env:DB_USERNAME="sekai_app"
$env:DB_PASSWORD="请替换为强密码"
$env:SERVER_PORT="8086"
$env:SEKAI_COOKIE_SECURE="true"
$env:THYMELEAF_CACHE="true"
$env:SEKAI_ADMIN_TOKEN="请替换为强随机后台token"
```

广告位默认关闭。拿到广告平台的 client 和 slot 后再开启：

```powershell
$env:SEKAI_ADS_ENABLED="true"
$env:SEKAI_ADS_CLIENT="ca-pub-xxxxxxxxxxxxxxxx"
$env:SEKAI_ADS_SLOT="1234567890"
```

如果已经接好支付或收款页面，可以把商店里的按钮指向真实链接：

```powershell
$env:SEKAI_PLUS_CHECKOUT_URL="https://example.com/pay/plus"
$env:SEKAI_PACK_CHECKOUT_URL="https://example.com/pay/character-pack"
$env:SEKAI_SPONSOR_CHECKOUT_URL="https://example.com/sponsor"
```
