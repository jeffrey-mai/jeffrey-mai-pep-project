package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accDAO){
        this.accountDAO = accDAO;
    }

    public Account addAccount(Account account){
        return this.accountDAO.addAccount(account);
    }

    public Account logIn(Account account){
        return this.accountDAO.logIn(account);
    }
}