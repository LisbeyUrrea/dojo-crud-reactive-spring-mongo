package com.lisbey.mongo.mongodojo.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Document(collection = "person")
@Data
@Builder
public class Person implements Serializable {

    @Id
    @NotNull
    private String id;
    private String name;
    private String lastName;
    private Integer age;
    private Boolean delete;


}
