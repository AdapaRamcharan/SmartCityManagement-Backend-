package com.smartcity.backend;

import com.smartcity.backend.entity.City;
import com.smartcity.backend.entity.User;
import com.smartcity.backend.repository.CityRepository;
import com.smartcity.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Arrays;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initData(CityRepository cityRepo, UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            if (cityRepo.count() == 0) {
                City hyd = new City(
                    "Hyderabad",
                    "The City of Pearls - Famous for its IT industry and historical monuments",
                    "https://images.unsplash.com/photo-1599661046289-e31897846e41?w=600&h=400&fit=crop",
                    "Telangana",
                    "India"
                );
                
                City chn = new City(
                    "Chennai",
                    "Gateway to South India - Known for its culture and beaches",
                    "https://images.unsplash.com/photo-1627873649417-4d71bcdd2014?w=600&h=400&fit=crop",
                    "Tamil Nadu",
                    "India"
                );
                
                City mum = new City(
                    "Mumbai",
                    "Financial Capital - Home to Bollywood and major financial institutions",
                    "https://images.unsplash.com/photo-1578359421253-a12fb4444e36?w=600&h=400&fit=crop",
                    "Maharashtra",
                    "India"
                );
                
                City del = new City(
                    "Delhi",
                    "Capital City - Rich in history and political importance",
                    "https://images.unsplash.com/photo-1486490126798-f650c09adc3b?w=600&h=400&fit=crop",
                    "Delhi",
                    "India"
                );

                cityRepo.saveAll(Arrays.asList(hyd, chn, mum, del));
                System.out.println("✓ Initial cities created");
            }

            if (userRepo.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setEmail("admin@smartcity.com");
                admin.setRole("ADMIN");
                userRepo.save(admin);
                System.out.println("✓ Admin user created: admin@smartcity.com / admin123");
            }
        };
    }
}
