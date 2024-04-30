We are to get the response like below using below mentioned KIBANA devtool and postman.
We want to achive using programaticallly in springboot application.

Need step by step code and should get exact results

elastic search Url: https://elastic-search/myapp/_search

restquest Body:

{
  "fields": ["status_code", "name", {"field":"@timestamp", "format": "strict_date_optional_time"}],
  "query": {
    "bool": {
      "must": [],
      "filter": [
        {"range": {"status_code": {"gte": 100, "lte": 600}}},
        {
          "bool": {
            "should": [
              {"match_phrase": {"name": "MY_RESPONSE"}},
              {"match_phrase": {"name": "My Request_REQUEST"}}
            ],
            "minimum_should_match": 1
          }
        },
        {"range": {"@timestamp": {"format": "strict_date_optional_time", "gte": "2023-03-25T00:29:08.691Z"}}}
      ]
    }
  },
  "aggs": {
    "api_name": {
      "terms": {
        "field": "name.keyword",
        "aggs": {
          "status_ranges": {
            "range": {
              "field": "status_code",
              "ranges": [
                {"from": 200, "to": 299, "key": "SUCCESS"},
                {"from": 400, "to": 499, "key": "CS_ERROR"},
                {"from": 500, "to": 599, "key": "SS_ERROR"}
              ]
            }
          }
        }
      }
    }
  }
}



Response body:

{
  "aggregations": {
    "api_name": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 0,
      "buckets": [
        {
          "key": "FEIGN_RESPONSE",
          "doc_count": 657618,
          "status_ranges": {
            "buckets": [
              {
                "key": "SUCCESS",
                "from": 200.0,
                "to": 299.0,
                "doc_count": 638009
              },
              {
                "key": "CS_ERROR",
                "from": 400.0,
                "to": 499.0,
                "doc_count": 19222
              },
              {
                "key": "SS_ERROR",
                "from": 500.0,
                "to": 599,
                "doc_count": 387
              }
            ]
          }
        }
      ]
    }
  }
}
ChatGPT
To achieve this in a Spring Boot application, you can use the Spring RestTemplate or the more modern WebClient to make HTTP requests to Elasticsearch. Below is a step-by-step guide to implement this in a Spring Boot application:

Step 1: Add dependencies to your pom.xml or build.gradle file. If you're using Maven, add the following dependencies:

xml
Copy code
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
Step 2: Create a configuration class to define RestTemplate or WebClient beans:

java
Copy code
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
or using WebClient:

java
Copy code
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
Step 3: Create a service class to make the Elasticsearch request:

java
Copy code
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticsearchService {

    @Value("${elasticsearch.url}")
    private String elasticsearchUrl;

    private final RestTemplate restTemplate;

    public ElasticsearchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getElasticsearchData() {
        // Prepare request body
        String requestBody = "{\"fields\": [\"status_code\", \"name\", {\"field\":\"@timestamp\", \"format\": \"strict_date_optional_time\"}], \"query\": {\"bool\": {\"must\": [],\"filter\": [{\"range\": {\"status_code\": {\"gte\": 100, \"lte\": 600}}},{\"bool\": {\"should\": [{\"match_phrase\": {\"name\": \"MY_RESPONSE\"}},{\"match_phrase\": {\"name\": \"LESTR_FEIGN_REQUEST\"}}],\"minimum_should_match\": 1}},{\"range\": {\"@timestamp\": {\"format\": \"strict_date_optional_time\", \"gte\": \"2023-03-25T00:29:08.691Z\"}}}]}}}";

        // Make the request
        return restTemplate.postForObject(elasticsearchUrl, requestBody, String.class);
    }
}
or using WebClient:

