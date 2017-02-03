package twitter4j_example;


import java.net.MalformedURLException;
import java.util.List;

import twitter4j.Status;
import twitter4j.conf.ConfigurationBuilder;

public class Main {

	public static void main(String[] args) {
		
		ConfigurationBuilder config = new ConfigurationBuilder();		
		config.setOAuthConsumerKey("k7cO2L3IIFe0zRkMoP40r43sz");
		config.setOAuthConsumerSecret("dNVNzVOHJSMGKXhsFz3VlpghJggVw8Ss5ukEs1uP3OqjiuxhmU");		
		config.setOAuthAccessToken("807801279917465600-LzPaNKNi0CXkLCckpQljb8C7VxKJh2S");
		config.setOAuthAccessTokenSecret("He5DS9N9XoJGRITabztbkDZ7FBHUlo1Kp7stjbvtM3t3V");
		config.setJSONStoreEnabled(true);
		
		String id = "chryssac1";
		TwitterUser user = new TwitterUser(id, "");
		
		List<Status> tweets = user.getTweets(config);
		user.retrieveImageTweetsAndAssets();
		List<Status> imageTweets = user.getImageTweets();
		
		System.out.println("There were " +tweets.size() +" tweets associated with user " +id +".");
		System.out.println("Out of which, " +imageTweets.size() +" tweet(s) contain an image.");
		
		
		System.out.println(user.toJSON());
		
		try {
			user.saveImages();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}
}
