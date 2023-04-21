package com.example.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.shop.models.Person;
import com.example.shop.services.PersonService;
import com.example.shop.util.PersonValidator;

import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
    
    private final PersonValidator personValidator;

    private final PersonService personService;

    public AuthenticationController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }

    @GetMapping("/authentication")
    public String login(Model model) {
        model.addAttribute("title", "Авторизация");
        return "home/authentication";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person, Model model) {
        model.addAttribute("title", "Регистрация");
        return "home/registration";
    }

    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        
        if(bindingResult.hasErrors()){
            return "home/registration";
        }
        
        personService.register(person);
        return "redirect:/authentication";
    }
}
