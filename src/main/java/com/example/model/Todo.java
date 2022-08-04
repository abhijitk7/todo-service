package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Todo implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String userName;

    private String description;

    @JsonFormat(pattern="dd-MMM-yyyy")
    private Date dateOfCompletion;

    private Boolean isCompleted;
}
