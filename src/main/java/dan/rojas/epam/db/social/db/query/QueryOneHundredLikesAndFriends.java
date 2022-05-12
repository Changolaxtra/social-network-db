package dan.rojas.epam.db.social.db.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Order(5)
@Component
@RequiredArgsConstructor
public class QueryOneHundredLikesAndFriends implements CommandLineRunner {

  private final JdbcTemplate jdbcTemplate;

  @Value("${db.query.users.100likes.100friends}")
  private String query;

  @Override
  public void run(String... args) throws Exception {
    log.info("Running Query...");
    final List<String> userNames = jdbcTemplate.query(query, (rs, rowNum) -> rs.getString(1));
    log.info("List Users with more than 100 likes and friends: {}", userNames);
    log.info("Size of Users with more than 100 likes and friends: {}", userNames.size());
  }

}
