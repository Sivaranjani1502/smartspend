package com.smartspend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AfterLoginController {

    @GetMapping("/afterLogin")
    public String afterLogin(Authentication auth) {

        String role = auth.getAuthorities()
                          .iterator()
                          .next()
                          .getAuthority();

        if (role.equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/dashboard"; // normal user
    }
}
