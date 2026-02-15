package com.secureexam.backend.common;

import com.secureexam.backend.user.Role;
import com.secureexam.backend.user.User;
import com.secureexam.backend.user.repository.RoleRepository;
import com.secureexam.backend.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {

        return args -> {

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

            Role studentRole = roleRepository.findByName("STUDENT")
                    .orElseGet(() -> roleRepository.save(new Role("STUDENT")));

            Role examinerRole = roleRepository.findByName("EXAMINER")
                    .orElseGet(() -> roleRepository.save(new Role("EXAMINER")));

            if (!userRepository.existsByEmail("admin@secureexam.com")) {

                User admin = new User();
                admin.setName("System Admin");
                admin.setEmail("admin@secureexam.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(adminRole));
                admin.setEnabled(true);

                userRepository.save(admin);
            }
        };
    }
}
