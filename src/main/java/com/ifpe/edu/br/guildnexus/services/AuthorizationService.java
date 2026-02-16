package com.ifpe.edu.br.guildnexus.services;

import com.ifpe.edu.br.guildnexus.repositories.GamerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    GamerRepository gamerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return gamerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gamer não encontrado"));
    }
}