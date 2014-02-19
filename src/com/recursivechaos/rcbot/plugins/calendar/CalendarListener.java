package com.recursivechaos.rcbot.plugins.calendar;

/**
 * CalendarListener will fetch the next event from a calendar URL and post
 * it to the channel. 
 * 
 * @author Andrew Bell
 */
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.recursivechaos.rcbot.bot.object.MyPircBotX;
import com.recursivechaos.rcbot.settings.SettingsBO;

public class CalendarListener extends ListenerAdapter<MyPircBotX> {
	SettingsBO settings = new SettingsBO();
	Logger logger = LoggerFactory.getLogger(CalendarListener.class);

	// public CalendarListener(SettingsBO mySettings) {
	// this.settings = mySettings;
	// }
	/**
	 * Gets the 5 upcoming events from a calendar
	 * 
	 * @param event
	 * @return List<String> of the headlines for the events
	 * @throws Exception
	 */
	public List<String> getMeetupTicker(MessageEvent<MyPircBotX> event)
			throws Exception {
		List<String> meetupList = new ArrayList<>();
		// Set up the URL and the object that will handle the connection:
		// Update this to reflect your Domain-App-#
		CalendarService myService = new CalendarService(
				"RecursiveChaos-RC_BOT-1");
		URL feedUrl = new URL(event.getBot().getSettings().getCalendarUrl());
		CalendarQuery query = new CalendarQuery(feedUrl);
		// Resolve Recurring Events to single events
		query.setStringCustomParameter("singleevents", "true");
		// Order by start Time ascending
		query.setStringCustomParameter("orderby", "starttime");
		query.setStringCustomParameter("sortorder", "ascending");
		// Events in the future
		query.setStringCustomParameter("futureevents", "true");
		// Start the Query for CalendarEvents
		CalendarEventFeed resultFeed;
		resultFeed = myService.query(query, CalendarEventFeed.class);
		// Create list of calendar events
		List<CalendarEventEntry> entries = resultFeed.getEntries();
		// Sets max replies to 5
		int max = 5;
		if (max > entries.size()) {
			max = entries.size();
		}

		// adds String text of next event
		for (int i = 0; i < max; i++) {
			CalendarEventEntry entry = entries.get(i);
			String title = entry.getTitle().getPlainText();
			String location = "";
			String outString;
			long starttime = entry.getTimes().get(0).getStartTime().getValue();
			long endtime = entry.getTimes().get(0).getEndTime().getValue();
			java.util.Date eventDate;
			// "Fixes" date for all day events
			long SEC_IN_DAY = 86400000;
			if ((endtime - starttime) == SEC_IN_DAY) {
				eventDate = new java.util.Date(starttime + SEC_IN_DAY);
				SimpleDateFormat format = new SimpleDateFormat("MM/dd");
				outString = (title + " - " + format.format(eventDate));
			} else {
				eventDate = new java.util.Date(starttime);
				SimpleDateFormat format = new SimpleDateFormat("MM/dd h:mm a");
				outString = (title + " - " + format.format(eventDate));
			}
			// get location, if available
			if (entry.getLocations().size() > 0) {
				location = entry.getLocations().get(0).getValueString();
			}
			if (!location.equals("")) {
				outString = outString + " at " + location;
			}
			meetupList.add(outString);
		}
		return meetupList;
	}

	@Override
	public void onMessage(final MessageEvent<MyPircBotX> event) {
		String msg = event.getMessage().toLowerCase();
		// Responds to both '!meetup' and '!event'
		if ((msg.startsWith("!meetup")) || (msg.startsWith("!event"))) {
			try {
				List<String> events = getMeetupTicker(event);
				event.getBot()
						.sendIRC()
						.message(event.getChannel().getName().toString(),
								"The next event: " + events.get(0));
			} catch (Exception e) {
				logger.error("Calendar Fetch failed: " + e.getMessage());
			}
		}
	}
}
