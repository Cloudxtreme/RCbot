package com.recursivechaos.rcbot.plugins.dadjokes;
/**
 * DadJokeListener parses text for the word "I'm" in order to make a "dad joke"
 * from the user's text.
 * 
 * @author Andrew Bell
 */
import java.util.Random;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DadJokeListener extends ListenerAdapter<PircBotX> {
	Logger logger = LoggerFactory.getLogger(DadJokeListener.class);

	@Override
	public void onMessage(final MessageEvent<PircBotX> event) {
		String message = event.getMessage();
		//TODO: Make this a dynamic value, or at least modifiable.
		int DAD_JOKE_CHANCE = 5;
		try {
			Random generator = new Random();
			if (DAD_JOKE_CHANCE > 0) {
				int r = generator.nextInt(DAD_JOKE_CHANCE);
				if (r == (DAD_JOKE_CHANCE - 1)) {
					int im = message.indexOf("I'm");
					if (im == -1) {
						im = message.indexOf("i'm");
					}
					im += 4;
					int per = 255;
					// Check for various punctuation.
					if ((message.indexOf(".") < per)
							&& (message.indexOf(".") != -1)) {
						per = message.indexOf(".");
					}
					if ((message.indexOf(",") < per)
							&& (message.indexOf(",") != -1)) {
						per = message.indexOf(",");
					}
					if ((message.indexOf("?") < per)
							&& (message.indexOf("?") != -1)) {
						per = message.indexOf("?");
					}
					if ((message.indexOf("!") < per)
							&& (message.indexOf("!") != -1)) {
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
						String dadReply = "Hi " + kid + ", I'm "
								+ event.getBot().getNick() + ".";
						event.getChannel()
								.getBot()
								.sendIRC()
								.message(event.getChannel().getName(), dadReply);
					}
				}
			}
		} catch (Exception e) {
			// It's okay, you'd make a terrible dad.
			logger.error("Dad joke failed: " + e.getMessage());
		}
	}
}
