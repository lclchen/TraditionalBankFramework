package com.bank.server.example;

import com.bank.server.model.Command;
import com.bank.server.system.*;
import com.bank.server.util.CommandFactory;

import java.math.BigDecimal;
import java.util.UUID;

public class LocalTest {
    public static void main(String[] args) throws Exception{
        ServerSystem server = new ServerSystem();
        server.setJdbcDriver("com.mysql.jdbc.Driver");
        server.setDbUrl("jdbc:mysql://localhost:3306/banksystem");
        server.setDbUsername("root");
        server.setDbPassword("sdsd");
        //Could change connection-pool settings: system.pool.XXXX();
        server.init();


        //----------------
        //   start test
        //----------------
        UUID uuid = UUID.randomUUID();
        server.insertAccount(uuid, "Toddy", new BigDecimal("100"), "RMB", true, 1);
        Thread.sleep(1000);

        while(true) {
            Command cmd = CommandFactory.getCommand(UUID.randomUUID(), uuid, "RMB", Command.TYPE_DEPOSIT, null, null, new BigDecimal(10));
            server.handleCommand(cmd);
            Thread.sleep(1);
        }
    }
}
