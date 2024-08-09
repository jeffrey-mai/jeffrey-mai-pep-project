package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO accDAO){
        this.messageDAO = accDAO;
    }

    public Message addMessage(Message message){
        return this.messageDAO.addMessage(message);
    }

    public List<Message> getAllMessages(){
        return this.messageDAO.getAllMessages();
    }

    public Message getMessageByID(int id){
        return this.messageDAO.getMessageByID(id);
    }

    public Message deleteMessageByID(int id){
        return this.messageDAO.deleteMessageByID(id);
    }

    public Message patchMessageByID(int id, String requestedMessage){
        return this.messageDAO.patchMessageByID(id, requestedMessage);
    }

    public List<Message> getUserMessages(int account_id){
        return this.messageDAO.getUserMessages(account_id);
    }
}
