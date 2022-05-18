package dan.rojas.epam.db.social.db.repository;

import dan.rojas.epam.db.social.config.DbConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Repository
@RequiredArgsConstructor
public class BatchRepository {

  private ExecutorService executor;

  private final JdbcTemplate jdbcTemplate;
  private final DbConfig dbConfig;

  @PostConstruct
  public void init() {
    executor = Executors.newFixedThreadPool(dbConfig.getThreads());
  }

  public <T> void insert(final String insertStatement,
                         final List<T> list,
                         final Function<List<T>, BatchPreparedStatementSetter> mapperFunction) {

    final List<List<T>> partitions = ListUtils.partition(list, dbConfig.getBatchSize());
    final CompletableFuture[] completableFutures = partitions
        .stream()
        .map(mapperFunction)
        .map(batchPreparedStatement -> batchInsert(insertStatement, batchPreparedStatement))
        .toArray(CompletableFuture[]::new);
    CompletableFuture.allOf(completableFutures).join();
    executor.shutdown();
  }

  @Transactional
  private CompletableFuture<Void> batchInsert(final String insertStatement,
                                              final BatchPreparedStatementSetter batchPreparedStatementSetter) {
    return CompletableFuture.runAsync(() ->
        jdbcTemplate.batchUpdate(insertStatement, batchPreparedStatementSetter), executor);
  }

}

