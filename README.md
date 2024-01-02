import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import okio.Buffer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MultipartInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Assuming 'payload' is the part name for the file
        RequestBody requestBody = originalRequest.body();
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);

        // Extract relevant parts using custom logic
        String requestBodyString = buffer.readUtf8();
        try {
            extractMultipart(requestBodyString);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // Proceed with the original request
        return chain.proceed(originalRequest);
    }

    private void extractMultipart(String text) throws MessagingException, IOException {
        // Extracting parts from the multipart content
        MimeMultipart mimeMultipart = new MimeMultipart(new InputStreamDataSource(text));
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            // Process each part as needed
            // You might check content type, extract binary data, etc.
            // Example: Part part = mimeMultipart.getBodyPart(i);
            //          String contentType = part.getContentType();
            //          InputStream inputStream = part.getInputStream();
        }
    }

    // A custom DataSource to convert the string into an InputStream for MimeMultipart
    private static class InputStreamDataSource extends javax.mail.util.ByteArrayDataSource {
        public InputStreamDataSource(String data) throws IOException {
            super(data.getBytes(StandardCharsets.UTF_8), "multipart/form-data");
        }
    }
}
