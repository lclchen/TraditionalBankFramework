package com.bank.server.dao;

import com.bank.server.model.Event;

import java.util.List;

public interface EventDao {
    public Event getEventByEventId(String eventId) throws Exception;

    public List<Event> getEventByAccountId(String accountId) throws Exception;

    public boolean insertEvent(Event event) throws Exception;
}
