package at.codersbay.java.taskapp.rest.restapi.security;

import at.codersbay.java.taskapp.rest.services.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;



    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    public class SecurityConfig {

        @Autowired
        private JwtAuthFilter authFilter;

        @Bean
        public UserDetailsService userDetailsService() {
            return new AppUserDetailsService();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          /** normal ein risiko*/
            return http.csrf().disable()
                    .authorizeHttpRequests()
                    .requestMatchers(
                            new RegexRequestMatcher("authenticate", HttpMethod.POST.toString()),
                            //  GOOGLE ant Muster...permit all erlaubt vorherige Requests
                            new AntPathRequestMatcher("/tasks/", HttpMethod.GET.toString())
                    ).permitAll()
                    .and()
                    .authorizeHttpRequests().requestMatchers(
                            new AntPathRequestMatcher("/user/**", HttpMethod.GET.toString())
                           // new AntPathRequestMatcher("/students/**", HttpMethod.POST.toString()),
                           // new AntPathRequestMatcher("/students/**", HttpMethod.DELETE.toString()),
                           // new AntPathRequestMatcher("/students/**", HttpMethod.PUT.toString())

                    )
                    .authenticated().and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationProvider authenticationProvider(){
            DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService());
            authenticationProvider.setPasswordEncoder(passwordEncoder());
            return authenticationProvider;
        }
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }

    }
