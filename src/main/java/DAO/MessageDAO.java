package DAO;

import Util.ConnectionUtil;
import Model.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public Message addMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        Message addedMessage = new Message();
        if(message.getMessage_text().length() <= 0 || message.getMessage_text().length() > 255) return null;
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                String selectSql = "SELECT * FROM message WHERE posted_by = ? AND message_text = ? AND time_posted_epoch = ?;";
                try (PreparedStatement selectPs = connection.prepareStatement(selectSql)) {
                    selectPs.setInt(1, message.getPosted_by());
                    selectPs.setString(2, message.getMessage_text());
                    selectPs.setLong(3, message.getTime_posted_epoch());
                    try (ResultSet rs = selectPs.executeQuery()) {
                        if (rs.next()) {
                            addedMessage = new Message(
                                rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch")
                            );
                        }
                    }
                }
            }
            return addedMessage;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> allMessages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                allMessages.add(message);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return allMessages;
    }
    
    public Message getMessageByID(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageByID(int id){
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMessage = new Message();
        try {
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectPs = connection.prepareStatement(selectSql);
            selectPs.setInt(1, id);
            ResultSet rs = selectPs.executeQuery();
            if (rs.next()) {
                deletedMessage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
            else return null;

            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return deletedMessage;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message patchMessageByID(int id, String requestedMessage){
        Connection connection = ConnectionUtil.getConnection();
        Message updatedMessage = null;
        if(requestedMessage.length() == 0) return null;
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, requestedMessage);
            ps.setInt(2, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                String selectSql = "SELECT * FROM message WHERE message_id = ?";
                try (PreparedStatement selectPs = connection.prepareStatement(selectSql)) {
                    selectPs.setInt(1, id);
                    try (ResultSet rs = selectPs.executeQuery()) {
                        if (rs.next()) {
                            updatedMessage = new Message(
                                rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch")
                            );
                        }
                    }
                }
            }
            return updatedMessage;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getUserMessages(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> userMessages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                userMessages.add(message);
            }
            return userMessages;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
