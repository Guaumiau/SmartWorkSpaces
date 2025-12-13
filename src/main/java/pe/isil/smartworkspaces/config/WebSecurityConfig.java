package pe.isil.smartworkspaces.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pe.isil.smartworkspaces.security.UserDetailsServiceImp;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        // 1. RECURSOS PÚBLICOS (Nadie necesita loguearse)
                        .requestMatchers("/", "/login", "/registro", "/css/**", "/js/**", "/media/**", "/images/**").permitAll()

                        // 2. ZONA DE ADMINISTRADOR INTERNO
                        .requestMatchers("/admin/**", "/salas/**").hasRole("ADMIN")

                        // 3. ZONA DE USUARIOS DEL COWORKING
                        .requestMatchers("/reservas/**").hasAnyRole("USER", "ADMIN")

                        // 4. Lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                )
                // Manejo de error si un USUARIO intenta entrar a /salas (Acceso Denegado)
                .exceptionHandling(
                        customizer->customizer.accessDeniedHandler(accesoDenegado())
                )
                .userDetailsService(userDetailsServiceImp);

        return http.build();
    }

    @Bean
    AccessDeniedHandler accesoDenegado()
    {
        return
                (request, response, accessDeniedException)
                        -> response.sendRedirect(request.getContextPath() + "/403")
                ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}