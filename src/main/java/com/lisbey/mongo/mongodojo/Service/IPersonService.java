package com.lisbey.mongo.mongodojo.Service;

import com.lisbey.mongo.mongodojo.Document.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPersonService {

    public Flux<Person> findAll();
    public Mono<Person> findById(String id);
    public Mono<Person> save(Person person);
    public Mono<Person> update(String id, Person person);
    public Mono<String> delete(String id);
    public Mono<String> deleteForce(String id);

}
