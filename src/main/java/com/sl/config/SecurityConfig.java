//package com.sl.config;
//
//import com.vaadin.flow.spring.security.VaadinWebSecurity;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.provisioning.UserDetailsManager;
//
//@EnableWebSecurity
//@Configuration
//class SecurityConfig extends VaadinWebSecurity {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//       http.authorizeHttpRequests(
//                authz -> authz.requestMatchers("/images/**","/**","/login").
//                        permitAll()
//        );
//    }
//
//    @Bean
//    public UserDetailsManager userDetailsManager() {
//        LoggerFactory.getLogger(SecurityConfig.class)
//                .warn("NOT FOR PRODUCITON: Using in-memory user details manager!");
//        var user = User.withUsername("user")
//                .password("user")
//                .roles("USER")
//                .build();
//        var admin = User.withUsername("admin")
//                .password("admin")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }
//}