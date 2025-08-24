package dev.killercavin.springbootnotesapp.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/api/notes/**").authenticated()
                it.anyRequest().permitAll()
            }
            .httpBasic { }
            .formLogin { it.disable() }

        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user: UserDetails = User
            .withUsername("user")
            .password("{noop}user123")
            .roles("USER")
            .build()

        return InMemoryUserDetailsManager(user)
    }
}
