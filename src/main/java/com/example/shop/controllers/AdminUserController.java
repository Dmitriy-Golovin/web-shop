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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
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
        return "admin/user/list";
    }

    @GetMapping("/admin/user/details/{id}")
    public String details(@PathVariable("id") int id, Model model) {
        Person personDetails = personService.getPersonId(id);

        if (personDetails == null) {
            throw new ResponseStatusException (
                HttpStatus.NOT_FOUND, "Пользователь не найден"
            );
        }

        model.addAttribute("person", personDetails);
        model.addAttribute("title", "Пользователь: " + personDetails.getFullName());
        return "admin/user/details";
    }

    @GetMapping("/admin/user/edit/{id}")
    public String editUser(@PathVariable("id") int id, Model model){
        Person personEdit = personService.getPersonId(id);

        if (personEdit == null) {
            throw new ResponseStatusException (
                HttpStatus.NOT_FOUND, "Пользователь не найден"
            );
        }

        model.addAttribute("edit_person", personEdit);
        model.addAttribute("title", "Редактировать пользователя: " + personEdit.getFullName());
        return "admin/user/edit";
    }

    @PostMapping("/admin/user/edit/{id}")
    public String editUser(@RequestParam("role") String role, @PathVariable("id") int id, Model model){
        Person person = personService.getPersonId(id);

        if (role.isEmpty()) {
            model.addAttribute("edit_person", person);
            model.addAttribute("title", "Редактировать пользователя: " + person.getFullName());
            model.addAttribute("error", "Роль не может быть пустой");

            return "admin/user/edit";
        }

        person.setRole(role);
        personService.personEdit(id, person);
        model.addAttribute("person", person);
        model.addAttribute("title", "Пользователь: " + person.getFullName());
        return "admin/user/details";
    }
}
