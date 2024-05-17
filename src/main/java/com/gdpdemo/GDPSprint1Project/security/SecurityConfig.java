package com.gdpdemo.GDPSprint1Project.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import com.gdpdemo.GDPSprint1Project.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	 
	/*
    @Autowired
    private MyUserDetailsService userDetailsService;

	
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception { auth .userDetailsService(userDetailsService)
	 * .passwordEncoder(passwordEncoder); }
	 */
	// Configure security access and permissions
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        
            .authorizeRequests()
                .antMatchers("/", "/myposts", "/category", "/Login", "/SignUp", "/ForgotPassword", "/resetverifyotp", "/resetSendOtp","/admin").permitAll()
                
            .and()
                .formLogin()
                .loginPage("/Login")
                .defaultSuccessUrl("/category")
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
            .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");
        
        
		/*
		 * .authorizeRequests() // Allow admins to see all pages .antMatchers("/admin",
		 * "/adminView").hasRole("ADMIN") // Allow everyone else to see all other pages
		 * .anyRequest().authenticated() .and() // Use form login .formLogin()
		 * .loginPage("/Login") .defaultSuccessUrl("/category") .and() // Use logout
		 * .logout() .logoutUrl("/logout") .logoutSuccessUrl("/") .and() // Handle
		 * exceptions .exceptionHandling() .accessDeniedPage("/access-denied");
		 */
    }

    // Use this method to define a password encoder if required
	  @Bean 
	  public BCryptPasswordEncoder passwordEncoder() { return new
	  BCryptPasswordEncoder(); }
	 
}
