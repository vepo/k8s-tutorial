package k8s.tutorial.call;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
@Path("/call")
public class CallController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallController.class);

    private String uid;

    @PostConstruct
    public void setup() {
        uid = UUID.randomUUID().toString();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getWait(@QueryParam("wait") @DefaultValue("500") Integer milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ie) {
            LOGGER.error("Someone wake me up!", ie);
            return "Someone wake me up!";
        }
        return "I (" + uid + ") slept " + milliseconds + "ms";
    }
}