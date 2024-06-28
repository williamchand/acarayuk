package com.amenodiscovery.authentication.spring;

import com.amenodiscovery.authentication.security.google2fa.CustomAuthenticationProvider;
import com.amenodiscovery.authentication.security.location.DifferentLocationChecker;
import com.maxmind.db.Reader.FileMode;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

// @ImportResource({ "classpath:webSecurityConfig.xml" })
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTRequestFilter jwtRequestFilter;

    @Value("classpath:maxmind/GeoLite2-City.mmdb")
    Resource resourceFile;

    // @Bean
    // public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    //     return http.getSharedObject(AuthenticationManagerBuilder.class)
    //         .authenticationProvider(authProvider())
    //         .build();
    // }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers(new AntPathRequestMatcher("/resources/**", "/h2/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        Customizer<CorsConfigurer<HttpSecurity>> corsCustomizer = Customizer.withDefaults();
        return http
            .cors(corsCustomizer)
            .securityContext((securityContext) -> securityContext.requireExplicitSave(true))
            .authenticationProvider(authProvider())
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers(
                    "/v1/user/registration/captcha",
                    "/v1/user/registration/captchav3", 
                    "/resources/**", 
                    "/qrcode*",
                    "/v1/user/registration",
                    "/v1/user/registration-token/resend",
                    "/v1/user/password/reset",
                    "/v1/user/password/save",
                    "/v1/user/2fa/update",
                    "/v1/user/registration-confirm",
                    "/v1/user/login",
                    "/v1/user/login/google",
                    "/v1/user/enable-new-location"
                ))
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry())
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers(
                "/v1/user/registration/captcha",
                "/v1/user/registration/captchav3", 
                "/resources/**", 
                "/qrcode*",
                "/v1/user/registration",
                "/v1/user/registration-token/resend",
                "/v1/user/password/reset",
                "/v1/user/password/save",
                "/v1/user/2fa/update",
                "/v1/user/registration-confirm",
                "/v1/user/login",
                "/v1/user/login/google",
                "/v1/user/enable-new-location"
                )
                .permitAll()
                .requestMatchers("/v1/public/**").permitAll()
                .requestMatchers("/v1/user/password/update").authenticated()
                .anyRequest().authenticated()) 
            .build();
    }

    // beans
    @Bean
    public SecurityExpressionHandler<FilterInvocation> customWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setPostAuthenticationChecks(differentLocationChecker());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean(name = "GeoIPCountry")
    public DatabaseReader databaseReader() throws IOException, GeoIp2Exception {
        InputStream dbAsStream = resourceFile.getInputStream(); // <-- this is the difference
        return new DatabaseReader
            .Builder(dbAsStream)
            .fileMode(FileMode.MEMORY)
            .build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        String hierarchy = "ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER";
        return RoleHierarchyImpl.fromHierarchy(hierarchy);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public DifferentLocationChecker differentLocationChecker() {
        return new DifferentLocationChecker();
    }
}
