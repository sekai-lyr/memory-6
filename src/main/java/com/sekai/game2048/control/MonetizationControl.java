package com.sekai.game2048.control;

import com.sekai.game2048.model.MonetizationLead;
import com.sekai.game2048.model.MonetizationLeadRequest;
import com.sekai.game2048.model.Result;
import com.sekai.game2048.model.User;
import com.sekai.game2048.service.MonetizationService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MonetizationControl {

    @Resource
    private MonetizationService monetizationService;

    @Value("${sekai.admin.token:}")
    private String adminToken;

    @GetMapping("/admin/monetization/leads")
    public String leads(@RequestParam(value = "token", required = false) String token, Model model) {
        if (adminToken == null || adminToken.isBlank() || !adminToken.equals(token)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("leads", monetizationService.listRecentLeads(100));
        return "admin-leads";
    }

    @PostMapping("/monetization/lead")
    public String createLead(@ModelAttribute MonetizationLeadRequest request,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User user = loginUser(session);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "请先登录后再提交");
            return "redirect:/login";
        }

        Result<MonetizationLead> result = monetizationService.createLead(user, request);
        redirectAttributes.addFlashAttribute(result.isSuccess() ? "message" : "error", result.getMessage());
        return "redirect:/store";
    }

    private User loginUser(HttpSession session) {
        Object value = session.getAttribute("loginUser");
        return value instanceof User user ? user : null;
    }
}
