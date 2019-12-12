package com.lisbey.mongo.mongodojo.Dao;

import com.lisbey.mongo.mongodojo.Document.Person;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PersonDao extends ReactiveMongoRepository<Person, String> {

    public Flux<Person> findByDeleteFalse();

}
