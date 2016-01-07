package com.bank.server.example;

import com.bank.server.system.ServerSystem;
import com.bank.server.util.Logger;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RemoteTest {
    public static void main(String[] args){
        try{
            ServerSystem system = new ServerSystem();
            system.setJdbcDriver("com.mysql.jdbc.Driver");
            system.setDbUrl("jdbc:mysql://localhost:3306/banksystem");
            system.setDbUsername("root");
            system.setDbPassword("sdsd");
            //Could change connection-pool settings: system.pool.XXXX();
            system.init();

            LocateRegistry.createRegistry(9999);
            Naming.bind("rmi://localhost:9999/RemoteServerSystem", system);
            Logger.logger.info("RemoteServerSystem: Start binding");

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
