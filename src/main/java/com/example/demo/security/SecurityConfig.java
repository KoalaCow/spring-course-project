package com.example.demo.security;

import com.example.demo.constants.RolesConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/admin/**").hasRole(RolesConstants.ADMIN)
                .antMatchers("/sport/**").hasAnyRole(RolesConstants.SPORTS, RolesConstants.ADMIN)
                .antMatchers("/electricity/**").hasAnyRole(RolesConstants.ELECTRICITY, RolesConstants.ADMIN)
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles(RolesConstants.ADMIN)

                .and()
                .withUser("sports")
                .password(passwordEncoder().encode("sports123"))
                .roles(RolesConstants.SPORTS)

                .and()
                .withUser("electricity")
                .password(passwordEncoder().encode("electricity123"))
                .roles(RolesConstants.ELECTRICITY);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
