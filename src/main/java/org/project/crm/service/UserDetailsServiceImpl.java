package org.project.crm.service;

import lombok.RequiredArgsConstructor;
import org.project.crm.entity.Audit;
import org.project.crm.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<Audit> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Audit save(Audit audit) {
        audit.setPassword(passwordEncoder.encode(audit.getPassword()));
        return userRepository.save(audit);
    }
}
