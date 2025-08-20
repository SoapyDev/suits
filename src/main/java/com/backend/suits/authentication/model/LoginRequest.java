package com.backend.suits.authentication.model;

/**
 * Représente la requête reçue lors d'une tentative de connexion
 */
public record LoginRequest(String username, String password) {
}
