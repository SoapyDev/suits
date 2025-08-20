package com.backend.suits.entity;

import com.backend.suits.userProfile.models.UserProfile;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@NoArgsConstructor
@Data
@Table(name = "users")
public class User {

    public User(@NonNull String username, @NonNull String password, @NonNull String name, @NonNull Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.userProfile = new UserProfile(this.getUsername(), this.getName());
    }

    @Id
    @Column(nullable = false, unique = true)
    @Email
    @NonNull
    private String username;

    @Column(nullable = false)
    @NonNull
    private String password;

    @Column(nullable = false)
    @NonNull
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    private UserProfile userProfile;

    @Version
    private Long version;

}
