package com.recursivechaos.rcbot.plugins.redditPreview;

/**
 * RedditPreviewListener will check messages for a valid reddit url, and
 * display a preview
 * 
 * @author Andrew Bell
 */
import static java.util.Arrays.asList;

import java.util.Iterator;
import java.util.List;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.settings.SettingsBO;

@SuppressWarnings("rawtypes")
public class RedditPreviewListener extends ListenerAdapter {
	// static final String[] ignoredUsers = ;
	static final List<String> ignoredUsers = asList("dtbot", "dsbot",
			"dolemite7", "testStoop");
	// Start logger
	Logger logger = LoggerFactory.getLogger(RedditPreviewListener.class);
	SettingsBO mySettings = new SettingsBO();

	/**
	 * Checks if the event contains a valid URL. This is a function, rather than
	 * in the BO, because I don't want to have to create the BO on EVERY event.
	 * 
	 * @param event
	 *            event object to check for url
	 * @return true if url is found in the event message
	 */
	private boolean hasValidURL(MessageEvent event) {
		final String[] validURLs = { "http://", "www.", "reddit.com/",
				"redd.it/" };
		boolean validURL = false;
		// get message text
		String message = event.getMessage().toLowerCase();
		// search message for URL
		for (String u : validURLs) {
			if (message.contains(u)) {
				validURL = true;
				logger.info("Valid URL Found");
			}
		}
		return validURL;
	}

	private boolean isApprovedUser(MessageEvent event) {
		boolean flag = true;
		Iterator itr = ignoredUsers.iterator();
		while (itr.hasNext()) {
			String user = event.getUser().getNick().toLowerCase();
			String banned = itr.next().toString().toLowerCase();
			if (user.equals(banned)) {
				flag = false;
			}
		}
		return flag;
	}

	@Override
	public void onMessage(final MessageEvent event) {
		if (isApprovedUser(event)) {
			if (hasValidURL(event)) {
				UrlBO url = new UrlBO();
				String reply = url.getAnnouncement(event);
				if (!reply.isEmpty()) {
					// event.respond(reply);
					event.getBot()
							.sendIRC()
							.message(event.getChannel().getName().toString(),
									reply);
				}
			}
		}
	}
}
