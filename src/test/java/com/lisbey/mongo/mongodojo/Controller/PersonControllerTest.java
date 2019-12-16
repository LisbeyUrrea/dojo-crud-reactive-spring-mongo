package com.lisbey.mongo.mongodojo.Controller;

import com.lisbey.mongo.mongodojo.Dao.PersonDao;
import com.lisbey.mongo.mongodojo.Document.Person;
import com.lisbey.mongo.mongodojo.Service.IPersonService;
import com.lisbey.mongo.mongodojo.Service.Impl.PersonServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = PersonController.class)
@Import(PersonServiceImpl.class)
class PersonControllerTest {

    @MockBean
    PersonDao repository;

    @MockBean
    PersonController controller;

    @Autowired
    private WebTestClient webClient;

    @Test
    void souldCreatePerson(){
        Person person = Person.builder().name("Lisbey").lastName("Urrea").age(29).delete(false).build();


        Mockito
                .when(controller.savePerson(person))
                .thenReturn(Mono.just(person));

        webClient.post()
                .uri("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(person), Person.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Person.class);

        Mockito.verify(controller, times(1)).savePerson(person);

    }

    @Test
    void souldUpdatePerson(){
        Person person = Person.builder().name("Lisbey").lastName("Urrea").age(29).delete(false).build();
        String id= "5df28176083191355f7b0789";

        Person personUpdated = Person.builder().id(id).name("Diana").lastName("Urrea").age(29).delete(false).build();

        Mockito
                .when(controller.editPerson(id,person))
                .thenReturn(Mono.just(personUpdated));

        webClient.put()
                .uri("/person/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(person), Person.class)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Person.class);

        Mockito.verify(controller, times(1)).editPerson(id,person);

    }

    @Test
    void souldReturnErrorOnUpdatePerson(){
        Person person = Person.builder().name("Lisbey").lastName("Urrea").age(29).delete(false).build();
        String id= "5df28176083191355hyr748d";

        Person personUpdated = Person.builder().id(id).name("Diana").lastName("Urrea").age(29).delete(false).build();

        Mockito
                .when(controller.editPerson(id,person))
                .thenReturn(Mono.error(new Exception("Erro: El usuario con el id: "+id+" no existe ")));

        webClient.put()
                .uri("/person/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(person), Person.class)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBodyList(Person.class);

        Mockito.verify(controller, times(1)).editPerson(id,person);

    }

    @Test
    void shouldFindPersonById(){
        Person person = Person.builder().id("5df28176083191355f7b0789").name("Lisbey").lastName("Urrea").age(29).delete(false).build();

        Mockito
                .when(controller.getById("5df28176083191355f7b0789"))
                .thenReturn(Mono.just(person));


        webClient.get()
                .uri("/person/{id}",person.getId() )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Person.class);

        Mockito.verify(controller, times(1)).getById("5df28176083191355f7b0789");
    }

    @Test
    void souldRetournErrorSearchingAPerson(){
        String id = "5df28176083191355f7b0789";
        Mockito
                .when(controller.getById(id))
                .thenReturn(Mono.error(new Exception("Erro: El usuario con el id: "+id+" no existe ")));


        webClient.get()
                .uri("/person/{id}",id )
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(Person.class);

        Mockito.verify(controller, times(1)).getById("5df28176083191355f7b0789");

    }

    @Test
    void souldFindAllPersons(){
        List<Person> personList = new ArrayList<>();
        personList.add(Person.builder().id("5df28176083191355f7b0789").name("Lisbey").lastName("Urrea").age(29).delete(false).build());
        personList.add(Person.builder().id("5df2817e083191355f7b078a").name("Diana").lastName("Urrea").age(33).delete(false).build());

        Mockito.when(controller.getAll()).thenReturn(Flux.fromIterable(personList));

        webClient.get()
                .uri("/person" )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> Assertions.assertThat(response.getResponseBody()).isNotNull());


        Mockito.verify(controller, times(1)).getAll();

    }

    @Test
    void souldDeletePersonLogically(){
        String id= "5df28176083191355f7b0789";

        Mockito
                .when(controller.deletePerson(id))
                .thenReturn(Mono.just("Usuario eliminado exitosamente"));


        webClient.delete()
                .uri("/person/{id}",id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Usuario eliminado exitosamente");

        Mockito.verify(controller, times(1)).deletePerson(id);
    }

    @Test
    void souldDeletePersonPhysically(){
        String id= "5df28176083191355f7b0789";

        Mockito
                .when(controller.deletePersonForce(id))
                .thenReturn(Mono.just("Usuario eliminado exitosamente"));


        webClient.delete()
                .uri("/person/force/{id}",id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Usuario eliminado exitosamente");

        Mockito.verify(controller, times(1)).deletePersonForce(id);
    }

}