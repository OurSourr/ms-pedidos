package cl.duoc.ms.pedidos.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        // Se define aquí la URL base de ms-catalogo 
        return WebClient.builder()
                .baseUrl("http://localhost:8081") 
                .build();
    }
}