package it.epicode.epic_energy_services.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = true) //con debug true, nel caso di errori ci darà ancora piu info riguardo l'errore
@EnableMethodSecurity // EnableMethodSecurity PERMETTE DI ATTIVARE LA SICUREZZA SU I METODI DEL CONTROLLER CON ANNOTAZIONE @PRE AUTHORIZED
public class SecurityConfig {

    //disabilitiamo alcuni filtri, tipo l'autenticazione con il form, csrf(protezione contro accessi impropri), sessionManagement
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(e->e.disable()); // SENZA FRONT END SI BLOCCA
        httpSecurity.csrf(e->e.disable());
        httpSecurity.sessionManagement(e->e.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // SENZA FRONT END SI BLOCCA

        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/api/**").permitAll());
       // httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/api/users/**").permitAll());
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers(HttpMethod.POST,"/auth/**").permitAll()); // permette a tutti di accedere ai path signUp e Login

        httpSecurity.cors(Customizer.withDefaults());
        return httpSecurity.build();
    };




@Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(11);
    }



//BEAN PER ABILITARE CHIUNQUE A UTILIZZARE QUALSIASI SERVIZIO
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(List.of("*"));
    corsConfiguration.setAllowedMethods(List.of("*"));


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
