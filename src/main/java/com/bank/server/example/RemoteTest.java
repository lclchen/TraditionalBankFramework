package com.bank.server.example;

import com.bank.server.system.ServerSystem;
import com.bank.server.util.Logger;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RemoteTest {
    public static void main(String[] args){
        try{
            ServerSystem server = new ServerSystem();
            server.setJdbcDriver("com.mysql.jdbc.Driver");
            server.setDbUrl("jdbc:mysql://localhost:3306/banksystem");
            server.setDbUsername("root");
            server.setDbPassword("sdsd");
            //Could change connection-pool settings: system.pool.XXXX();
            server.init();

            //Could change rmi port and url
            // initial the system
            LocateRegistry.createRegistry(server.getRemotePort());
            Naming.bind(server.getRemoteUrl(), server);
            Logger.logger.info("RemoteServerSystem: Start binding");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
