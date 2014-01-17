package com.recursivechaos.rcbot.plugins.urlAnnounce;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class UrlBO {
	static final String[] validUrls = { "http://", "www.", "reddit.com/",
			"redd.it/" };
	Logger logger = LoggerFactory.getLogger(UrlBO.class);

	/**
	 * Gets a string describing the URL given
	 * 
	 * @param event
	 *            event containing a URL
	 * @return string description of URL
	 */
	public String getAnnouncement(MessageEvent event) {
		String url = getUrlFromEvent(event);
		String title = getPreviewFromUrl(url);
		return title;
	}

	/**
	 * Returns preview text for a url, depending on the source
	 * 
	 * @param url
	 *            string of the url
	 * @return preview text of the url
	 */
	private String getPreviewFromUrl(String url) {
		String preview = null;
		if (url.contains("reddit.com")) {
			RedditUrlBO reddit = new RedditUrlBO();
			try {
				preview = reddit.getPreview(url);
			} catch (Exception e) {
				logger.info("Was not able to parse URL: " + e.getMessage());
			}
		}
		return preview;
	}

	/**
	 * Returns URL after checking to see if it redirects
	 * 
	 * @param url
	 *            string value of a url
	 * @return string value of the redirected URL
	 */
	private String getRedirectedURL(String url) {
		logger.info("Predirected url: " + url);
		// If redirect, finds end url
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setInstanceFollowRedirects(false);
			while (connection.getResponseCode() / 100 == 3) {
				url = connection.getHeaderField("location");
				logger.info("Long URL: " + url);
				connection = (HttpURLConnection) new URL(url).openConnection();
				connection.setInstanceFollowRedirects(false);
			}
		} catch (MalformedURLException e) {
			logger.info("Malformed URL: " + e.getMessage());
			url = null;
		} catch (IOException e) {
			logger.info("IOException: " + e.getMessage());
			url = null;
		}
		logger.info("Redirected URL: " + url);
		return url;
	}

	/**
	 * Fetches the URL string from a message event
	 * 
	 * @param event
	 *            from pircbotx
	 * @return String of the url in the event
	 */
	private String getUrlFromEvent(MessageEvent event) {
		// stripped URL
		String url = "";
		// get message text
		String message = event.getMessage().toLowerCase();
		// start location of URL
		int start = -1;
		// search message for URL
		for (String u : validUrls) {
			if (message.contains(u)) {
				// If earlier, or not found
				if ((start > message.indexOf(u)) || (start == -1)) {
					start = message.indexOf(u);
					logger.debug("Index found at " + start);
				}
			}
		}
		// If a URL is found, trim url
		if (start != -1) {
			String ltrim = message.substring(start);
			int end = ltrim.indexOf(" ");
			if (end != -1) {
				url = ltrim.substring(0, end);
			} else {
				url = ltrim;
			}

		}
		url = getRedirectedURL(url);
		return url;
	}

}
