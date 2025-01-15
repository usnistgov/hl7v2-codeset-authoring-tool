package gov.nist.hit.hl7.codesetauthoringtool.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService jwtUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, UserDetailsService jwtUserDetailsService, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.exceptionHandling((exception) -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        http.csrf(csrf -> csrf.disable())
//                .authorizeRequests().requestMatchers("/api/auth/v1/login").permitAll()
//                .requestMatchers("/api/auth/v1/register").permitAll()
//                .requestMatchers("/api/password/**").permitAll()
//                .requestMatchers("/api/users").permitAll()
//                .requestMatchers("/api/user/**").permitAll()
//                .requestMatchers("/api/**").fullyAuthenticated()
//                .and().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/v1/login","/api/auth/v1/logout","/api/auth/v1/status").permitAll() // Permit all requests to login endpoint
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/codesets").authenticated() // Require authentication for all other /api/v1/** endpoints
                ).authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/codesets/*").authenticated() // Require authentication for all other /api/v1/** endpoints
                ).authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/codesets/*/versions/*").authenticated() // Require authentication for all other /api/v1/** endpoints
                ).authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/codesets/*/versions/*/commit").authenticated()
                ).authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/codesets/*/versions/*/exportCSV").authenticated()
                ).authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/users", "/api/users/*").authenticated()
                ).authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/api-keys", "/api/api-keys/*").authenticated()
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll() // Require authentication for all other /api/v1/** endpoints
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configure stateless session management
                )
                .addFilterBefore( jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter
        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
