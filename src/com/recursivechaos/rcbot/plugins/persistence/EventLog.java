package com.recursivechaos.rcbot.plugins.persistence;

/**
 * EventLog holds all fields from a bot event
 * 
 * @author Andrew Bell
 * 
 */
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class EventLog {
	public enum TYPE {
		MESSAGE, ACTION, PING
	}

	@Id
	@GeneratedValue
	int eventID;
	Timestamp sqltimestamp;
	String nick;
	String message;
	String channel;
	String bot;

	String eventType;;

	public EventLog() {
	}

	public EventLog(long timestamp, String nick2, String message2, String name,
			String nick3, String type) {
		// TODO: Check time format for errors
		this.sqltimestamp = new Timestamp(timestamp);
		this.nick = nick2;
		this.message = message2;
		this.channel = name;
		this.bot = nick3;
		this.eventType = type;
	}

	public String getBot() {
		return bot;
	}

	public String getChannel() {
		return channel;
	}

	public int getEventID() {
		return eventID;
	}

	public String getEventType() {
		return eventType;
	}

	public String getMessage() {
		return message;
	}

	public String getNick() {
		return nick;
	}

	public Timestamp getSqltimestamp() {
		return sqltimestamp;
	}

	public void setBot(String bot) {
		this.bot = bot;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public void setSqltimestamp(Timestamp sqltimestamp) {
		this.sqltimestamp = sqltimestamp;
	}
}
