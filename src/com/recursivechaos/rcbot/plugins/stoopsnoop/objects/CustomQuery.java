package com.recursivechaos.rcbot.plugins.stoopsnoop.objects;

import java.sql.Timestamp;

import org.pircbotx.hooks.events.MessageEvent;

import com.recursivechaos.rcbot.bot.object.MyPircBotX;
import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.QueryDAOImpl;
import com.recursivechaos.rcbot.plugins.stoopsnoop.query.QueryBO;
import com.recursivechaos.rcbot.plugins.stoopsnoop.query.QueryDAO;

public class CustomQuery {
	public enum Report {TRENDING, WORDCOUNT, TOPUSER, LASTMENTION}
	String nick;
	Report report;
	String channel;
	MessageEvent<MyPircBotX> event;
	Timestamp start;
	Timestamp end;
	int timeQuantity;
	String timePeriod;
	int resultsQuantity;
	
	
	public CustomQuery(MessageEvent<MyPircBotX> event) {
		QueryBO helper = new QueryBO();
		// get initial settings
		this.channel 	= event.getChannel().getName();
		this.event		= event;
		// read message
		String input = event.getMessage();
		// sanitize input
		input = sanitize(input); 
		// create string array
		String[] inArray = input.split(" ");
		// [0] "!query" [1] REPORT
		// parse input from array
		// find report
		report = Report.valueOf(inArray[1].toUpperCase());
		// find nick
		for (int i= 2;i<inArray.length;i++){
			String test = inArray[i].trim();
			// Get nick
			if (test.equals("-n")){
				nick = inArray[i+1];
			}
			// Get time
			if (test.equals("-t")){
				this.timeQuantity = Integer.parseInt(inArray[i+1]);
				this.timePeriod = inArray[i+2];
				this.start = helper.getPeriodsAgo(timeQuantity,timePeriod,event);
			}
			// Time end defaults to "now"
			this.end = helper.getNow(event);
			// Get quantity
			if (test.equals("-q")){
				this.resultsQuantity = Integer.parseInt(inArray[i+1]);
			}
		}
		// verify input
		verify();
	}	
	
	public CustomQuery(MessageEvent<MyPircBotX> event, Report report) {
		this.report 	= report;
		this.channel 	= event.getChannel().getName();
		this.event		= event;
		this.resultsQuantity = 5;
		this.timePeriod = "hour";
		this.timeQuantity = 24;
		
	}
	
	private void verify() {
		QueryDAO qdao = new QueryDAOImpl();
		if(!qdao.verifyUserHistory(this)){
			this.nick = null;
		}
	}
	private String sanitize(String input) {
		return input.toLowerCase().trim();
	}
	public String getUser() {
		return nick;
	}
	public void setUser(String user) {
		this.nick = user;
	}
	public Report getReport() {
		return report;
	}
	public void setReport(Report report) {
		this.report = report;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public MessageEvent<MyPircBotX> getEvent() {
		return event;
	}
	public void setEvent(MessageEvent<MyPircBotX> event) {
		this.event = event;
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

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getTimeQuantity() {
		return timeQuantity;
	}

	public void setTimeQuantity(int timeQuantity) {
		this.timeQuantity = timeQuantity;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public int getResultsQuantity() {
		return resultsQuantity;
	}

	public void setResultsQuantity(int resultsQuantity) {
		this.resultsQuantity = resultsQuantity;
	}
	
}
