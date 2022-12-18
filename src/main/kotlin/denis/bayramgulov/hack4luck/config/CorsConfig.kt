package denis.bayramgulov.hack4luck.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource


@Configuration
class CorsConfig {

    @Bean
    fun corsWebFilter(): CorsWebFilter? {
        val corsConfig = CorsConfiguration()
        corsConfig.allowedOrigins = listOf("http://localhost:63343")
        corsConfig.maxAge = 8000L
        corsConfig.addAllowedMethod("GET")
        corsConfig.addAllowedHeader("Hack4Luck-Allowed")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        return CorsWebFilter(source)
    }
}