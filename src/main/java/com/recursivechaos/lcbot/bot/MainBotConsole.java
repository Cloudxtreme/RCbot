package main.java.com.recursivechaos.lcbot.bot;

/**
 * MainBotConsole is responsible for initializing the bot object. Each 
 * bot module needs to have their listener attached.
 * 
 * @author Andrew Bell
 *
 */
import java.io.IOException;

import main.java.com.recursivechaos.lcbot.plugins.social.SocialPluginListener;
import main.java.com.recursivechaos.lcbot.plugins.urlAnnounce.UrlAnnouncePluginListener;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.cap.TLSCapHandler;
import org.pircbotx.exception.IrcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainBotConsole {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		// Start logger
		Logger logger = LoggerFactory.getLogger(MainBotConsole.class);
		logger.info("Loading bot configuration.");
		// Load configuration
		// TODO: Register with nickserv
		Configuration configuration = new Configuration.Builder()
				.setName("LCbot")
				// Set the nick of the bot. CHANGE IN YOUR CODE
				.setLogin("LCbot")
				// login part of hostmask, eg name:login@host
				.setAutoNickChange(true)
				// Automatically change nick
				.setCapEnabled(true)
				// Enable CAP features
				.addCapHandler(
						new TLSCapHandler(new UtilSSLSocketFactory()
								.trustAllCertificates(), true))
				.addListener(new SocialPluginListener())
				.addListener(new UrlAnnouncePluginListener())
				.setServerHostname("chat.freenode.org")
				.addAutoJoinChannel("#StoopTest").buildConfiguration();
		logger.info("Configuration loaded.");
		// Start bot
		try {
			PircBotX bot = new PircBotX(configuration);
			bot.startBot();
			logger.info("Bot Started.");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("IOException: " + e.getMessage());
		} catch (IrcException e) {
			e.printStackTrace();
			logger.error("IrcException: " + e.getMessage());
		} finally {
			logger.info("Bot stopped.");
		}
	}
}
