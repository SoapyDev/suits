package com.backend.suits.authentication.model;

/**
 * Représente la réponse retournée lorsque la connexion est réussie
 */
public record LoginResponse(String username, String role) {
}
