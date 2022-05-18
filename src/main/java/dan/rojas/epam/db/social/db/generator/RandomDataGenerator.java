package dan.rojas.epam.db.social.db.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomDataGenerator {

  public String getRandomId() {
    return RandomStringUtils.randomAlphanumeric(10);
  }

  public Date getRandomDateBirth(final int maxYears) {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.YEAR, -maxYears);
    Date startInclusive = calendar.getTime();
    Date endExclusive = new Date();
    final long startMillis = startInclusive.getTime();
    final long endMillis = endExclusive.getTime();
    final long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
    return new Date(randomMillisSinceEpoch);
  }

  public Date getRandomDateBetween(final int yearStart, final int yearEnd) {
    final Calendar calendar = Calendar.getInstance();
    calendar.set(yearStart, Calendar.JANUARY, 1);
    final Date startDate = calendar.getTime();
    calendar.set(yearEnd, Calendar.DECEMBER, 31);
    final Date endDate = calendar.getTime();
    return getRandomDateBetween(startDate, endDate);
  }

  public Date getRandomDateBetween(final Date start, final Date end) {
    final long startMillis = start.getTime();
    final long endMillis = end.getTime();
    final long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
    return new Date(randomMillisSinceEpoch);
  }

  public List<String> getListFromJson(final String jsonFile) throws IOException {
    final ObjectMapper mapper = new ObjectMapper();
    final InputStream inputStream = new ClassPathResource(jsonFile).getInputStream();
    return mapper.readValue(inputStream, new TypeReference<List<String>>() {
    });
  }

  public String getRandomFromList(final List<String> list) {
    return list.get(ThreadLocalRandom.current().nextInt(list.size() - 1));
  }


}
