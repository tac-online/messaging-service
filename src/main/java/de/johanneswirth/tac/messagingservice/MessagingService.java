package de.johanneswirth.tac.messagingservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Metered;
import de.johanneswirth.tac.common.Message;
import de.johanneswirth.tac.common.Status;

@Path("messaging")
@Metered
public class MessagingService {

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response sendMessage(Message message) {
        Status status = Websocket.broadcast(message.getRecipients(), new ClientMessage(message));

        return Response.ok(status).build();
    }
}
