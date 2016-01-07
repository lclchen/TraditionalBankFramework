package com.bank.server.util;

import com.bank.server.model.Command;

import java.math.BigDecimal;
import java.util.UUID;

public class CommandFactory {
    public static Command getCommand(UUID commandId, UUID accountId, String currency, String type,
                                     UUID accountTransferIn, UUID accountTransferOut, BigDecimal amount){
        Command cmd = new Command();
        cmd.setCommandId(commandId.toString());
        cmd.setAccountId(accountId.toString());
        cmd.setCurrency(currency);
        cmd.setType(type);

        if(accountTransferIn != null)
            cmd.setAccountTransferIn(accountTransferIn.toString());
        else
            cmd.setAccountTransferIn("");

        if(accountTransferOut != null)
            cmd.setAccountTransferOut(accountTransferOut.toString());
        else
            cmd.setAccountTransferOut("");

        cmd.setAmount(amount.toString());
        return cmd;
    }
}
