package com.recursivechaos.rcbot.plugins.stoopsnoop;

/**
 * LogListener listens for events and passes them to the EventLogDAO
 * 
 * @author Andrew Bell www.recursivechaos.com
 * 
 */

import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.bot.object.BotException;
import com.recursivechaos.rcbot.bot.object.MyPircBotX;
import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.EventLogDAOImpl;
import com.recursivechaos.rcbot.plugins.stoopsnoop.log.EventLogDAO;

public class LogListener extends ListenerAdapter<MyPircBotX> {
	Logger logger = LoggerFactory.getLogger(LogListener.class);
	EventLogDAO eventlog = new EventLogDAOImpl();

	@Override
	public void onEvent(final Event<MyPircBotX> event) throws Exception {
		try{
			eventlog.logEvent(event);
		}catch(BotException e){
			String admin = (event.getBot().getSettings().getAdmin());
			event.getBot().sendIRC().message(admin, "I broke: " + e.getMessage());
		}finally{
			// This throws a generic Exception, but in *theory* the BotException will be caught,
			// while anything else will be passed on to the bot to handle ever so gracefully.
			super.onEvent(event);
		}
	}
}