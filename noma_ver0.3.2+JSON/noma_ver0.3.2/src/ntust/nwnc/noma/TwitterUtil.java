package ntust.nwnc.noma;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public final class TwitterUtil {

    public static String TWITTER_CONSUMER_KEY = "hRVUCROxszSEOGwkCluzcdM9S";
    public static String TWITTER_CONSUMER_SECRET = "2VYVKQ06DOLAqz6omlz0DT3287L6c3eExDovsyU8mdHo3ivnJg";
    public static String TWITTER_CALLBACK_URL = "";
    
    private RequestToken requestToken = null;
    private TwitterFactory twitterFactory = null;
    private Twitter twitter;

    public TwitterUtil() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
        Configuration configuration = configurationBuilder.build();
        twitterFactory = new TwitterFactory(configuration);
        twitter = twitterFactory.getInstance();
    }

    public TwitterFactory getTwitterFactory()
    {
        return twitterFactory;
    }

    public void setTwitterFactory(AccessToken accessToken)
    {
        twitter = twitterFactory.getInstance(accessToken);
    }

    public Twitter getTwitter()
    {
        return twitter;
    }
    
    public RequestToken getRequestToken() {
        if (requestToken == null) {
            try {
                requestToken = twitterFactory.getInstance().getOAuthRequestToken(TWITTER_CALLBACK_URL);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
        return requestToken;
    }

    static TwitterUtil instance = new TwitterUtil();

    public static TwitterUtil getInstance() {
        return instance;
    }

    public void reset() {
        instance = new TwitterUtil();
    }
}
