package dan.rojas.epam.db.social.db.query;

import dan.rojas.epam.db.social.config.QueryConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Order(5)
@Component
@RequiredArgsConstructor
public class QueryOneHundredLikesAndFriends implements CommandLineRunner {

  private final JdbcTemplate jdbcTemplate;
  private final QueryConfig queryConfig;

  @Override
  public void run(String... args) throws Exception {
    log.info("Running Query...");
    final List<String> userNames =
        jdbcTemplate.query(queryConfig.getQueryFor100Likes(), (rs, rowNum) -> rs.getString(1));
    log.info("List Users with more than 100 likes and friends: {}", userNames);
    log.info("Size of Users with more than 100 likes and friends: {}", userNames.size());
  }

}
