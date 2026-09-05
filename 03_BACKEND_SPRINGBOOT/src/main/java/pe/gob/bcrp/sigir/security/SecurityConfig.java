package pe.gob.bcrp.sigir.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${ldap.urls:ldap://localhost:389}")
    private String ldapUrl;

    @Value("${ldap.domain:bcrp.local}")
    private String ldapDomain;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos: Documentación Swagger, estado de salud y recursos estáticos
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/actuator/**",
                    "/api/v1/auth/**",
                    "/api/v1/telemetria/**"
                ).permitAll()
                // Operaciones de consulta y monitoreo
                .requestMatchers("/api/v1/entidades/**", "/api/v1/incidentes/**", "/api/v1/reportes/**")
                .hasAnyRole("OPERADOR", "SUPERVISOR", "ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Proveedor de Autenticación integrado con Active Directory (LDAP corporativo).
     * Mantiene un solo login institucional.
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        try {
            ActiveDirectoryLdapAuthenticationProvider adProvider =
                    new ActiveDirectoryLdapAuthenticationProvider(ldapDomain, ldapUrl);
            adProvider.setConvertSubErrorCodesToExceptions(true);
            adProvider.setUseAuthenticationRequestCredentials(true);
            return new ProviderManager(List.of(adProvider));
        } catch (Exception ex) {
            // Fallback en memoria si el controlador de dominio no está presente en entorno local
            return new ProviderManager(List.of());
        }
    }

    /**
     * Usuarios de respaldo corporativos para pruebas locales sin requerir Active Directory real.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails operador = User.builder()
                .username("operador.noc")
                .password(passwordEncoder.encode("Bcrp2026!"))
                .roles("OPERADOR")
                .build();

        UserDetails supervisor = User.builder()
                .username("supervisor.bcrp")
                .password(passwordEncoder.encode("Bcrp2026!"))
                .roles("SUPERVISOR", "OPERADOR")
                .build();

        UserDetails admin = User.builder()
                .username("admin.ti")
                .password(passwordEncoder.encode("Bcrp2026!"))
                .roles("ADMIN", "SUPERVISOR", "OPERADOR")
                .build();

        return new InMemoryUserDetailsManager(operador, supervisor, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
