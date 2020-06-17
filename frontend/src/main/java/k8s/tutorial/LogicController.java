package k8s.tutorial;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class LogicController implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogicController.class);

    @Inject
    @ConfigProperty(name = "BACKEND_URL", defaultValue = "http://localhost:8081")
    private String backend;

    private String message;

    public String call() {
        LOGGER.info("Calling callback!");
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(backend);
            request.addHeader("User-Agent", "Chrome");

            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                this.message = EntityUtils.toString(entity);
            } else {
                this.message = "No response!";
            }
        } catch (IOException ioe) {
            LOGGER.error("Error calling backend!", ioe);
            this.message = String.format("ERROR: %s", ioe.getMessage());
        }
        return "/index.xhtml";
    }

    public String getMessage() {
        return message;
    }
}