java
Copy code
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ElasticsearchService {

    @Value("${elasticsearch.url}")
    private String elasticsearchUrl;

    private final WebClient.Builder webClientBuilder;

    public ElasticsearchService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public String getElasticsearchData() {
        // Prepare request body
        String requestBody = "{\"fields\": [\"status_code\", \"name\", {\"field\":\"@timestamp\", \"format\": \"strict_date_optional_time\"}], \"query\": {\"bool\": {\"must\": [],\"filter\": [{\"range\": {\"status_code\": {\"gte\": 100, \"lte\": 600}}},{\"bool\": {\"should\": [{\"match_phrase\": {\"name\": \"MY_RESPONSE\"}},{\"match_phrase\": {\"name\": \"LESTR_FEIGN_REQUEST\"}}],\"minimum_should_match\": 1}},{\"range\": {\"@timestamp\": {\"format\": \"strict_date_optional_time\", \"gte\": \"2023-03-25T00:29:08.691Z\"}}}]}}}";

        // Make the request
        return webClientBuilder.build()
                .post()
                .uri(elasticsearchUrl)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
Step 4: Use the service in your controller:

java
Copy code
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;

    public ElasticsearchController(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    @GetMapping("/elasticsearch")
    public String getElasticsearchData() {
        return elasticsearchService.getElasticsearchData();
    }
}
Step 5: Configure your application properties file (application.properties or application.yml) with the Elasticsearch URL:

properties
Copy code
elasticsearch.url=https://elastic-search/myapp/_search
or in YAML format:

yaml
Copy code
elasticsearch:
  url: https://elastic-search/myapp/_search
With these steps, your Spring Boot application should be able to make the Elasticsearch request programmatically and retrieve the response. Adjust the code as necessary based on your specific requirements and environment.

is there any elegantway to create request and response
ChatGPT
Absolutely! You can create request and response models to represent the structure of your Elasticsearch query and its response. This approach not only makes your code more organized and readable but also helps with type safety and validation. Let me show you how to do it:

Step 1: Create Request and Response Models

java
Copy code
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ElasticsearchRequest {
    private List<String> fields;
    private Query query;
    private Aggregations aggs;

    // Constructors, getters, and setters
}

class Query {
    private Bool bool;

    // Constructor, getters, and setters
}

class Bool {
Sure, here's the continuation:

java
Copy code
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ElasticsearchRequest {
    private List<String> fields;
    private Query query;
    private Aggregations aggs;

    // Constructors, getters, and setters
}

class Query {
    private Bool bool;

    // Constructor, getters, and setters
}

class Bool {
    private List<Object> must;
    private List<Filter> filter;

    // Constructors, getters, and setters
}

class Filter {
    private Range range;
    private Bool bool;

    // Constructors, getters, and setters
}

class Range {
    private StatusCode statusCode;
    @JsonProperty("@timestamp")
    private Timestamp timestamp;

    // Constructors, getters, and setters
}

class StatusCode {
    private int gte;
    private int lte;

    // Constructors, getters, and setters
}

class Timestamp {
    private String format;
    private String gte;

    // Constructors, getters, and setters
}

class Aggregations {
    @JsonProperty("api_name")
    private ApiName apiName;

    // Constructors, getters, and setters
}

class ApiName {
    private Terms terms;

    // Constructors, getters, and setters
}

class Terms {
    private String field;
    private Aggregations statusRanges;

    // Constructors, getters, and setters
}

class Aggregations {
    private Range range;

    // Constructors, getters, and setters
}

class Range {
    private String field;
    private List<RangeBucket> ranges;

    // Constructors, getters, and setters
}

class RangeBucket {
    private int from;
    private int to;
    private String key;

    // Constructors, getters, and setters
}
Step 2: Deserialize Response

For deserializing the Elasticsearch response, you can create response models accordingly. Here's how you might structure them:

java
Copy code
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ElasticsearchResponse {
    private Aggregations aggregations;

    // Constructors, getters, and setters
}

class Aggregations {
    @JsonProperty("api_name")
    private List<ApiName> apiName;

    // Constructors, getters, and setters
}

class ApiName {
    private int doc_count_error_upper_bound;
    private int sum_other_doc_count;
    private List<Bucket> buckets;

    // Constructors, getters, and setters
}

class Bucket {
    private String key;
    private int doc_count;
    private StatusRanges status_ranges;

    // Constructors, getters, and setters
}

class StatusRanges {
    private List<RangeBucket> buckets;

    // Constructors, getters, and setters
}

class RangeBucket {
    private String key;
    private double from;
    private double to;
    private int doc_count;

    // Constructors, getters, and setters
}
Step 3: Use Models in Your Service

Now, you can use these models in your service class to create requests and parse responses. Here's an example:

java
Copy code
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticsearchService {

    @Value("${elasticsearch.url}")
    private String elasticsearchUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ElasticsearchService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ElasticsearchResponse getElasticsearchData(ElasticsearchRequest request) {
        // Make the request
        String response = restTemplate.postForObject(elasticsearchUrl, request, String.class);
        // Parse the response
        try {
            return objectMapper.readValue(response, ElasticsearchResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Elasticsearch response: " + e.getMessage(), e);
        }
    }
}
With this approach, your code becomes more elegant, maintainable, and less error-prone. You can easily manipulate the request and response structures without worrying about JSON serialization and deserialization logic.



























vimport org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity; // Add this import
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GetRequestWithBodyExample {

    public static void main(String[] args) {
        try {
            // Create HttpClient instance
            CloseableHttpClient httpClient = HttpClients.createDefault();

            // Create HttpGet instance with URL
            HttpGet httpGet = new HttpGet("https://example.com/api/resource");

            // Add request body (optional)
            String requestBody = "{\"key\": \"value\"}";
            httpGet.setEntity(new StringEntity(requestBody));

            // Execute the request
            CloseableHttpResponse response = httpClient.execute(httpGet);

            // Get the response entity
            HttpEntity entity = response.getEntity();

            // Print the response
            if (entity != null) {
                System.out.println(EntityUtils.toString(entity));
            }

            // Close the response
            response.close();

            // Close the HttpClient
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.13</version> <!-- Or the latest version available -->
</dependency>

