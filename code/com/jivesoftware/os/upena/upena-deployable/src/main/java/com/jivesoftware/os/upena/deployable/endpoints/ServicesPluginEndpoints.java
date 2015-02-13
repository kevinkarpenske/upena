package com.jivesoftware.os.upena.deployable.endpoints;

import com.google.common.base.Optional;
import com.jivesoftware.os.upena.deployable.region.ServicesPluginRegion;
import com.jivesoftware.os.upena.deployable.region.ServicesPluginRegion.ServicesPluginRegionInput;
import com.jivesoftware.os.upena.deployable.soy.SoyService;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Singleton
@Path("/services")
public class ServicesPluginEndpoints {

    private final SoyService soyService;
    private final ServicesPluginRegion pluginRegion;

    public ServicesPluginEndpoints(@Context SoyService soyService, @Context ServicesPluginRegion pluginRegion) {
        this.soyService = soyService;
        this.pluginRegion = pluginRegion;
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public Response services() {
        String rendered = soyService.renderPlugin(pluginRegion,
            Optional.of(new ServicesPluginRegionInput("", "", "", "")));
        return Response.ok(rendered).build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response action(@FormParam("key") @DefaultValue("") String key,
        @FormParam("name") @DefaultValue("") String name,
        @FormParam("description") @DefaultValue("") String description,
        @FormParam("action") @DefaultValue("") String action) {
        String rendered = soyService.renderPlugin(pluginRegion,
            Optional.of(new ServicesPluginRegionInput(key, name, description, action)));
        return Response.ok(rendered).build();
    }

}
