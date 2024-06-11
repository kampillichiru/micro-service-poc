import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Add Basic Auth interceptor
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(basicAuthInterceptor("username", "password")); // Replace with actual username and password
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    private ClientHttpRequestInterceptor basicAuthInterceptor(String username, String password) {
        return (request, body, execution) -> {
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            String authHeader = "Basic " + new String(encodedAuth);
            request.getHeaders().set("Authorization", authHeader);
            return execution.execute(request, body);
        };
    }
}
