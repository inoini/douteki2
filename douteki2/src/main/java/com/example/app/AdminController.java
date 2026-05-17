package com.example.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }
}
