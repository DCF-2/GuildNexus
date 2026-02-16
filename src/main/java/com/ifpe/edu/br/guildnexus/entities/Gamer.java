package com.ifpe.edu.br.guildnexus.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_gamers")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Gamer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Métodos obrigatórios do UserDetails (Spring Security)
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por enquanto, todo mundo é usuário comum, sem roles específicas
        return List.of(); 
    }

    @Override
    public String getUsername() {
        return this.email; // Vamos usar o EMAIL para logar
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}