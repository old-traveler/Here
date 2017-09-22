package com.here.util;

import com.here.bean.Account;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

/**
 * Created by hyc on 2017/7/2 22:56
 */

public class AccountUtil {

    public static void addAccount(String name,String username
            ,String password,String imageUrl,boolean isThird){
        Connector.getDatabase();
        for (Account account : DataSupport.findAll(Account.class)) {
            if (account.getUsername().equals(username)){
                account.delete();
                break;
            }
        }
        Account account=new Account();
        account.setName(name);
        account.setUsername(username);
        account.setPassword(password);
        account.setImageUrl(imageUrl);
        account.setThird(isThird);
        account.save();
    }



}
