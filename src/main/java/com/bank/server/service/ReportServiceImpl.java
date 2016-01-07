package com.bank.server.service;

import com.bank.server.dao.*;
import com.bank.server.model.*;
import com.bank.server.util.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.UUID;
import java.util.Date;

public class ReportServiceImpl {
    public Connection conn;

    private AccountDao accountDao;
    private EventDao eventDao;

    public ReportServiceImpl(Connection connection){
        this.conn = connection;
        accountDao = new AccountDaoImpl(conn);
        eventDao = new EventDaoImpl(conn);
    }

    public boolean handleCommand(Command cmd){
        switch (cmd.getType()){
            case Command.TYPE_DEPOSIT:
                return handleDepositCommand(cmd);
            case Command.TYPE_WITHDRAW:
                return handleWithdrawCommand(cmd);
            case Command.TYPE_TRANSFER:
                return handleTransferCommand(cmd);
            default:
                Logger.logger.error("handlerCommand(): receive wrong type of command");
        }
        return false;
    }

    public boolean insertAccount(UUID uuid, String username, BigDecimal balance, String currency, boolean activated,
                                 int revision){
        try{
            conn.setAutoCommit(false);

            Account account = new Account();
            account.setUuid(uuid.toString());
            account.setUsername(username);
            account.setBalance(balance.toString());
            account.setCurrency(currency);
            account.setActivated(new Boolean(activated).toString());
            account.setRevision(revision);
            accountDao.createAccount(account);

            conn.commit();
        }catch(Exception e){
            try {
                conn.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean handleDepositCommand(Command cmd){
        try{
            conn.setAutoCommit(false);

            Account account = accountDao.getAccountById(cmd.getAccountId(), true);
            // check if account exists and if currency match
            if(!account.getCurrency().equals(cmd.getCurrency())){
                Logger.logger.error("handleDepositCommand id:" + cmd.getCommandId() + " currency not match");
                throw new Exception();
            }
            account.setBalance((new BigDecimal(account.getBalance()).add(new BigDecimal(cmd.getAmount()))).toString());
            account.setRevision(account.getRevision() + 1);
            accountDao.updateAccount(account.getUuid(), account);

            Event event = new Event();
            event.setEventId(UUID.randomUUID().toString());
            event.setCommandId(cmd.getCommandId());
            event.setCommittedTime(new Date().toString());
            event.setAccountId(cmd.getAccountId());
            event.setCurrency(cmd.getCurrency());
            event.setRevision(account.getRevision());
            event.setType(Event.TYPE_DEPOSIT);
            event.setAccountTransferIn(cmd.getAccountTransferIn());
            event.setAccountTRansferOut(cmd.getAccountTransferOut());
            event.setAmount(cmd.getAmount());
            eventDao.insertEvent(event);

            conn.commit();
        }catch(Exception e){
            try {
                conn.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean handleWithdrawCommand(Command cmd){
        try{
            conn.setAutoCommit(false);

            Account account = accountDao.getAccountById(cmd.getAccountId(), true);
            // check if account exists and if currency match
            if(account.getCurrency() != cmd.getCurrency()){
                Logger.logger.error("handleWithdrawCommand id:" + cmd.getCommandId() + " currency not match");
                throw new Exception();
            }
            account.setBalance((new BigDecimal(account.getBalance()).subtract(new BigDecimal(cmd.getAmount()))).toString());
            account.setRevision(account.getRevision() + 1);
            accountDao.updateAccount(account.getUuid(), account);

            Event event = new Event();
            event.setEventId(UUID.randomUUID().toString());
            event.setCommandId(cmd.getCommandId());
            event.setCommittedTime(new Date().toString());
            event.setAccountId(cmd.getAccountId());
            event.setCurrency(cmd.getCurrency());
            event.setRevision(account.getRevision());
            event.setType(Event.TYPE_WITHDRAW);
            event.setAccountTransferIn(cmd.getAccountTransferIn());
            event.setAccountTRansferOut(cmd.getAccountTransferOut());
            event.setAmount(cmd.getAmount());
            eventDao.insertEvent(event);

            conn.commit();
        }catch(Exception e){
            try {
                conn.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean handleTransferCommand(Command cmd){
        try{
            conn.setAutoCommit(false);

            Account accountIn = accountDao.getAccountById(cmd.getAccountTransferIn(), true);
            Account accountOut = accountDao.getAccountById(cmd.getAccountTransferOut(), true);
            // check if account exists and if currency match
            if(accountIn.getCurrency() != cmd.getCurrency()){
                Logger.logger.error("handleDepositCommand id:" + cmd.getCommandId() + " currency not match");
                throw new Exception();
            }
            if(accountOut.getCurrency() != cmd.getCurrency()){
                Logger.logger.error("handleDepositCommand id:" + cmd.getCommandId() + " currency not match");
                throw new Exception();
            }

            accountIn.setBalance((new BigDecimal(accountIn.getBalance()).add(new BigDecimal(cmd.getAmount()))).toString());
            accountOut.setBalance((new BigDecimal(accountOut.getBalance()).subtract(new BigDecimal(cmd.getAmount()))).toString());
            accountIn.setRevision(accountIn.getRevision() + 1);
            accountOut.setRevision(accountOut.getRevision() + 1);
            accountDao.updateAccount(accountIn.getUuid(), accountIn);
            accountDao.updateAccount(accountOut.getUuid(), accountOut);

            Event eventIn = new Event();
            eventIn.setEventId(UUID.randomUUID().toString());
            eventIn.setCommandId(cmd.getCommandId());
            eventIn.setCommittedTime(new Date().toString());
            eventIn.setAccountId(cmd.getAccountTransferIn());
            eventIn.setCurrency(cmd.getCurrency());
            eventIn.setRevision(accountIn.getRevision());
            eventIn.setType(Event.TYPE_TRANSFER_IN);
            eventIn.setAccountTransferIn(cmd.getAccountTransferIn());
            eventIn.setAccountTRansferOut(cmd.getAccountTransferOut());
            eventIn.setAmount(cmd.getAmount());
            eventDao.insertEvent(eventIn);

            Event eventOut = new Event();
            eventOut.setEventId(UUID.randomUUID().toString());
            eventOut.setCommandId(cmd.getCommandId());
            eventOut.setCommittedTime(new Date().toString());
            eventOut.setAccountId(cmd.getAccountTransferOut());
            eventOut.setCurrency(cmd.getCurrency());
            eventOut.setRevision(accountOut.getRevision());
            eventOut.setType(Event.TYPE_TRANSFER_OUT);
            eventOut.setAccountTransferIn(cmd.getAccountTransferIn());
            eventOut.setAccountTRansferOut(cmd.getAccountTransferOut());
            eventOut.setAmount(cmd.getAmount());
            eventDao.insertEvent(eventOut);

            conn.commit();
        }catch(Exception e){
            try {
                conn.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
