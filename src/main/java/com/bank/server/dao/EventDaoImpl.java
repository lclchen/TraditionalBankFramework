package com.bank.server.dao;

import com.bank.server.model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDaoImpl implements EventDao {
    public Connection conn;

    public EventDaoImpl(Connection connection){
        conn = connection;
    }

    @Override
    public Event getEventByEventId(String eventId) throws Exception {
        String sql = "select * from event where event_id=\"" + eventId + "\";";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<Map> list = convertList(rs);
        if(list == null || list.isEmpty())
            return null;
        else
            return convertEvent(list.get(0));
    }

    @Override
    public List<Event> getEventByAccountId(String accountId) throws Exception {
        String sql = "select * from event where account_id=\"" + accountId + "\";";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<Map> list = convertList(rs);
        if(list == null)
            return new ArrayList<Event>();
        else {
            List<Event> result = new ArrayList<Event>();
            for(Map map:list){
                result.add(convertEvent(map));
            }
            return result;
        }
    }

    @Override
    public boolean insertEvent(Event event) throws Exception {
        String sql = "insert into event (event_id, command_id, committed_time, account_id, currency, revision, type, " +
                "account_transfer_in, account_transfer_out, amount) values(\"" +
                event.getEventId() + "\", \"" +
                event.getCommandId() + "\", \"" +
                event.getCommittedTime() + "\", \"" +
                event.getAccountId() + "\", \"" +
                event.getCurrency() + "\", " +
                event.getRevision() + ", \"" +
                event.getType() + "\", \"" +
                event.getAccountTransferIn() + "\", \"" +
                event.getAccountTRansferOut() + "\", \"" +
                event.getAmount() + "\");";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.executeUpdate();
        return true;
    }

    private static List<Map> convertList(ResultSet rs) throws Exception {
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount(); //Map rowData;

        while (rs.next()) { //rowData = new HashMap(columnCount);
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

    public static Event convertEvent(Map map) throws Exception {
        Event event = new Event();
        event.setEventId((String) map.get("event_id"));
        event.setCommandId((String) map.get("command_id"));
        event.setCommittedTime((String) map.get("committed_time"));
        event.setAccountId((String) map.get("account_id"));
        event.setCurrency((String) map.get("currency"));
        event.setRevision(Integer.valueOf((String) map.get("revision")));

        event.setType((String) map.get("type"));
        event.setAccountTransferIn((String) map.get("account_transfer_in"));
        event.setAccountTRansferOut((String) map.get("account_transfer_out"));
        event.setAmount((String) map.get("amount"));

        return event;
    }
}
