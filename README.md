import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MyInterceptorTest {

    private MockWebServer mockWebServer;
    private MyInterceptor myInterceptor;

    @Before
    public void setUp() {
        mockWebServer = new MockWebServer();
        myInterceptor = new MyInterceptor();
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testInterceptor() throws IOException {
        // Start the mock server
        mockWebServer.start();

        // Enqueue a mock response for the interceptor to handle
        mockWebServer.enqueue(new MockResponse().setBody("Mock Response"));

        // Create a request using the mock server's URL
        Request request = new Request.Builder().url(mockWebServer.url("/")).build();

        // Create an interceptor chain with a mock request
        okhttp3.Interceptor.Chain chain = new okhttp3.Interceptor.Chain() {
            @Override
            public Request request() {
                return request;
            }

            @Override
            public Response proceed(Request request) throws IOException {
                // This is where the interceptor's logic is executed
                return new Response.Builder()
                        .request(request)
                        .protocol(okhttp3.Protocol.HTTP_1_1)
                        .code(200)
                        .message("OK")
                        .body(okhttp3.ResponseBody.create("Mock Response", null))
                        .build();
            }

            @Override
            public Interceptor.Chain withConnectTimeout(int timeout, TimeUnit unit) {
                return this;
            }

            @Override
            public Interceptor.Chain withReadTimeout(int timeout, TimeUnit unit) {
                return this;
            }

            @Override
            public Interceptor.Chain withWriteTimeout(int timeout, TimeUnit unit) {
                return this;
            }
        };

        // Call the intercept method and get the response
        Response response = myInterceptor.intercept(chain);

        // Assert that the response is not null
        assertNotNull(response);

        // Assert that the response body is as expected
        assertEquals("Mock Response", response.body().string());

        // Assert that the interceptor made the expected request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("/", recordedRequest.getPath());
    }
}
