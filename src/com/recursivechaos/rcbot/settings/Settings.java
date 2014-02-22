package com.recursivechaos.rcbot.settings;

/**
 * Settings is the object representing global bot settings.
 * 
 * @author Andrew Bell
 */
import javax.persistence.Entity;

@Entity
public class Settings {
	String nick = "";
	String password = "";
	String server = "";
	String channel = "";
	String admin = "";
	String calendarUrl = "";
	Boolean redditPreview = false;
	Boolean newUserGreeting = false;
	Boolean calendar = false;
	Boolean dice = false;
	Boolean catfacts = false;
	Boolean dadjokes = false;
	int dadjokeChance = 0;
	Boolean logger = false;

	public Settings() {
	}

	public Settings(Settings inSettings) {
		this.nick = inSettings.getNick();
		this.password = inSettings.getPassword();
		this.server = inSettings.getServer();
		this.channel = inSettings.getChannel();
		this.admin = inSettings.getAdmin();
	}

	public String getAdmin() {
		return admin;
	}

	public Boolean getCalendar() {
		return calendar;
	}

	public String getCalendarUrl() {
		return calendarUrl;
	}

	public Boolean getCatfacts() {
		return catfacts;
	}

	public String getChannel() {
		return channel;
	}

	public int getDadjokeChance() {
		return dadjokeChance;
	}

	public Boolean getDadjokes() {
		return dadjokes;
	}

	public Boolean getDice() {
		return dice;
	}

	public Boolean getLogger() {
		return logger;
	}

	public Boolean getNewUserGreeting() {
		return newUserGreeting;
	}

	public String getNick() {
		return nick;
	}

	public String getPassword() {
		return password;
	}

	public Boolean getRedditPreview() {
		return redditPreview;
	}

	public String getServer() {
		return server;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public void setCalendar(Boolean calendar) {
		this.calendar = calendar;
	}

	public void setCalendarUrl(String calendarUrl) {
		this.calendarUrl = calendarUrl;
	}

	public void setCatfacts(Boolean catfacts) {
		this.catfacts = catfacts;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setDadjokeChance(int dadjokeChance) {
		this.dadjokeChance = dadjokeChance;
	}

	public void setDadjokes(Boolean dadjokes) {
		this.dadjokes = dadjokes;
	}

	public void setDice(Boolean dice) {
		this.dice = dice;
	}

	public void setLogger(Boolean logger) {
		this.logger = logger;
	}

	public void setNewUserGreeting(Boolean newUserGreeting) {
		this.newUserGreeting = newUserGreeting;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRedditPreview(Boolean redditPreview) {
		this.redditPreview = redditPreview;
	}

	public void setServer(String server) {
		this.server = server;
	}

}
