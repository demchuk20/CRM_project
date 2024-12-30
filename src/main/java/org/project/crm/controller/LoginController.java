package org.project.crm.controller;

import lombok.RequiredArgsConstructor;
import org.project.crm.entity.Audit;
import org.project.crm.model.NewUser;
import org.project.crm.service.UserDetailsServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class LoginController {
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping
    public String register(@ModelAttribute @Valid NewUser newUser) {
        var user = userDetailsService.findByUsername(newUser.getUsername());
        if (user.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (!newUser.getPassword().equals(newUser.getPasswordRepeated())) {
            throw new RuntimeException("Passwords do not match");
        }
        Audit audit = Audit.builder()
                .username(newUser.getUsername())
                .password(newUser.getPassword()).build();
        userDetailsService.save(audit);
        return "redirect:login";
    }

    @GetMapping
    public String getRegisterPage(Model model) {
        NewUser newUser = new NewUser();
        model.addAttribute("newUser", newUser);
        return "register";
    }
}
