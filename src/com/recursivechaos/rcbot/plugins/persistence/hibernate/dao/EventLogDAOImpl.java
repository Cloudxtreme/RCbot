package com.recursivechaos.rcbot.plugins.persistence.hibernate.dao;

/**
 * EventLogDAOImpl implements plugins.eventlog.EventLogDAO to provide
 * the ability to take generic events, and log them accordingly to the db
 * 
 * @author Andrew Bell www.recursivechaos.com
 * 
 */
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.ServerPingEvent;

import com.recursivechaos.rcbot.bot.object.BotException;
import com.recursivechaos.rcbot.bot.object.MyPircBotX;
import com.recursivechaos.rcbot.plugins.stoopsnoop.log.EventLogDAO;
import com.recursivechaos.rcbot.plugins.stoopsnoop.objects.EventLog;

public class EventLogDAOImpl extends DAO implements EventLogDAO {

	private void heartbeat(Event<MyPircBotX> event) throws BotException {
		try {
			ServerPingEvent<MyPircBotX> pingEvent = (ServerPingEvent<MyPircBotX>) event;
			EventLog eventlog = new EventLog(pingEvent.getTimestamp(),
					"SERVER", "", "", event.getBot().getNick(),
					EventLog.TYPE.PING.toString());
			begin();
			getSession().save(eventlog);
			commit();

		} catch (Exception e) {
			e.printStackTrace();
			rollback();
			throw new BotException("Error logging server ping.",e);
		} finally {
			close();
		}
		System.out.println("Heartbeat confirmed.");
	}

	public void logEvent(Event<MyPircBotX> event) throws BotException {
		// Message event
		// TODO: Compare object instead of string
		if (event.getClass().toString()
				.equals("class org.pircbotx.hooks.events.MessageEvent")) {
			try {
				MessageEvent<MyPircBotX> msgEvent = (MessageEvent<MyPircBotX>) event;

				EventLog eventlog = new EventLog(msgEvent.getTimestamp(),
						msgEvent.getUser().getNick(), msgEvent.getMessage(),
						msgEvent.getChannel().getName(), event.getBot()
								.getNick(), EventLog.TYPE.MESSAGE.toString());
				begin();
				getSession().save(eventlog);
				commit();

			} catch (Exception e) {
				e.printStackTrace();
				rollback();
				throw new BotException("Error logging message.",e);
			} finally {
				close();
			}
		}
		if (event.getClass().toString()
				.equals("class org.pircbotx.hooks.events.ActionEvent")) {
			try {
				ActionEvent<MyPircBotX> actEvent = (ActionEvent<MyPircBotX>) event;
				EventLog eventlog = new EventLog(actEvent.getTimestamp(),
						actEvent.getUser().getNick(), actEvent.getMessage(),
						actEvent.getChannel().getName(), event.getBot()
								.getNick(), EventLog.TYPE.ACTION.toString());
				begin();
				getSession().save(eventlog);
				commit();
			} catch (Exception e) {
				e.printStackTrace();
				rollback();
				throw new BotException("Error logging action.",e);
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
