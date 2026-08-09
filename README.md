# 馃幃 Sekai 2048 路 鍘熷垱瑙掕壊鍚堟垚娓告垙

> **鍘熷垱瑙掕壊 2048锛氬悎鎴愬崌绾?路 鎺掕姒?路 VIP 璁㈤槄 路 AI 鏁欑粌**
> Original-character 2048: merge, leaderboard, VIP subscriptions & AI coach

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![MyBatis](https://img.shields.io/badge/MyBatis-3-lightgrey)](https://mybatis.org/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3-005F0F)](https://www.thymeleaf.org/)
[![VIP](https://img.shields.io/badge/VIP-璁㈤槄绯荤粺-gold)](#)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

鍘熷垱瑙掕壊鐗?2048锛氭瘡涓暟瀛楁牸瀛愬搴斾竴涓師鍒涜鑹诧紝鍚堟垚鏃跺崌绾т负鏇撮珮闃惰鑹诧紝鏀寔鍒嗘暟鎺掕姒滀笌**瀹屾暣 VIP 璁㈤槄浣撶郴**銆?
An original-character 2048 game with merging upgrades, leaderboards, and a full VIP subscription system.

## 馃幃 鐜╂硶鐗硅壊 Gameplay

- 鍘熷垱瑙掕壊璁惧畾锛堝凡鏇挎崲鐪熷疄鍔ㄦ极瑙掕壊锛岃閬跨増鏉冮闄╋級Original characters
- 鍚堟垚鍗囩骇锛氫綆闃惰鑹?鈫?楂橀樁瑙掕壊 Merge & upgrade
- 鍒嗘暟鎺掕姒?Leaderboard
- 姣忓眬鎶€鑳斤細Focus / Boost / Scan / Auto In-game skills

## 馃憫 VIP 鏉冪泭 VIP Benefits

- 棣栭〉鍜屽晢搴楅〉 VIP 寰界珷灞曠ず
- 鍟嗗簵 Plus 鏈堝崱"宸插紑閫?/ 宸叉嫢鏈?
- 娓告垙椤靛厤骞垮憡 Ad-free
- VIP Gold 涓撳睘涓婚 Gold theme
- 姣忓眬棰濆鎶€鑳芥鏁帮細Focus +1銆丅oost +2銆丼can +2銆丄uto +3
- 鏈嶅姟绔瘡鏃ョぜ鍖咃細+256 鍒嗘暟銆? Boost銆? Scan Daily gift
- **VIP 浜戠瀛樻。**锛氭渶澶?8 涓湇鍔″櫒妲戒綅 Cloud save (8 slots)
- **VIP Pro Coach**锛氭湇鍔＄妯℃嫙鍥涙柟鍚戜笁姝ヨ矾绾匡紝杈撳嚭鏈€浣宠蛋娉曘€侀闄╄瘎鍒嗐€佺┖鏍兼暟銆佸彲鍚堝苟鏈轰細 AI coach
- **VIP 鎴愰暱鎶ュ憡**锛氬巻鍙叉垚缁╄秼鍔裤€佸急鐐广€佷笅涓€鐩爣涓庤缁冧换鍔?Growth report

## 馃洅 鍟嗕笟鍖栧噯澶?Monetization

- 闅愮鏀跨瓥 `/privacy`銆佹湇鍔℃潯娆?`/terms`
- 鍙厤缃箍鍛婁綅锛堢幆澧冨彉閲忓紑鍏筹級
- 鍟嗗簵椤?`/store`锛氫細鍛樸€佽鑹插寘銆佽禐鍔╀綅
- 鍟嗕笟鍖栫嚎绱㈠悗鍙?`/admin/monetization/leads?token=浣犵殑鍚庡彴token`
- 鏁版嵁搴撻厤缃幆澧冨彉閲忓寲

## 馃梽锔?鏁版嵁搴?Database

鏁版嵁搴?`sekai_friend`锛岃〃鍓嶇紑 `sekai_2048_`锛坲ser / game_record / monetization_lead / vip_daily_gift / vip_cloud_save锛夈€?
鍒濆鍖栬剼鏈細`build-data/init.sql`锛涗负鐜版湁搴撹拷鍔?VIP 瀛楁骞剁粰 `sekai` 璐﹀彿寮€閫氫竴骞?VIP锛?
```powershell
Get-Content -Encoding UTF8 -Raw build-data/upgrade-vip.sql | mysql -uroot -p123456
```

## 鈻讹笍 鍚姩 Run

```powershell
mvn spring-boot:run
```

璁块棶 `http://localhost:8086/login`锛岄粯璁ょ鍙?`8086`銆?
## 鈿欙笍 鐜鍙橀噺 Environment

```powershell
$env:DB_URL="jdbc:mysql://127.0.0.1:3306/sekai_friend?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true"
$env:DB_USERNAME="sekai_app"
$env:DB_PASSWORD="璇锋浛鎹负寮哄瘑鐮?
$env:SERVER_PORT="8086"
$env:SEKAI_COOKIE_SECURE="true"
$env:THYMELEAF_CACHE="true"
$env:SEKAI_ADMIN_TOKEN="璇锋浛鎹负寮洪殢鏈哄悗鍙皌oken"
```

骞垮憡浣嶏紙榛樿鍏抽棴锛夛細

```powershell
$env:SEKAI_ADS_ENABLED="true"
$env:SEKAI_ADS_CLIENT="ca-pub-xxxxxxxxxxxxxxxx"
$env:SEKAI_ADS_SLOT="1234567890"
```

鏀粯閾炬帴锛堟帴濂芥敮浠樺悗閰嶇疆锛夛細

```powershell
$env:SEKAI_PLUS_CHECKOUT_URL="https://example.com/pay/plus"
$env:SEKAI_PACK_CHECKOUT_URL="https://example.com/pay/character-pack"
$env:SEKAI_SPONSOR_CHECKOUT_URL="https://example.com/sponsor"
```

## 馃搫 License

[MIT](LICENSE) 漏 2026 [sekai-lyr](https://github.com/sekai-lyr)


<p align="center">
  <img src="screenshots/demo.webp" alt="Demo" width="720"/>
</p>
---

**猸?If this project helped you, star it! 濡傛灉杩欎釜椤圭洰瀵逛綘鏈夊府鍔╋紝娆㈣繋 Star锛?*

