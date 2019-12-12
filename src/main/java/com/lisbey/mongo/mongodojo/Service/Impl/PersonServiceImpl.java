package com.lisbey.mongo.mongodojo.Service.Impl;

import com.lisbey.mongo.mongodojo.Document.Person;
import com.lisbey.mongo.mongodojo.Dao.PersonDao;
import com.lisbey.mongo.mongodojo.Service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonServiceImpl  implements IPersonService {

    @Autowired
    private PersonDao personDao;

     @Override
    public Flux<Person> findAll() {
        return personDao.findByDeleteFalse();
    }

    @Override
    public Mono<Person> findById(String id) {
        return personDao.findById(id)
                .switchIfEmpty(Mono.error(new Exception("Erro: El usuario con el id: "+id+"no existe ")));
    }

    @Override
    public Mono<Person> save(Person person) {
        return personDao.insert(person);
    }

    @Override
    public Mono<Person> update(String id, Person person) {
        return findById(id).doOnSuccess(findPerson -> {
            findPerson.setAge(person.getAge());
            findPerson.setName(person.getName());
            findPerson.setLastName(person.getLastName());
            personDao.save(findPerson);
        });
    }

    @Override
    public Mono<String> delete(String id) {
     return findById(id).doOnSuccess(person -> {
           person.setDelete(true);
           personDao.save(person).subscribe();
       }).flatMap(blog -> Mono.just("Usuario eliminado exitosamente"));

    }

    @Override
    public Mono<String> deleteForce(String id) {
    return findById(id)
                .flatMap(existingPerson -> personDao.delete(existingPerson).then(Mono.just("Usuario eliminado exitosamente")));
    }
}
