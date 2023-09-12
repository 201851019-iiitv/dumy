package com.example.user.config;

import com.example.user.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.example.user.constants.UserConstants.*;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
	UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(getPE());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and().csrf().disable()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.POST, "/user/**").permitAll()
                .antMatchers("/user/**").hasAuthority(USER_AUTHORITY)
                .antMatchers("/admin/**").hasAnyAuthority(ADMIN_AUTHORITY, SERVICE_AUTHORITY)
                .and().formLogin();
    }

    @Bean
   public BCryptPasswordEncoder getPE() {
        return new BCryptPasswordEncoder();
    }

}
