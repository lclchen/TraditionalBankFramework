package com.bank.client.example;

import com.bank.client.system.ClientSystem;
import com.bank.server.model.Command;
import com.bank.server.util.CommandFactory;
import com.bank.server.util.Logger;

import java.math.BigDecimal;
import java.util.UUID;

public class RemoteTest {
    public static void main(String[] args) throws Exception{
        ClientSystem client = new ClientSystem();

        while(!client.init()){
            Thread.sleep(1000);
            Logger.logger.warn("fail to register RMI service");
        }

        //----------------
        //   start test
        //----------------
        UUID uuid = UUID.randomUUID();
        client.insertAccount(uuid, "Toddy", new BigDecimal("100"), "RMB", true, 1);
        Thread.sleep(1000);

        while(true) {
            Command cmd = CommandFactory.getCommand(UUID.randomUUID(), uuid, "RMB", Command.TYPE_DEPOSIT, null, null, new BigDecimal(10));
            client.sendCommand(cmd);
            Thread.sleep(1);
        }
    }
}
