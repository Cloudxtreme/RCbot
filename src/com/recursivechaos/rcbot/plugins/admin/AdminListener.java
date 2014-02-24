package com.recursivechaos.rcbot.plugins.admin;

import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.bot.object.MyPircBotX;

public class AdminListener extends ListenerAdapter<MyPircBotX> { 
	Logger logger = LoggerFactory.getLogger(AdminListener.class);
	public void onPrivateMessageEvent(MyPircBotX bot, User user, String message){
		message = message.toUpperCase();
		if (user.getNick()==bot.getBot().getSettings().getAdmin()){
			if (message.startsWith("SET DAD JOKE")){
				int dadJokeChance = Integer.parseInt(message.substring(12));
				bot.getSettings().setDadjokeChance(dadJokeChance);
				logger.info("Dad Joke Updated to: " + dadJokeChance + " on bot " + bot.getNick());
			}
			if(message.startsWith("QUIT")){
				bot.stopBotReconnect();
				bot.sendIRC().quitServer("Shutting down...");
				
			}
			if(message.startsWith("MESSAGE")){
				String dest = message.substring(message.indexOf(" "));
				dest = dest.substring(0, dest.indexOf(" "));
				String reply = message.substring(message.indexOf(dest));
				bot.sendIRC().message(dest,reply);
			}
		}
		
	}
}
