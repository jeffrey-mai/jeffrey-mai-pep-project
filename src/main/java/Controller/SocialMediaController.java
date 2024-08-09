package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("register", this::registerHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::messageHandler);
        app.get("messages", this::allMessagesHandler);
        app.get("messages/{message_id}", this::messageByIDHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("messages/{message_id}", this::patchMessageByIDHandler);
        app.get("accounts/{account_id}/messages", this::userMessagesHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount != null) ctx.json(om.writeValueAsString(addedAccount));
        else ctx.status(400);
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account existingAcc = accountService.logIn(account);
        if(existingAcc != null) ctx.json(om.writeValueAsString(existingAcc));
        else ctx.status(401);
    }

    private void messageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage != null) ctx.json(om.writeValueAsString(addedMessage));
        else ctx.status(400);
    }

    private void allMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    public void messageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageByID = messageService.getMessageByID(id);
        if(messageByID != null) ctx.json(om.writeValueAsString(messageByID));
    }

    public void deleteMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageByID(id);
        if(deletedMessage != null) ctx.json(om.writeValueAsString(deletedMessage));
        else ctx.status(200);
    }

    public void patchMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message requestedMessaage = ctx.bodyAsClass(Message.class);
        Message patchedMessage = messageService.patchMessageByID(id, requestedMessaage.getMessage_text());
        if(patchedMessage != null) ctx.json(om.writeValueAsString(patchedMessage));
        else ctx.status(400);
    }

    public void userMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messageByID = messageService.getUserMessages(account_id);
        if(messageByID != null) ctx.json(om.writeValueAsString(messageByID));
    }
}