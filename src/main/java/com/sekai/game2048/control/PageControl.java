package com.sekai.game2048.control;

import com.sekai.game2048.model.User;
import com.sekai.game2048.service.Game2048Service;
import com.sekai.game2048.service.MonetizationService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageControl {

    @Resource
    private Game2048Service game2048Service;

    @Resource
    private MonetizationService monetizationService;

    @Value("${sekai.ads.enabled:false}")
    private boolean adsEnabled;

    @Value("${sekai.ads.client:}")
    private String adsClient;

    @Value("${sekai.ads.slot:}")
    private String adsSlot;

    @GetMapping("/")
    public String index(HttpSession session) {
        return loginUser(session) == null ? "redirect:/login" : "redirect:/home";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        return loginUser(session) == null ? "login" : "redirect:/home";
    }

    @GetMapping("/register")
    public String register(HttpSession session) {
        return loginUser(session) == null ? "register" : "redirect:/home";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = loginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("loginUser", user);
        model.addAttribute("dashboard", game2048Service.buildDashboard(user.getId()));
        addMonetization(model);
        return "home";
    }

    @GetMapping("/leaderboard")
    public String leaderboard(HttpSession session, Model model) {
        User user = loginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("loginUser", user);
        model.addAttribute("leaderboard", game2048Service.listLeaderboard());
        addMonetization(model);
        return "leaderboard";
    }

    @GetMapping("/store")
    public String store(HttpSession session, Model model) {
        User user = loginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("loginUser", user);
        model.addAttribute("monetization", monetizationService.buildDashboard(user.getId()));
        addMonetization(model);
        return "store";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    private User loginUser(HttpSession session) {
        Object value = session.getAttribute("loginUser");
        return value instanceof User user ? user : null;
    }

    private void addMonetization(Model model) {
        boolean hasAdConfig = adsEnabled && !adsClient.isBlank() && !adsSlot.isBlank();
        model.addAttribute("adsEnabled", hasAdConfig);
        model.addAttribute("adsClient", adsClient);
        model.addAttribute("adsSlot", adsSlot);
    }
}
