package com.bank.client.system;

import com.bank.server.model.Command;
import com.bank.server.util.Logger;
import com.bank.server.system.IServerSystem;

import java.math.BigDecimal;
import java.rmi.Naming;
import java.util.UUID;

public class ClientSystem {
    private String url = "rmi://localhost:9999/";
    private String name = "RemoteServerSystem";
    public IServerSystem system;

    public boolean init(){
        try {
            system = (IServerSystem) Naming.lookup(url + name);
            return true;
        }catch(Exception e) {
            Logger.logger.error(e);
            return false;
        }
    }

    public boolean insertAccount(UUID uuid, String username, BigDecimal balance, String currency, boolean activated,
                       int revision){
        try {
            system.insertAccount(uuid, username, balance, currency, activated, revision);
            return true;
        }catch(Exception e) {
            Logger.logger.error(e);
            return false;
        }
    }

    public boolean sendCommand(Command cmd){
        try {
            system.handleCommand(cmd);
            return true;
        }catch(Exception e) {
            Logger.logger.error(e);
            return false;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
