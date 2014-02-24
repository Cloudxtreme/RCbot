package com.recursivechaos.rcbot.plugins.dadjokes;

/**
 * DadJokeListener parses text for the word "I'm" in order to make a "dad joke"
 * from the user's text.
 * 
 * @author Andrew Bell
 */
import java.util.Random;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.bot.object.MyPircBotX;

public class DadJokeListener extends ListenerAdapter<MyPircBotX> {
	Logger logger = LoggerFactory.getLogger(DadJokeListener.class);
	// TODO: Make this a dynamic value, or at least modifiable.
	int DAD_JOKE_CHANCE = 10; 
	
	@Override
	public void onMessage(final MessageEvent<MyPircBotX> event) {
		DAD_JOKE_CHANCE = event.getBot().getSettings().getDadjokeChance();
		try {
			Random generator = new Random();
			if (DAD_JOKE_CHANCE > 0) {
				int r = generator.nextInt(DAD_JOKE_CHANCE);
				if (r == (DAD_JOKE_CHANCE - 1)) {
					String message = event.getMessage();
					String response = parseDadJoke(message, event.getBot()
							.getNick());
					event.getChannel().getBot().sendIRC()
							.message(event.getChannel().getName(), response);
				}
			}
		} catch (Exception e) {
			// It's okay, you'd make a terrible dad.
			logger.error("Dad joke failed: " + e.getMessage());
		}
	}

	public String parseDadJoke(String message, String botname) {
		String dadReply = null;
		int im = message.indexOf("I'm");
		if (im != -1) {
			im = message.indexOf("i'm");
			im += 5;
			int per = 255;
			// Check for various punctuation.
			if ((message.indexOf(".") < per) && (message.indexOf(".") != -1)) {
				per = message.indexOf(".");
			}
			if ((message.indexOf(",") < per) && (message.indexOf(",") != -1)) {
				per = message.indexOf(",");
			}
			if ((message.indexOf("?") < per) && (message.indexOf("?") != -1)) {
				per = message.indexOf("?");
			}
			if ((message.indexOf("!") < per) && (message.indexOf("!") != -1)) {
				per = message.indexOf("!");
			}
			if ((message.indexOf(" and ") < per)
					&& (message.indexOf(" and ") != -1)) {
				per = message.indexOf(" and ");
			}
			if ((message.indexOf(" or ") < per)
					&& (message.indexOf(" or ") != -1)) {
				per = message.indexOf(" or ");
			}
			String kid = "";
			// If no punctuation found after i'm
			if ((per == 255) || (im > per)) {
				kid = message.substring(im);
			} else {
				kid = message.substring(im, per);
			}
			// remove a/an
			if (kid.substring(0, 2).equals("a ")) {
				kid = kid.substring(2);
			}
			if (kid.substring(0, 3).equals("an ")) {
				kid = kid.substring(3);
			}
			// Long messages will not display, nor will short ones

			if ((kid.length() > 2) && (kid.length() < 50)) {
				dadReply = "Hi " + kid + ", I'm " + botname + ".";
			}

		}
		return dadReply;
	}
}
