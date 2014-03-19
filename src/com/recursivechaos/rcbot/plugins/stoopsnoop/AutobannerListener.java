package com.recursivechaos.rcbot.plugins.stoopsnoop;

/**
 * Autobanner listens for abusive nature and can automatically ban users, given they meet
 * spam thresholds. Only is hooking message events, not action events.
 * 
 * @author Andrew Bell www.recursivechaos.com
 * 
 */

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import com.recursivechaos.rcbot.bot.object.MyPircBotX;
import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.AutobannerDAOImpl;
import com.recursivechaos.rcbot.plugins.stoopsnoop.autobanner.AutobannerDAO;

public class AutobannerListener extends ListenerAdapter<MyPircBotX> {

	public void onMessage(final MessageEvent<MyPircBotX> event) throws Exception {
		String admin = event.getBot().getSettings().getAdmin();

		AutobannerDAO banhammer = new AutobannerDAOImpl();
		if(banhammer.isFloodSpam(event)){
			banhammer.banUser(event,"Flooding.");
			//event.respond("I'm telling (Flooding)");
			event.getBot().sendIRC().message(admin, event.getUser().getNick() 
					+ " is flood spamming in " + event.getChannel().getName() + " with line "+
					event.getMessage());
		}else if(banhammer.isBulkSpam(event)){
			banhammer.banUser(event,"Bulk Spam.");
			//event.respond("I'm telling (Bulk)");
			event.getBot().sendIRC().message(admin, event.getUser().getNick() 
					+ " is bulk spamming in " + event.getChannel().getName() + " with line "+
					event.getMessage());
		}
	}
}