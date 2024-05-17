package com.webapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class PasswordResetController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    private Map<String, String> otpStorage = new HashMap<>();

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) throws IOException {
        User user = userService.getUserAttibutesAfterLogIn(email);
        if (user == null) {
            model.addAttribute("message", "Invalid email address!");
            return "forgot_password";
        }

        // Generate OTP
        String otp = generateOTP();
        otpStorage.put(email, otp);

        // Send OTP to email
        emailService.sendEmail(email, "Password Reset OTP", "Your OTP for password reset is: " + otp);

        model.addAttribute("message", "OTP sent to your email!");
        return "password_reset_form";
    }

    @GetMapping("/verify-otp")
    public String showVerifyOtpForm() {
        return "password_reset_form";
    }

    @PostMapping("/verify-otp")
    public String processVerifyOtp(@RequestParam("email") String email,
                                   @RequestParam("otp") String otp,
                                   @RequestParam("password") String password,
                                   Model model) {
        String storedOtp = otpStorage.get(email);

        if (storedOtp != null && storedOtp.equals(otp)) {
            // OTP is correct, update the password
            User user = userService.getUserAttibutesAfterLogIn(email);
            if (user != null) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(password);
                userService.save(user);
                model.addAttribute("message", "Password updated successfully!");
                otpStorage.remove(email);
                return "login"; // Redirect to login page after successful reset
            }
        }

        model.addAttribute("message", "Invalid OTP!");
        return "password_reset_form";
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
