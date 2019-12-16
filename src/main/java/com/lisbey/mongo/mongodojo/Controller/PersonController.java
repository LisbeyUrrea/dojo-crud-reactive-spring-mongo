package com.lisbey.mongo.mongodojo.Controller;

import com.lisbey.mongo.mongodojo.Document.Person;
import com.lisbey.mongo.mongodojo.Service.IPersonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value="person")
public class PersonController {

    @Autowired
    private IPersonService personService;

    @GetMapping
    public Flux<Person> getAll(){
        return personService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Person> getById(@PathVariable String id){
        return personService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Person> savePerson(@RequestBody Person person){
        person.setDelete(false);
        return  personService.save(person);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Person> editPerson(@PathVariable String id, @RequestBody Person person)  {
        return  personService.update(id, person);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> deletePerson(@PathVariable String id){
        return personService.delete(id);
    }

    @DeleteMapping(value = "/force/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> deletePersonForce(@PathVariable String id){
        return personService.deleteForce(id);
    }


}
