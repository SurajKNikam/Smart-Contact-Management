package com.scm.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.FormLoginBeanDefinitionParser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.scm.services.impl.SecurityCustomUserDetailsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

//    private final DaoAuthenticationProvider authenticationProvider;
 
    
	@Autowired
	private OAuthAuthenticationSuccessHandler handler;
	
    @Autowired
    private SecurityCustomUserDetailsService userDetailService;
    
    @Autowired
    private AuthFailureHandler authFailureHandler;

//    SecurityConfig(DaoAuthenticationProvider authenticationProvider) {
//        this.authenticationProvider = authenticationProvider;
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/user/**").authenticated();
                auth.anyRequest().permitAll();
            })
            
            
            .formLogin(form -> form
                .loginPage("/login")                     // custom login page
                .loginProcessingUrl("/authenticate")
                // form POST target
                .defaultSuccessUrl("/user/profile",true)
                .failureUrl("/login?error=true")
                .failureHandler(authFailureHandler)
                .usernameParameter("email")
                 .passwordParameter("password")
//                 .failureHandler(new AuthenticationFailureHandler() {
					
//					@Override
//					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//							AuthenticationException exception) throws IOException, ServletException {
//						// TODO Auto-generated method stub
//						
//					}
//				})
                .permitAll()
                )
            
            
            
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable);
        
        //oauth configurations
        
        httpSecurity.oauth2Login(oauth->{
        	oauth.loginPage("/login");
        	oauth.successHandler(handler);
        	
        	
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
