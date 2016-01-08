package com.bank.server.example;

import com.bank.server.model.Command;
import com.bank.server.system.*;
import com.bank.server.util.CommandFactory;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
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
        // insert accounts
        ArrayList<UUID> account_ids = new ArrayList<UUID>();
        for(int i=0; i<1000; i++) {
            UUID uuid = UUID.randomUUID();
            server.insertAccount(uuid, "Toddy", new BigDecimal("10000"), "RMB", true, 1);

            account_ids.add(uuid);
        }

        // wait for insertion
        System.out.print("Enter \'1\' to continue.");
        Scanner sc = new Scanner(System.in);
        while(sc.nextInt() != 1) {
            Thread.sleep(1000);
            System.out.print("Enter \'1\' to continue.");
        }

        // start to handle commands
        int i = 0;
        while(true) {
            Command cmd = CommandFactory.getCommand(UUID.randomUUID(), account_ids.get(i), "RMB", Command.TYPE_DEPOSIT, null, null, new BigDecimal(10));
            server.handleCommand(cmd);
            //Thread.sleep(1);
            i++;
            if(i == 10000)
                i = 0;
        }
    }
}
