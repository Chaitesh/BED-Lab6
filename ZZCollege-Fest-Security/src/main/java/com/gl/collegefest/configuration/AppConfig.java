package com.gl.collegefest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


import com.gl.collegefest.service.UserService;

@Configuration
public class AppConfig {

	UserService service;

	public AppConfig(UserService service) {
		super();
		this.service = service;
	}

//	@Bean
//	public static BCryptPasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}

	/*
	 * This is inmemory authentication
	 */	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {

		UserDetails Hitesh = User.builder().username("Hitesh").password("{noop}welcome").roles("ADMIN").build();
		UserDetails Rahul = User.builder().username("Rahul").password("{noop}welcome").roles("USER").build();
		UserDetails Ajay = User.builder().username("Ajay").password("{noop} welcome").roles("USER").build();
		return new InMemoryUserDetailsManager(Hitesh, Rahul, Ajay);

	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(
				(configurer) -> configurer
				.requestMatchers("/registration/**").permitAll()
				.requestMatchers("/regSuccess/**").permitAll()
				.requestMatchers("/").hasAnyRole("USER", "ADMIN")
				.requestMatchers("/students/list/").hasAnyRole("USER", "ADMIN")
				.requestMatchers("/student/showStudentFormForAdd/**").hasAnyRole("USER", "ADMIN")
				.requestMatchers("/students/saveStudent/").hasAnyRole("USER", "ADMIN")
				.requestMatchers("/students/delete/**").hasRole("ADMIN").
				requestMatchers("/students/studentFormForEdit/**").hasRole("ADMIN")
				.anyRequest()
				.authenticated()

				)
		.formLogin((form)->form.loginPage("/login").loginProcessingUrl("/authenticateTheUser").permitAll()

				)
		.logout(logout->logout.permitAll())
		.exceptionHandling(configurer->configurer.accessDeniedPage("/access-denied"));
		return http.build();


	}
}
