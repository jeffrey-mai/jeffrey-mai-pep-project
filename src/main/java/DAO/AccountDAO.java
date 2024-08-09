package DAO;

import Util.ConnectionUtil;
import Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDAO {
    public Account addAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        Account addedAccount = new Account();
        if(account.getUsername().length() == 0 || account.getPassword().length() < 4) return null;
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?,?);";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                String selectSql = "SELECT * FROM account WHERE username = ? AND password = ?;";
                try (PreparedStatement selectPs = connection.prepareStatement(selectSql)) {
                    selectPs.setString(1, account.getUsername());
                    selectPs.setString(2, account.getPassword());
                    try (ResultSet rs = selectPs.executeQuery()) {
                        if (rs.next()) {
                            addedAccount = new Account(
                                rs.getInt("account_id"),
                                rs.getString("username"),
                                rs.getString("password")
                            );
                        }
                    }
                }
            }
            return addedAccount;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account logIn(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account loggedInAccount = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return loggedInAccount;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
