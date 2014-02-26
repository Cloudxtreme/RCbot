package com.recursivechaos.rcbot.plugins.newUserGreetingPlugin;

/**
 * NewUserGreetingListener will greet any new (to the bot) users with a welcome
 * message, and then adds them to a list of known users.
 * 
 * @author Andrew Bell www.recursivechaos.com
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class NewUserGreetingListener extends ListenerAdapter {
	// Start logger
	Logger logger = LoggerFactory.getLogger(NewUserGreetingListener.class);

	/**
	 * Adds user to the known users list
	 * 
	 * @param username
	 *            string of the username
	 */
	private void addKnownUser(String username) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("users.txt", true));
			bw.newLine();
			bw.write(username);
			bw.flush();
		} catch (IOException e) {
			logger.error("Unable to update 'users.txt': " + e.getMessage());
			e.printStackTrace();
		} finally { // always close the file
			try {
				bw.close();
			} catch (IOException e) {
				// You dun goofed
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns a list of known users
	 * 
	 * @return List<String> of known users
	 * @throws Exception
	 */
	private List<String> getUserList() throws Exception {
		List<String> userList = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("users.txt"));
		try {
			String line = br.readLine();
			while (line != null) {
				String startChar = line.substring(0, 1);
				if (!startChar.equals("#")) {
					userList.add(line);
				}
				line = br.readLine();
			}
		} catch (Exception e) {
			logger.error("Read Failed: " + e.getMessage());
		} finally {
			br.close();
		}
		return userList;
	}

	/**
	 * Returns if the user is a new visitor to the channel
	 * 
	 * @param event
	 *            join event
	 * @return true if new user
	 * @throws Exception
	 */
	private boolean isExistingUser(JoinEvent event) {
		List<String> knownUsers;
		try {
			knownUsers = getUserList();
		} catch (Exception e) {
			logger.error("Unable to load userlist. " + e.getMessage());
			return false;
		}
		boolean existingUser = false;
		String username = event.getUser().getNick();
		for (String u : knownUsers) {
			if (event.getUser().getNick().equalsIgnoreCase(u)) {
				logger.info("Known user: " + username);
				existingUser = true;
			}
		}
		// New user
		if (existingUser == false) {
			logger.info("New user: " + username);
			addKnownUser(username);
		}
		return existingUser;
	}

	// User Join event
	@Override
	public void onJoin(JoinEvent event) {
		logger.info(event.getUser().getNick() + " joined the channel.");
		// If not an existing user, welcomes new user.
		if (!isExistingUser(event)) {
			// TODO: get reply from configuration file
			event.respond("Welcome to "
					+ event.getChannel().getName().toString() + " This "
					+ "is a new channel, and we're still trying to gain "
					+ "users. If you have a question, feel free to ask, "
					+ "and be sure to come back soon!");
		}
	}
}