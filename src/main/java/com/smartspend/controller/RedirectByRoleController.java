package com.smartspend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectByRoleController {

    @GetMapping("/redirect-by-role")
    public String redirectByRole(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        for (GrantedAuthority a : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(a.getAuthority())) {
                return "redirect:/admin/dashboard";
            }
        }
        return "redirect:/dashboard";
    }
}
