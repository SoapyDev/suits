package com.backend.suits.organisation.models;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Data
public class TeamMembers {
    private String name;
    private String service;
}
