package com.backend.suits.authentication.model;

import com.backend.suits.entity.Role;

/**
 * Représente la requête reçue lors d'une création d'un nouvel utilisateur
 */
public record RegisterResponse(String username, String name, Role role) {
}
