package id.longquoc.messenger.config;

import id.longquoc.messenger.common.Constants;
import id.longquoc.messenger.filter.ApiKeyFilter;
import id.longquoc.messenger.filter.AuthTokenFilter;
import id.longquoc.messenger.security.jwt.AuthEntryPointJwt;
import id.longquoc.messenger.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterChainConfig {
    @Autowired
    private ApiKeyFilter apiKeyFilter;
    @Autowired
    private AuthTokenFilter authTokenFilter;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Autowired
    private AuthenticationProvider authenticationProvider;
//    @Autowired
//    private LogoutHandler logoutHandler;
    private final String[] API_ENDPOINTS_NO_AUTH = {"/v1/api/auth/**"};
    private final String[] API_ENDPOINTS_AUTH = {"/v1/api/user/**", "/v1/api/friendship/**","/v1/api/conversation/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable()).exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests((registry) -> {
                    registry.requestMatchers(API_ENDPOINTS_NO_AUTH).permitAll();
                    registry.requestMatchers(API_ENDPOINTS_AUTH).hasAuthority(Constants.ROLE_BASIC);
                    registry.requestMatchers("/ws/**").permitAll();
                        }
                );
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider);
//                        .logout(logout -> {
//                            logout.logoutUrl("/v1/api/logout").addLogoutHandler(logoutHandler).logoutSuccessHandler((request,response,authentication)-> SecurityContextHolder.clearContext());
//                        });

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("123456")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }



}
