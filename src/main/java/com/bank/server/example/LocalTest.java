package com.bank.server.example;

import com.bank.server.model.Command;
import com.bank.server.system.*;
import com.bank.server.util.CommandFactory;

import java.math.BigDecimal;
import java.util.UUID;

public class LocalTest {
    public static void main(String[] args) throws Exception{
        ServerSystem system = new ServerSystem();
        system.setJdbcDriver("com.mysql.jdbc.Driver");
        system.setDbUrl("jdbc:mysql://localhost:3306/banksystem");
        system.setDbUsername("root");
        system.setDbPassword("sdsd");
        //Could change connection-pool settings: system.pool.XXXX();
        system.init();

        UUID uuid = UUID.randomUUID();
        system.insertAccount(uuid, "Toddy", new BigDecimal("100"), "RMB", true, 1);
        Thread.sleep(1000);

        Command cmd = CommandFactory.getCommand(UUID.randomUUID(), uuid, "RMB", Command.TYPE_DEPOSIT, null, null, new BigDecimal(10));
        system.handleCommand(cmd);
    }
}
