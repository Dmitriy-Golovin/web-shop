package com.example.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shop.models.Person;
import com.example.shop.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Person findByLogin(Person person){
        Optional<Person> person_db = personRepository.findByLogin(person.getLogin());
        return person_db.orElse(null);
    }

    public Person getPersonId(int id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        return optionalPerson.orElse(null);
    }

    public List<Person> getAllUser() {
        return personRepository.findAll();
    }

    public Person getDefaultAdmin() {
        Optional<Person> admin = personRepository.findDefaultAdmin();
        return admin.orElse(null);
    }

    @Transactional
    public void personEdit(int id, Person person) {
        person.setId(id);
        personRepository.save(person);
    }

    @Transactional
    public void register(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        personRepository.save(person);
    }

    @Transactional
    public void createDefaultAdmin(){
        Person admin = new Person();
        admin.setLogin("admin");
        admin.setFullName("Админ");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setRole("ROLE_ADMIN");
        personRepository.save(admin);
    }
}
