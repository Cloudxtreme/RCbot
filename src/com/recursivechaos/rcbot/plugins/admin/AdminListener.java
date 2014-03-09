package com.recursivechaos.rcbot.plugins.admin;

/**
 * AdminListener listens to the bot for admin commands.
 * 
 * @author Andrew Bell www.recursivechaos.com
 */
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.bot.object.MyPircBotX;

public class AdminListener extends ListenerAdapter<MyPircBotX> { 
	Logger logger = LoggerFactory.getLogger(AdminListener.class);
	
	 public void onPrivateMessage(PrivateMessageEvent<MyPircBotX> event) throws Exception{
		String message = event.getMessage().toUpperCase();
		String user = event.getUser().getNick().toLowerCase();
		String admin = event.getBot().getSettings().getAdmin().toLowerCase();
		if (user.equals(admin)){
			if (message.startsWith("SET DAD JOKE")){
				int dadJokeChance = Integer.parseInt(message.substring(12));
				event.getBot().getSettings().setDadjokeChance(dadJokeChance);
				logger.info("Dad Joke Updated to: " + dadJokeChance + " on bot " 
						+ event.getBot().getNick());
			}
			if(message.startsWith("QUIT")){
				event.getBot().stopBotReconnect();
				event.getBot().sendIRC().quitServer("Daisy, Daisy...");
			}
			if(message.startsWith("MESSAGE")){
				String dest = message.substring(message.indexOf(" "));
				dest = dest.substring(0, dest.indexOf(" "));
				String reply = message.substring(message.indexOf(dest));
				event.getBot().sendIRC().message(dest,reply);
			}
		}
	}
	
	public void onPrivateMessageEvent(MyPircBotX bot, User user, String message){
		
		
	}
}
