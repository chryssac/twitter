package twitter4j_example;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUser {
	private String id = null;
	private String password = null;
	private List<Status> tweets = null;
	private List<Status> imageTweets = null;
	private List<MediaEntity> tweetMedia = null; 


	TwitterUser(String id, String password) {
		this.id = id;
		this.password = password;
		tweets = new ArrayList<Status>();
		imageTweets = new ArrayList<Status>();
		tweetMedia = new ArrayList<MediaEntity>();
	}

	/**
	 * Returns all tweets for this user. Twitter restricts the size of the search
	 * index by placing a date limit on the updates they allow you to search.
	 * This limit is currently around 1.5 weeks but is dynamic and subject to shrink
	 * as the number of tweets per day continues to grow.
	 * @param config - the configuration used to build a Twitter object through which 
	 * the user's Twitter account will be accessed to retrieve the tweets.
	 * @return - the tweets for this user or null if the user has no tweets
	 */
	public List<Status> getTweets(ConfigurationBuilder config) {
		Twitter twitter = new TwitterFactory(config.build()).getInstance();

		try {
			tweets.addAll(twitter.getUserTimeline(id));

		}catch (TwitterException e) {
			e.printStackTrace();
		}

		return tweets;
	}

	/**
	 * Builds a list of all image tweets, and a list of all the associated 
	 * external assets (i.e. images or videos) for each tweet of this user.
	 */
	public void retrieveImageTweetsAndAssets() {
		if(tweets != null) {

			for(Status status : tweets) {
				MediaEntity[] media = status.getMediaEntities(); //get the media entities from the status

				for(MediaEntity m : media) {
					tweetMedia.add(m);

					switch(m.getType()) {
					case "photo":
					case "animated_gif":
						imageTweets.add(status);						
						break;

					case "video":
						break;
					}
				}
			}
		}
	}

	/**
	 * Returns all of this user's tweets in JSON format
	 * @return - a string representing all tweets for this user in JSON format
	 */
	public String toJSON() {
		String json = "";
		for(Status s : tweets) {
			json += TwitterObjectFactory.getRawJSON(s) + "\n";
		}
		return json;
	}

	/**
	 * Saves all of this user's tweet images.
	 * @return - true if the media is saved successfully, false if an error occurs
	 * @throws MalformedURLException - if the media URL is null, or the protocol is not specified or unknown
	 */
	public boolean saveImages() throws MalformedURLException {
		BufferedImage image = null;
		String urlString = null;
		int count = 1;
		
		for(MediaEntity m : tweetMedia) {
			urlString = m.getMediaURL();
			String format = urlString.substring(urlString.lastIndexOf('.')+1, urlString.length());
			URL url = new URL(urlString);
			try {
				image = ImageIO.read(url);
				ImageIO.write(image, format, new File("media" +count++ +"." +format));
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the ID for this user.
	 * @return - the user ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the user ID.
	 * @param id - the user ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the password for this user.
	 * @return - a string representing this user's password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password for this user.
	 * @param password - the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns image tweets for this user.
	 * @return - a list of tweets that have images
	 */
	public List<Status> getImageTweets() {
		return imageTweets;
	}

	/**
	 * Sets the list of image tweets for this user.
	 * @param imageTweets - the list of image tweets
	 */
	public void setImageTweets(List<Status> imageTweets) {
		this.imageTweets = imageTweets;
	}

	/**
	 * Returns a list of external assets associated with this user's tweets.
	 * @return - a list of this user's external associated assets
	 */
	public List<MediaEntity> getAssets() {
		return tweetMedia;
	}

	/**
	 * Sets this user's external associated assets (i.e. images or videos).
	 * @param assets - the list of external associated assets 
	 */
	public void setAssets(List<MediaEntity> assets) {
		this.tweetMedia = assets;
	}
}
