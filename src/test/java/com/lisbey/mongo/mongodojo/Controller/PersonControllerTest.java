package com.lisbey.mongo.mongodojo.Controller;

import com.lisbey.mongo.mongodojo.Document.Person;
import com.lisbey.mongo.mongodojo.Service.IPersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(PersonController.class)
class PersonControllerTest {
    @Autowired
    private WebTestClient webClient;

    private PersonController personController;

    //@Mock
    //private IPersonService personService;
    private IPersonService personService = mock(IPersonService.class);

    @BeforeEach
    void setUp() {
        personController = new PersonController(personService);
    }

    @Test
    void getAll() {

        Person person = Person
                .builder()
                .id("5df28ca060d4e36fa3b4bb10")
                .name("Lisbey")
                .lastName("Urrea")
                .age(25)
                .delete(false)
                .build();

        Mockito.when(personService.findAll()).thenReturn(Flux.just(person));

        final WebTestClient.ResponseSpec spec = webClient
                .get()
                .uri("/person")
                .exchange();


        spec.expectBody(String.class).consumeWith(rest -> {
            final HttpStatus status = rest.getStatus();
            final String body = rest.getResponseBody();
            assertThat(status.is2xxSuccessful()).isTrue();
            //assertThat(body).isEqualTo(Flux.just(person));
        });
    }

    @Test
    void getById() {
    }

    @Test
    void postSavePerson() {
    }

    @Test
    void putEditPerson() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void deleteUserForce() {
    }
}