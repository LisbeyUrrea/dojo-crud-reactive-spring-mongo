package com.lisbey.mongo.mongodojo.Controller;

import com.lisbey.mongo.mongodojo.Document.Person;
import com.lisbey.mongo.mongodojo.Service.IPersonService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
class PersonControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Mock
    IPersonService repository;

    PersonController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new PersonController(repository);
    }

    @Test
    void methodSavePersonsShouldCreatePerson() {
        Person person = Person.builder().name("Lisbey").lastName("Urrea").age(29).delete(false).build();
        Mono<Person> expect = Mono.just(person);

        when(repository.save(person)).thenReturn(expect);

         Mono<Person> result = controller.savePerson(person);

        StepVerifier.create(result)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(person1 -> true)
                .consumeRecordedWith(people -> assertThat(people.isEmpty()).isFalse())
                .verifyComplete();

        Assert.assertEquals(expect, result);
        verify(repository, times(1)).save(person);




       /* webClient
                .post()
                .uri("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(person), Person.class)
                .exchange()
                .expectStatus().isCreated(); */
    }


    @Test
    void methodEditPersonShouldUpdatePerson(){
        Person person = Person.builder().name("Lisbey").lastName("Urrea").age(29).delete(false).build();
        String id= "5df28176083191355f7b0789";

        Mono<Person> expected = Mono.just(Person.builder().id(id).name("Diana").lastName("Urrea").age(29).delete(false).build());
        when(repository.update(id,person)) .thenReturn((expected));

        Mono<Person> result = controller.editPerson(id, person);

        StepVerifier.create(result)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(person1 -> true)
                .consumeRecordedWith(people -> assertThat(people.isEmpty()).isFalse())
                .verifyComplete();

        Assert.assertEquals(expected, result);

        verify(repository, times(1)).update(id,person);

            /*    webClient.put()
                .uri("/person/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(person), Person.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Person.class); */

    }

    @Test
    void methodEditPersonShouldReturnErrorOnUpdatePerson(){
        Person person = Person.builder().name("Lisbey").lastName("Urrea").age(29).delete(false).build();
        String id= "5df28176083191355hyr748d";

        when(repository.update(id,person))
                .thenReturn(Mono.error(new Exception("Erro: El usuario con el id: "+id+" no existe ")));

        Mono<Person> result = controller.editPerson(id, person);

        StepVerifier.create(result)
                .verifyErrorMessage("Erro: El usuario con el id: "+id+" no existe ");


        verify(repository, times(1)).update(id,person);

               /* webClient.put()
                .uri("/person/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(person), Person.class)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBodyList(Person.class); */

    }

    @Test
    void methodGetByIdShouldFindPersonWhenParamIs5df28176083191355f7b0789(){
        Person person = Person.builder().id("5df28176083191355f7b0789").name("Lisbey").lastName("Urrea").age(29).delete(false).build();
        Mono<Person> expected = Mono.just(person);
        String id = "5df28176083191355f7b0789";
        when(repository.findById(id))
                .thenReturn(expected);


        Mono<Person> result = controller.getById(id);

        StepVerifier.create(result)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(person1 -> true)
                .consumeRecordedWith(people -> assertThat(people.isEmpty()).isFalse())
                .verifyComplete();

        Assert.assertEquals(expected, result);

        verify(repository, times(1)).findById("5df28176083191355f7b0789");

               /* webClient.get()
                .uri("/person/{id}",person.getId() )
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Person.class); */
    }

    @Test
    void methodGetByIdShouldReturnMonoError(){
        String id = "5df28176083191355f7b0789";
       when(repository.findById(id))
                .thenReturn(Mono.error(new Exception("Erro: El usuario con el id: "+id+" no existe ")));

       Mono<Person> result = controller.getById(id);

        StepVerifier.create(result)
                .verifyErrorMessage("Erro: El usuario con el id: "+id+" no existe ");

        verify(repository, times(1)).findById("5df28176083191355f7b0789");


      /*  webClient.get()
                .uri("/person/{id}",id )
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(Person.class); */

    }

    @Test
    void methodGetAllShouldFindAllPersons(){
        List<Person> personList = new ArrayList<>();
        Person person1 = Person.builder().id("5df28176083191355f7b0789").name("Lisbey").lastName("Urrea").age(29).delete(false).build();
        personList.add(person1);
        Person person2 = Person.builder().id("5df2817e083191355f7b078a").name("Diana").lastName("Urrea").age(33).delete(false).build();
        personList.add(person2);
        Flux<Person> expected = Flux.fromIterable(personList);
        Mockito.when(repository.findAll()).thenReturn(expected);

        Flux<Person> result = controller.getAll();

        Assert.assertEquals(expected, result);

        StepVerifier.create(result)
                .expectNext(person1)
                .expectNext(person2)
                .verifyComplete();


        verify(repository, times(1)).findAll();

       /* webClient.get()
                .uri("/person" )
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(response -> Assertions.assertThat(response.getResponseBody()).isNotNull()); */




    }

    @Test
    void methodDeleteShouldDeletePersonLogically(){
        String id= "5df28176083191355f7b0789";

        Mono<String> expected = Mono.just("Usuario eliminado exitosamente");

        when(repository.delete(id))
                .thenReturn(expected);

        Mono<String> result = repository.delete(id);

        Assert.assertEquals(expected, result);

        verify(repository, times(1)).delete(id);

              /*  webClient.delete()
                .uri("/person/{id}",id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Usuario eliminado exitosamente"); */
    }

    @Test
    void methodDeleteForceShouldDeletePersonPhysically(){
        String id= "5df28176083191355f7b0789";

        Mono<String> expected = Mono.just("Usuario eliminado exitosamente");

        when(repository.deleteForce(id))
                .thenReturn(expected);

        Mono<String> result = repository.deleteForce(id);

        Assert.assertEquals(expected, result);


        verify(repository, times(1)).deleteForce(id);

               /* webClient.delete()
                .uri("/person/force/{id}",id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Usuario eliminado exitosamente"); */
    }

}