package com.recursivechaos.rcbot.plugins.stoopsnoop.objects;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class NickFilterGroup {
	@Id
	@GeneratedValue
	int			id;
	String 		nick;
	String 		nickFilterName;
	String 		channel;
	Timestamp 	start;
	Timestamp 	end;
	String 		note;
	
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getNickFilterName() {
		return nickFilterName;
	}
	public void setNickFilterName(String nickFilterName) {
		this.nickFilterName = nickFilterName;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Timestamp getStart() {
		return start;
	}
	public void setStart(Timestamp start) {
		this.start = start;
	}
	public Timestamp getEnd() {
		return end;
	}
	public void setEnd(Timestamp end) {
		this.end = end;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
