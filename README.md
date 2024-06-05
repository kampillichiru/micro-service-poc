
# Use an official openjdk runtime as a parent image
FROM openjdk:11-jre-slim

# Set environment variables for Chrome and ChromeDriver versions
ENV CHROME_VERSION="google-chrome-stable"
ENV CHROMEDRIVER_VERSION="114.0.5735.90"

# Install dependencies and Chrome
RUN apt-get update && \
    apt-get install -y wget gnupg unzip curl && \
    curl -sSL https://dl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list' && \
    apt-get update && \
    apt-get install -y ${CHROME_VERSION} && \
    apt-get clean

# Install ChromeDriver
RUN wget -q -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/${CHROMEDRIVER_VERSION}/chromedriver_linux64.zip && \
    unzip /tmp/chromedriver.zip -d /usr/local/bin/ && \
    rm /tmp/chromedriver.zip

# Add user for running Selenium
RUN useradd -m selenium && \
    mkdir -p /home/selenium/Downloads && \
    chown -R selenium:selenium /home/selenium

# Set the user to "selenium"
USER selenium

# Set the working directory
WORKDIR /home/selenium

# Expose the port (if your Spring Boot application runs on a specific port)
EXPOSE 8080

# Copy the Spring Boot application jar to the container
COPY target/your-spring-boot-app.jar /home/selenium/app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/home/selenium/app.jar"]






import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {

    @Bean
    public WebDriver webDriver() {
        // Set system property for ChromeDriver
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run Chrome in headless mode
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        return new ChromeDriver(options);
    }
}
