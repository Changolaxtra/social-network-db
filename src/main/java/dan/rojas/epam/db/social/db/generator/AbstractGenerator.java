package dan.rojas.epam.db.social.db.generator;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class AbstractGenerator {

  @Value("${db.id.length}")
  private Integer idLength;

  protected String getRandomId(){
    return RandomStringUtils.randomAlphanumeric(idLength);
  }

  public static Date getRandomDate(final int maxYears) {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.YEAR, -maxYears);
    Date startInclusive = calendar.getTime();
    Date endExclusive = new Date();
    long startMillis = startInclusive.getTime();
    long endMillis = endExclusive.getTime();
    long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
    return new Date(randomMillisSinceEpoch);
  }

}
