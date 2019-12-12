package com.lisbey.mongo.mongodojo.Controller;

import com.lisbey.mongo.mongodojo.Document.Person;
import com.lisbey.mongo.mongodojo.Service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(value="person")
public class PersonController {

    @Autowired
    private IPersonService personService;

    public PersonController(IPersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public Flux<Person> getAll(){
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Person> getById(@PathVariable String id){
        return personService.findById(id);
    }

    @PostMapping
    public Mono<Person> postSavePerson(@RequestBody Person person){
        person.setDelete(false);
        return  personService.save(person);
    }

    @PutMapping(value = "/{id}")
    public Mono<Person> putEditPerson(@PathVariable String id, @RequestBody Person person)  {
        return  personService.update(id, person);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<String> deleteUser(@PathVariable String id){
        return personService.delete(id);
    }

    @DeleteMapping(value = "/force/{id}")
    public Mono<String> deleteUserForce(@PathVariable String id){
        return personService.deleteForce(id);
    }


}
