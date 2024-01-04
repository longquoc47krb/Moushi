package id.longquoc.messenger.config;

import id.longquoc.messenger.common.Constants;
import id.longquoc.messenger.filter.ApiKeyFilter;
import id.longquoc.messenger.filter.AuthTokenFilter;
import id.longquoc.messenger.security.CustomLogoutHandler;
import id.longquoc.messenger.security.jwt.AuthEntryPointJwt;
import id.longquoc.messenger.security.service.UserDetailsServiceImpl;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;
@Configuration
@EnableWebSecurity
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
    @Autowired
    private CustomLogoutHandler logoutHandler;

    private final String[] API_ENDPOINTS_NO_AUTH = {
            "/v1/api/auth/**",
            "/data/**",
            "/ws/**"
    };
    private final String[] API_ENDPOINTS_AUTH = {
            "/v1/api/user/**",
            "/v1/api/friendship/**",
            "/v1/api/conversation/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests((registry) -> {
                    registry.requestMatchers(API_ENDPOINTS_NO_AUTH).permitAll();
//                    registry.requestMatchers(API_ENDPOINTS_AUTH).hasAuthority(Constants.ROLE_BASIC);
                    registry.requestMatchers(API_ENDPOINTS_AUTH).permitAll();
                    }
                );
        http.csrf(AbstractHttpConfigurer::disable).exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider);

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

/*TODO: Đây là một phương thức cấu hình bảo mật trong Spring Security. Dưới đây là giải thích chi tiết:
   @Bean: Annotation này cho biết rằng phương thức trả về một bean sẽ được quản lý bởi Spring.
   - public SecurityFilterChain filterChain(HttpSecurity http) throws Exception: Phương thức này cấu hình chuỗi bộ lọc bảo mật (SecurityFilterChain) sử dụng đối tượng HttpSecurity.
   - http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)): Điều này cấu hình chính sách tạo phiên là STATELESS, có nghĩa là Spring Security sẽ không tạo hoặc sử dụng bất kỳ phiên HTTP nào.
   - authorizeHttpRequests((registry) -> {...}): Phương thức này cấu hình quyền truy cập vào các endpoint HTTP. Trong đó, API_ENDPOINTS_NO_AUTH được cho phép truy cập mà không cần xác thực và API_ENDPOINTS_AUTH yêu cầu quyền ROLE_BASIC.
   - http.csrf((csrf) -> csrf.disable()): Điều này vô hiệu hóa bảo vệ chống tấn công CSRF (Cross-Site Request Forgery).
   - http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)): Điều này cấu hình điểm nhập xác thực để xử lý các ngoại lệ liên quan đến xác thực.
   - http.authenticationProvider(authenticationProvider()): Điều này cấu hình nhà cung cấp xác thực tùy chỉnh.
   - http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class): Điều này thêm một bộ lọc trước UsernamePasswordAuthenticationFilter. Bộ lọc authTokenFilter sẽ được gọi trước khi UsernamePasswordAuthenticationFilter được gọi.
   - return http.build(): Cuối cùng, phương thức này xây dựng và trả về đối tượng SecurityFilterChain12.
* */