package dan.rojas.epam.db.social.db.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public abstract class BaseGenerator<T> {

  @Value("${db.id.length}")
  private int idLength;

  @Value("${db.batch.size}")
  protected int batchSize;

  @Value("${db.batch.threads}")
  protected int threads;

  protected abstract JdbcTemplate getJdbcTemplate();

  protected abstract String getInsertStatement();

  protected String getRandomId() {
    return RandomStringUtils.randomAlphanumeric(idLength);
  }

  protected static Date getRandomDateBirth(final int maxYears) {
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

  protected static Date getRandomDateBetween(final int yearStart, final int yearEnd) {
    final Calendar calendar = Calendar.getInstance();
    calendar.set(yearStart, Calendar.JANUARY, 1);
    final Date startDate = calendar.getTime();
    calendar.set(yearEnd, Calendar.DECEMBER, 31);
    final Date endDate = calendar.getTime();
    return getRandomDateBetween(startDate, endDate);
  }

  protected static Date getRandomDateBetween(final Date start, final Date end) {
    final long startMillis = start.getTime();
    final long endMillis = end.getTime();
    final long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
    return new Date(randomMillisSinceEpoch);
  }

  protected List<String> getListFromJson(final String jsonFile) throws IOException {
    final ObjectMapper mapper = new ObjectMapper();
    final InputStream inputStream = new ClassPathResource(jsonFile).getInputStream();
    return mapper.readValue(inputStream, new TypeReference<List<String>>() {
    });
  }

  protected String getRandomFromList(final List<String> list) {
    return list.get(ThreadLocalRandom.current().nextInt(list.size() - 1));
  }

  protected void insertBatch(final List<T> list, Function<List<T>, BatchPreparedStatementSetter> mapperFunction) {
    final List<List<T>> partitions = ListUtils.partition(list, batchSize);
    final ExecutorService executor = Executors.newFixedThreadPool(threads);

    final CompletableFuture[] completableFutures = partitions
        .stream()
        .map(mapperFunction)
        .map(batchPreparedStatement -> batchInsert(batchPreparedStatement, executor))
        .toArray(CompletableFuture[]::new);
    CompletableFuture.allOf(completableFutures).join();
    executor.shutdown();
  }

  @Transactional
  private CompletableFuture<Void> batchInsert(final BatchPreparedStatementSetter batchPreparedStatementSetter,
                                              final Executor executor) {
    return CompletableFuture.runAsync(() ->
        getJdbcTemplate().batchUpdate(getInsertStatement(), batchPreparedStatementSetter), executor);
  }

}
