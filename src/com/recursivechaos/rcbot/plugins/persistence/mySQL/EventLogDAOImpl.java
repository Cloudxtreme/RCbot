package com.recursivechaos.rcbot.plugins.persistence.mySQL;

/**
 * EventLogDAOImpl implements plugins.eventlog.EventLogDAO to provide
 * the ability to take generic events, and log them accordingly to the db
 * 
 * @author Andrew Bell
 * 
 */
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.ServerPingEvent;

import com.recursivechaos.rcbot.plugins.eventlog.EventLogDAO;
import com.recursivechaos.rcbot.plugins.persistence.DAO;
import com.recursivechaos.rcbot.plugins.persistence.EventLog;

public class EventLogDAOImpl extends DAO implements EventLogDAO {

	private void heartbeat(Event<PircBotX> event) {
		try {
			ServerPingEvent<PircBotX> pingEvent = (ServerPingEvent<PircBotX>) event;
			EventLog eventlog = new EventLog(pingEvent.getTimestamp(),
					"SERVER", "", "", event.getBot().getNick(),
					EventLog.TYPE.PING.toString());
			begin();
			getSession().save(eventlog);
			commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		System.out.println("Heartbeat confirmed.");
	}

	@Override
	public void logEvent(Event<PircBotX> event) {
		// Message event
		// TODO: Compare object instead of string
		if (event.getClass().toString()
				.equals("class org.pircbotx.hooks.events.MessageEvent")) {
			try {
				MessageEvent<PircBotX> msgEvent = (MessageEvent<PircBotX>) event;

				EventLog eventlog = new EventLog(msgEvent.getTimestamp(),
						msgEvent.getUser().getNick(), msgEvent.getMessage(),
						msgEvent.getChannel().getName(), event.getBot()
								.getNick(), EventLog.TYPE.MESSAGE.toString());
				begin();
				getSession().save(eventlog);
				commit();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
		}
		if (event.getClass().toString()
				.equals("class org.pircbotx.hooks.events.ActionEvent")) {
			try {
				ActionEvent<PircBotX> actEvent = (ActionEvent<PircBotX>) event;
				EventLog eventlog = new EventLog(actEvent.getTimestamp(),
						actEvent.getUser().getNick(), actEvent.getMessage(),
						actEvent.getChannel().getName(), event.getBot()
								.getNick(), EventLog.TYPE.ACTION.toString());
				begin();
				getSession().save(eventlog);
				commit();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
		}
		if (event.getClass().toString()
				.equals("class org.pircbotx.hooks.events.ServerPingEvent")) {
			heartbeat(event);
		}

	}

}
