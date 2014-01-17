package com.recursivechaos.rcbot.plugins.urlAnnounce;

/**
 * urlAnnouncePluginListener will check messages for a valid url, and announce
 * them if found
 * @author Andrew Bell
 * 
 */
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.settings.SettingsBO;

@SuppressWarnings("rawtypes")
public class UrlAnnouncePluginListener extends ListenerAdapter {
	// Start logger
	Logger logger = LoggerFactory.getLogger(UrlAnnouncePluginListener.class);
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

	@Override
	public void onMessage(final MessageEvent event) {
		if (hasValidURL(event)) {
			UrlBO url = new UrlBO();
			String reply = url.getAnnouncement(event);
			if (!reply.isEmpty()) {
				event.getBot().sendIRC()
						.message(event.getChannel().toString(), reply);
			}
		}
	}
}
