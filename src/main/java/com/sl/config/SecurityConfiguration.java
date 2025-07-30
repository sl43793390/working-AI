package com.sl.config;

import com.sl.base.ui.view.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configure your static resources with public access before calling
        // super.configure(HttpSecurity) as it adds final anyRequest matcher
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/login", "/register", "/public/**")
            .permitAll())
            .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // 指定自定义登录页面的URL
                                .permitAll().defaultSuccessUrl("/chat", true))
                // Vaadin 特定配置 (重要！)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/login", "/vaadinServlet/**", "/service-worker.js") // 忽略 Vaadin 内部请求的 CSRF
                );

        super.configure(http); 

        // This is important to register your login view to the
        // navigation access control mechanism:
        setLoginView(http, LoginView.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Customize your WebSecurity configuration.
        super.configure(web);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);  // 使用 BCrypt 进行密码加密
    }

}