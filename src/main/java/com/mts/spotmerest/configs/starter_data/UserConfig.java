package com.mts.spotmerest.configs.starter_data;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mts.spotmerest.mappers.UserDAO;
import com.mts.spotmerest.models.Role;
import com.mts.spotmerest.models.User;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Configuration
public class UserConfig {

    @Enumerated(EnumType.STRING)
    private Role role;
    
   @Bean
   CommandLineRunner commandLineRunner(UserDAO userDAO){
        

       return args -> {
           User test= new User("username",
                   "user",
                   "name",
                   "user@fake.com",
                   "password",
                   Role.USER
           );
           test.setPassword("password");
           userDAO.saveAll(
                   List.of(test));
       };
   }
}
