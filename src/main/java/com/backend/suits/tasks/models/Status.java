package com.backend.suits.tasks.models;


import lombok.Getter;

@Getter
public enum Status {
    NON_DEBUTE("NON_DEBUTE"),
    EN_COURS("EN_COURS"),
    EN_ATTENTE("EN_ATTENTE"),
    TERMINEE("TERMINEE");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public static Status fromString(String status) {
        for (Status s : Status.values()) {
            if (s.getStatus().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Aucun statut correspondant à : " + status);
    }

}
