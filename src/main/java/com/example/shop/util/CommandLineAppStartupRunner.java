package com.example.shop.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.shop.models.Person;
import com.example.shop.services.PersonService;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final PersonService personService;

    @Autowired
    public CommandLineAppStartupRunner(PersonService personService) {
        this.personService = personService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        Person existAdmin = personService.getDefaultAdmin();

        if (existAdmin == null) {
            personService.createDefaultAdmin();
        }
    }
    
}
