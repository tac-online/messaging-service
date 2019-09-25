package de.johanneswirth.tac.messagingservice;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import de.johanneswirth.tac.common.Message;

@Path("messaging")
public class MessagingService {

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    @Timed
    @ExceptionMetered
    public void sendMessage(@NotNull @Valid Message message) {
        Websocket.broadcast(message.getRecipients(), new ClientMessage(message));
    }
}
