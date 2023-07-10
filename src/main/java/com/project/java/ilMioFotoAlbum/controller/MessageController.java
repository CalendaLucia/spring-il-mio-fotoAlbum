package com.project.java.ilMioFotoAlbum.controller;

import com.project.java.ilMioFotoAlbum.model.ContactMessage;
import com.project.java.ilMioFotoAlbum.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    ContactMessageRepository contactMessageRepository;

    @GetMapping
    public String getMessage(Model model) {
        List<ContactMessage> contactMessages = contactMessageRepository.findAll();
        int messageCount = contactMessages.size();
        model.addAttribute("contactMessages", contactMessages);
        model.addAttribute("messageCount", messageCount);
        return "messages/contact-messages";
    }
}
