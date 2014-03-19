package com.recursivechaos.rcbot.plugins.stoopsnoop.autobanner;
/**
 * AutobannerDAO provides all the necessary functions to check for spammers, as well
 * as ban spammers.
 * 
 * @author Andrew Bell www.recursivechaos.com
 */
import org.pircbotx.hooks.events.MessageEvent;

import com.recursivechaos.rcbot.bot.object.BotException;
import com.recursivechaos.rcbot.bot.object.MyPircBotX;

public interface AutobannerDAO {

	/**
	 * isFloodSpam will check if the message is only one word, and if so, will check
	 * the previous messages to see if they match. This only works if there is no spaces
	 * in what they are spamming, but it prevents a hibernate query for every event. It is possible
	 * to flood spam by simply placing a ' ' in the message.
	 * @param event triggering event
	 * @return true if flood spam
	 */
	boolean isFloodSpam(MessageEvent<MyPircBotX> event);

	/**
	 * banUser adds the user to the ban list. 
	 * @param event	event triggering ban
	 * @param note	note to attach
	 * @param hours	how long to ban
	 * @throws BotException On error saving to db 
	 */
	void banUser(MessageEvent<MyPircBotX> event, String note) throws BotException ;

	/**
	 * isBulk spam checks the message contents for over-use of a word. Will check against list
	 * of ignored words, if it meets the initial threshold, and return true if still above threshold
	 * @param event triggering event
	 * @return true if bulk spam
	 */
	boolean isBulkSpam(MessageEvent<MyPircBotX> event);
}
