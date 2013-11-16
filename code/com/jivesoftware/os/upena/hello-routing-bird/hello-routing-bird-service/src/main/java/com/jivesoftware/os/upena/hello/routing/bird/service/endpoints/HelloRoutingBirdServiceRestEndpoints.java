package com.jivesoftware.os.upena.hello.routing.bird.service.endpoints;

import com.jivesoftware.os.jive.utils.jaxrs.util.ResponseHelper;
import com.jivesoftware.os.jive.utils.logger.MetricLogger;
import com.jivesoftware.os.jive.utils.logger.MetricLoggerFactory;
import com.jivesoftware.os.upena.hello.routing.bird.service.HelloRoutingBirdService;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class HelloRoutingBirdServiceRestEndpoints {

    private static final MetricLogger log = MetricLoggerFactory.getLogger();
    @Context
    private HelloRoutingBirdService service;

    @GET
    @Path("/hello")
    public Response hello() {
        log.debug("Hello: " + service.greetings());
        return Response.ok(service.greetings() + " " + System.identityHashCode(Runtime.getRuntime()) + "\n", MediaType.TEXT_PLAIN).build();
    }

    @GET
    @Path("/echo")
    public Response echo(@QueryParam("tenantId") @DefaultValue("defaultTenantId") String tenantId,
            @QueryParam("message") @DefaultValue("echo") String message,
            @QueryParam("echos") @DefaultValue("3") int echos) {
        try {
            String echo = service.echo(tenantId, message, echos);
            return Response.ok("Echo: " + echo + " " + System.identityHashCode(Runtime.getRuntime()), MediaType.TEXT_PLAIN).build();
        } catch (Exception x) {
            return ResponseHelper.INSTANCE.errorResponse("Failed to echo: " + tenantId + " " + message + " " + echos, x);
        }
    }
}
