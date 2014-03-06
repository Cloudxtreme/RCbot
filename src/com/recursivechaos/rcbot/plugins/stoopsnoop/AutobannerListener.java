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
		AutobannerDAO banhammer = new AutobannerDAOImpl();
		if(banhammer.isFloodSpam(event)){
			banhammer.banUser(event,"Flooding.",24);
			event.respond("I'm telling (Flooding)");
		}else if(banhammer.isBulkSpam(event)){
			banhammer.banUser(event,"Bulk Spam.",24);
			event.respond("I'm telling (Bulk)");
		}
	}
}