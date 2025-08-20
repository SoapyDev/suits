package com.backend.suits.authentication.model;

/**
 * Représente la requête reçue lors d'une création d'un nouvel utilisateur
 */
public record RegisterRequest(String username, String password, String name) { }

