package com.example.shop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.enumm.UserRole;
import com.example.shop.models.Person;
import com.example.shop.services.PersonService;

import jakarta.validation.Valid;

@Controller
public class AdminUserController {
    

    private PersonService personService;

    public AdminUserController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/admin/user")
    public String index(Model model) {
        model.addAttribute("userList", personService.getAllUser());
        model.addAttribute("title", "Список пользователей");
        System.out.println(com.example.shop.enumm.UserRole.values());
        return "admin/user/list";
    }

    @GetMapping("/admin/user/details/{id}")
    public String details(@PathVariable("id") int id, Model model) {
        Person personDetails = personService.getPersonId(id);
        model.addAttribute("person", personDetails);
        model.addAttribute("title", "Пользователь: " + personDetails.getFullName());
        return "admin/user/details";
    }

    @GetMapping("/admin/user/edit/{id}")
    public String editProduct(@PathVariable("id") int id, Model model){
        Person personEdit = personService.getPersonId(id);
        model.addAttribute("edit_person", personEdit);
        model.addAttribute("title", "Редактировать пользователя: " + personEdit.getFullName());
        return "admin/user/edit";
    }

    @PostMapping("/admin/user/edit/{id}")
    public String edit_Product(@RequestParam("role") String role, @PathVariable("id") int id, Model model){
        Person person = personService.getPersonId(id);

        if (role.isEmpty()) {
            model.addAttribute("edit_person", person);
            model.addAttribute("title", "Редактировать пользователя: " + person.getFullName());
            model.addAttribute("error", "Роль не может быть пустой");
            System.out.println(role);
            return "admin/user/edit";
        }

        person.setRole(role);
        personService.personEdit(id, person);
        model.addAttribute("person", person);
        model.addAttribute("title", "Пользователь: " + person.getFullName());
        return "admin/user/details";
    }
}
