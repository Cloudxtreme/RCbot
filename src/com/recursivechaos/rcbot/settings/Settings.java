package com.recursivechaos.rcbot.settings;

/**
 * Settings is the object representing global bot settings.
 * 
 * @author Andrew Bell
 * 
 */
public class Settings {
	String nick = "";
	String password = "";
	String server = "";
	String channel = "";
	String admin = "";

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

	public String getChannel() {
		return channel;
	}

	public String getNick() {
		return nick;
	}

	public String getPassword() {
		return password;
	}

	public String getServer() {
		return server;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setServer(String server) {
		this.server = server;
	}
}
