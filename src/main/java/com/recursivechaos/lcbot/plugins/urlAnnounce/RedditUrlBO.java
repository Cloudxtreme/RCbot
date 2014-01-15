package main.java.com.recursivechaos.lcbot.plugins.urlAnnounce;
/**
 * RedditUrlBO handles all reddit url translations using JSON
 * @author Andrew Bell
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedditUrlBO {
	Logger logger = LoggerFactory.getLogger(RedditUrlBO.class);
	// reddit URL types
	// TODO: This is gross, fix it. Actually, this whole thing is gross.
	static final String TYPE_COMM = "t1";
	static final String TYPE_ACCO = "t2";
	static final String TYPE_LINK = "t3";
	static final String TYPE_MESG = "t4";
	static final String TYPE_SUBR = "t5";
	static final String TYPE_AWAR = "t6";
	static final String TYPE_PROM = "t7";
	static final String TYPE_NONE = "t0";
	static final String[] validUrls = { "http://", "www.", "reddit.com/",
			"redd.it" };
	static final String[] ignoredUsers = { "LCbot" };
	
	/**
	 * Reads the json text and creates a string
	 * @param rd reader loaded with json url, via input stream
	 * @return	json text
	 * @throws IOException
	 */
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	/**
	 * Creates a json object from a url
	 * @param url string of the url, with or without .json appended
	 * @return json object from the url
	 * @throws Exception
	 */
	private static JSONObject readJsonFromUrl(String url) throws Exception {
		if (!url.contains(".json")) {
			url = url.concat(".json");
		}
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText.substring(jsonText
					.indexOf("{")));
			return json;
		} finally {
			is.close();
		}
	}
	
	/**
	 * gets author of post given json object
	 * @param cdata json object
	 * @return string of author
	 */
	private String getAuthor(JSONObject cdata) {
		return cdata.getString("author");
	}
	
	/**
	 * Returns NSFW tag if true, nothing if false
	 * @param cdata reddit post JSON object
	 * @return [NSFW] if true, nothing if false
	 */
	private String getNSFW(JSONObject cdata) {
		String flag;// = (String)cdata.get("over_18");
		boolean nsfw = cdata.getBoolean("over_18");
		if (nsfw == true) {
			flag = " [NSFW] ";
		} else {
			flag = "";
		}
		return flag;
	}
	
	/**
	 * Fetches 'preview' of a reddit url
	 * @param url string url of a reddit post
	 * @return preview text of the reddit post
	 * @throws Exception
	 */
	public String getPreview(String url) throws Exception {
		String type = TYPE_NONE;
		JSONObject json = readJsonFromUrl(url);
		type = getType(json);
		JSONObject data = (JSONObject) json.get("data");
		type = updateType(data, type);
		JSONArray children = (JSONArray) data.get("children");

		JSONObject child = (JSONObject) children.get(0);
		type = updateType(child, type);
		logger.debug("T: " + (String) child.get("kind"));
		JSONObject cdata = (JSONObject) child.get("data");
		type = updateType(cdata, type);

		String preview = "";
		switch (type) {
		case TYPE_LINK:
			preview = getAuthor(cdata) + " posted \"" + getTitle(cdata)
					+ "\" to /r/" + getSubreddit(cdata) + getNSFW(cdata)
					+ " (+" + getScore(cdata) + ").";
			break;
		}

		return preview;
	}
	
	/**
	 * Fetches score of the post in the json object
	 * @param cdata json object of a reddit url
	 * @return String score of reddit post, should be always <= 0
	 */
	private String getScore(JSONObject cdata) {
		int score = cdata.getInt("score");
		return Integer.toString(score);
	}

	/**
	 * Fetches subreddit of the json object
	 * @param cdata json object of a reddit url
	 * @return String of the subreddit, minus the /r/
	 */
	private String getSubreddit(JSONObject cdata) {
		return (String) cdata.get("subreddit");
	}

	/**
	 * Fetches the post title of a json object.
	 * @param cdata json object of a reddit url
	 * @return String title of the url
	 */
	private String getTitle(JSONObject cdata) {
		return (String) cdata.get("title");
	}

	/**
	 * Fetches the type of reddit post, represented by a t*
	 * @param child json object of a reddit url
	 * @return raw string of reddit type
	 */
	private String getType(JSONObject child) {
		String tval = (String) child.get("kind");

		// return result;
		if (tval.equals("Listing"))
			tval = TYPE_NONE;
		return tval;
	}

	/**
	 * updates the type if it changes
	 * @param data json objec of the reddit url
	 * @param type current type
	 * @return new type
	 */
	private String updateType(JSONObject data, String type) {
		if (type.equals(TYPE_NONE)) {
			try {
				type = getType(data);
			} catch (Exception e) {
				// It's not that type
			}
		}
		return type;
	}

}
