import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "feign.clients")
public class FeignClientsProperties {
    private Map<String, FeignClientProperties> configs;

    public Map<String, FeignClientProperties> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, FeignClientProperties> configs) {
        this.configs = configs;
    }

    public static class FeignClientProperties {
        private String url;
        private String username;
        private String password;

        // getters and setters
    }
}


feign:
  clients:
    myApp-1:
      url: http://url-for-myapp-1.com
      username: usernameForMyApp1
      password: passwordForMyApp1
    myApp-2:
      url: http://url-for-myapp-2.com
      username: usernameForMyApp2
      password: passwordForMyApp2


import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DynamicFeignClientFactory {

    @Autowired
    private FeignClientsProperties feignClientsProperties;

    public <T> T createClient(Class<T> clientClass, String appName) {
        FeignClientsProperties.FeignClientProperties properties = feignClientsProperties.getConfigs().get(appName);
        if (properties == null) {
            throw new IllegalArgumentException("No configuration found for application: " + appName);
        }

        return Feign.builder()
                .requestInterceptor(new BasicAuthRequestInterceptor(properties.getUsername(), properties.getPassword()))
                .target(clientClass, properties.getUrl());
    }
}


import org.springframework.web.bind.annotation.GetMapping;

public interface DynamicFeignClient {
    @GetMapping("/some-endpoint")
    String getSomeData();
}


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired
    private DynamicFeignClientFactory feignClientFactory;

    @GetMapping("/call-api/{appName}")
    public String callApi(@PathVariable String appName) {
        DynamicFeignClient client = feignClientFactory.createClient(DynamicFeignClient.class, appName);
        return client.getSomeData();
    }
}
