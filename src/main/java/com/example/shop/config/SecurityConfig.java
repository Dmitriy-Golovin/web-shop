package com.example.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.shop.services.PersonDetailsService;

@Configuration
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    @Bean
    public PasswordEncoder getPasswordEncode(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers("/admin").hasRole("ADMIN")
            .requestMatchers("/authentication", "/registration", "/error", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/image/**", "/", "/product/**").permitAll()
            .anyRequest().hasAnyRole("USER", "ADMIN")
            .and() 
            .formLogin().loginPage("/authentication")
            .loginProcessingUrl("/process_login")
            .defaultSuccessUrl("/auth", true)
            .failureUrl("/authentication?error")
            .and()
            .logout().logoutUrl("/logout").logoutSuccessUrl("/authentication");

        return http.build();
    }

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }



    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncode());
    }

}
