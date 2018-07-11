package javacomp;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class Draw {

  private static final ZoneId MET_TIMEZONE = ZoneId.of("Europe/Berlin");

  public static void main(String[] args) throws TwitterException {
    System.out.println("Drawing happens at " + getCurrentTime());

    ZonedDateTime from = getDrawingTime();
    System.out.println("Drawing time: " + from);

    long statusId = 1015647297638674432L;
    List<String> retweeters = getRetweeters(from, statusId);
    System.out.println("There are " + retweeters.size() + " participants: " + retweeters);

    String winner = determineWinner(retweeters);
    System.out.println("And the winner is >>> " + winner + " <<<");
  }

  private static ZonedDateTime getCurrentTime() {
    return ZonedDateTime.now(MET_TIMEZONE);
  }

  private static List<String> getRetweeters(ZonedDateTime drawTime, long statusId)
      throws TwitterException {
    Date drawDate = Date.from(drawTime.toInstant());
    Twitter twitter = TwitterFactory.getSingleton();
    return twitter
        .tweets()
        .getRetweets(statusId)
        .stream()
        .filter(status -> status.getCreatedAt().before(drawDate))
        .map(Draw::toName)
        .collect(Collectors.toList());
  }

  private static ZonedDateTime getDrawingTime() {
    return ZonedDateTime.of(LocalDateTime.of(2018, 7, 10, 20, 0), MET_TIMEZONE);
  }

  private static String determineWinner(List<String> retweets) {
    int winner = new Random().nextInt(retweets.size() - 1);
    return retweets.get(winner);
  }

  private static String toName(Status status) {
    return "@" + status.getUser().getScreenName();
  }
}
