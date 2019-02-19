package cc.coopersoft.house.participant.controller;

import javax.ws.rs.*;

@Path("/")
public class PersonCardReader {


    @POST
    @Path("/person")
    @Produces({ "application/json" })
    public String personCardRead(@PathParam("key") String key , @FormParam("cer") String cer){

        return null;
    }
}
