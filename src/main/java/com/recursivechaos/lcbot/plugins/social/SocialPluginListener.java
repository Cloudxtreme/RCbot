package main.java.com.recursivechaos.lcbot.plugins.social;

/**
 * SocialPluginListener handles help for new users
 * 
 * @author Andrew Bell
 * 
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
import org.pircbotx.hooks.events.NickChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class SocialPluginListener extends ListenerAdapter {
	// Start logger
	Logger logger = LoggerFactory.getLogger(SocialPluginListener.class);
	

	// User Join event
	@Override
	public void onJoin(JoinEvent event) {
		logger.info(event.getUser().getNick() + " joined the channel.");
		// If not the bot, welcomes new user.
		// TODO: Add list of 'known' users
		if (isNewUser(event)) {
			event.respond("Welcome to the unofficial LaunchCode  IRC channel. "
					+ "This is a new channel, and we're still trying to gain users."
					+ " If you have a question, feel free to ask, and be sure to "
					+ "come back!");
		}
	}

	/**
	 * Returns if the user is a new visitor to the channel
	 * @param event join event
	 * @return	true if new user
	 * @throws Exception 
	 */
	private boolean isNewUser(JoinEvent event) {
		List<String> knownUsers;
		try {
			knownUsers = getUserList();
		} catch (Exception e) {
			logger.error("Unable to load userlist. " + e.getMessage());
			return false;
		}
		boolean newUser = false;
		String username = event.getUser().getNick();
		for (String u : knownUsers) {
			if (event.getUser().getNick().equalsIgnoreCase(u)) {
				logger.info("Known user: " + username);
			} else {
				logger.info("New user: " + username);
				newUser = true;
				try {
					addKnownUser(username);
				} catch (IOException e) {
					logger.error("Unable to add to userlist. " +e.getMessage());
				}
			}
		}
		return newUser;
	}

	private List<String> getUserList() throws Exception{
		List<String> userList = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("users.txt"));
	    try {
	        String line = br.readLine();
	        while (line != null) {
	        	String startChar = line.substring(0,1);
	        	if(!startChar.equals("#")){
	        		userList.add(line);
	        	}
	        	line = br.readLine();
	        }
        } catch (Exception e){
        	logger.error("Read Failed: " + e.getMessage());
	    } finally {
	        br.close();
	    }
		return userList;
	}

	/**
	 * Adds user to the known users list
	 * @param username string of the username
	 * @throws IOException 
	 */
	private void addKnownUser(String username) throws IOException {
		BufferedWriter bw = null;

		try {
		    bw = new BufferedWriter(new FileWriter("users.txt", true));
		    bw.newLine();
		    bw.write(username);
		    bw.flush();
		} catch (IOException ioe) {
		    ioe.printStackTrace();
		} finally { // always close the file
		    if (bw != null) {
		        try {
		            bw.close();
		        } catch (IOException ioe2) {
		            // just ignore it
		        }
		    }
		}
	}

	// User Nickname Changes
	@Override
	public void onNickChange(NickChangeEvent event) {
		logger.info("Nick Change from " + event.getOldNick() + " to "
				+ event.getNewNick() + ".");
		// If a user gets forced to a Guest* nickname, the bot will post a
		// message letting them know what happened.
		if (event.getNewNick().contains("Guest")) {
			event.getBot()
					.sendIRC()
					.message(
							"#LaunchCode",
							"Welcome to IRC! "
									+ "You may have chosen a nickname that is already taken. "
									+ "Type '/nick <new nickname>', without quotes where "
									+ "<new nickname> is a different, unique nickname.");
		}
	}
}
