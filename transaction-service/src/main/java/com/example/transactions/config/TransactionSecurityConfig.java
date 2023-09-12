package com.example.transactions.config;

import com.example.transactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class TransactionSecurityConfig extends WebSecurityConfigurerAdapter{

	public static final String USER_AUTHORITY = "usr";
	@Autowired
	TransactionService transactionService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(transactionService).passwordEncoder(getPE());;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic()
		.and().csrf().disable()
		.authorizeHttpRequests()
		.antMatchers("/transact/**").hasAuthority(USER_AUTHORITY)
		.and().formLogin();
	}

	@Bean
	public BCryptPasswordEncoder getPE() {
		return new BCryptPasswordEncoder();
	}
	
}
