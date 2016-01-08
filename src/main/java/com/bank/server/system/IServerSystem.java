package com.bank.server.system;

import com.bank.server.model.Command;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface IServerSystem extends Remote {
    public boolean insertAccount(UUID uuid, String username, BigDecimal balance, String currency, boolean activated,
                                 int revision) throws RemoteException;

    public boolean handleCommand(Command cmd) throws RemoteException;
}
