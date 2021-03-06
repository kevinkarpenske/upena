package com.jivesoftware.os.upena.deployable.endpoints.ui;

import com.jivesoftware.os.upena.deployable.ShiroRequestHelper;
import com.jivesoftware.os.upena.deployable.region.HealthPluginRegion;
import com.jivesoftware.os.upena.deployable.region.HealthPluginRegion.HealthPluginRegionInput;
import com.jivesoftware.os.upena.deployable.soy.SoyService;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Singleton
@Path("/ui/health")
public class HealthPluginEndpoints {

    private final ShiroRequestHelper shiroRequestHelper;

    private final SoyService soyService;
    private final HealthPluginRegion pluginRegion;

    public HealthPluginEndpoints(@Context ShiroRequestHelper shiroRequestHelper,
        @Context SoyService soyService,
        @Context HealthPluginRegion pluginRegion) {

        this.shiroRequestHelper = shiroRequestHelper;
        this.soyService = soyService;
        this.pluginRegion = pluginRegion;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response filter(@Context HttpServletRequest httpRequest,
        @QueryParam("datacenter") @DefaultValue("") String datacenter,
        @QueryParam("rack") @DefaultValue("") String rack,
        @QueryParam("cluster") @DefaultValue("") String cluster,
        @QueryParam("host") @DefaultValue("") String host,
        @QueryParam("service") @DefaultValue("") String service) {

        return shiroRequestHelper.call("health", (csrfToken) -> {
            String rendered = soyService.renderPlugin(httpRequest.getRemoteUser(), csrfToken, pluginRegion,
                new HealthPluginRegionInput(datacenter, rack, cluster, host, service));
            return Response.ok(rendered);
        });
    }

    @GET
    @Path("/live")
    @Produces(MediaType.APPLICATION_JSON)
    public Response live(@Context HttpServletRequest httpRequest,
        @QueryParam("datacenter") @DefaultValue("") String datacenter,
        @QueryParam("rack") @DefaultValue("") String rack,
        @QueryParam("cluster") @DefaultValue("") String cluster,
        @QueryParam("host") @DefaultValue("") String host,
        @QueryParam("service") @DefaultValue("") String service) {

        return shiroRequestHelper.call("health/live",
            csrfToken -> Response.ok(pluginRegion.renderLive())
        );
    }

}
