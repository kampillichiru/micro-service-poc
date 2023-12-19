import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.ResponseInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientInterceptorConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // Log or manipulate request details if needed
                System.out.println("Feign Request: " + template);
            }
        };
    }

    @Bean
    public ResponseInterceptor responseInterceptor() {
        return new ResponseInterceptor() {
            @Override
            public void apply(Response response) {
                // Log or manipulate response details if needed
                System.out.println("Feign Response: " + response);
            }
        };
    }
}

