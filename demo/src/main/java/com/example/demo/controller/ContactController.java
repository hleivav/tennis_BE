package com.example.demo.controller;

import com.example.demo.dto.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "https://tennis-app-one.vercel.app"})
public class ContactController {

    //@Autowired
    //private JavaMailSender mailSender;

    /*@PostMapping("/contact")
    public ResponseEntity<String> sendContactEmail(@RequestBody ContactRequest request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("facetedjungle@gmail.com");
            message.setSubject("Tennis System - Nytt meddelande från " + request.getName());
            message.setText(
                    "Namn: " + request.getName() + "\n" +
                            "E-post: " + request.getEmail() + "\n\n" +
                            "Meddelande:\n" + request.getMessage()
            );
            message.setFrom("facetedjungle@gmail.com");
            message.setReplyTo(request.getEmail());

            mailSender.send(message);
            return ResponseEntity.ok("Meddelande skickat!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Kunde inte skicka meddelande: " + e.getMessage());
        }
    }*/
}