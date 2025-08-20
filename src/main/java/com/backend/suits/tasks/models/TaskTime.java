package com.backend.suits.tasks.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
@NoArgsConstructor
@Data
public class TaskTime{
    private long id;
    private String name;
    private long time;
    private long tarification;
}
