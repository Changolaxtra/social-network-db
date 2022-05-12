package dan.rojas.epam.db.social.db.generator;

import dan.rojas.epam.db.social.db.batch.UserBatchPreparedStatement;
import dan.rojas.epam.db.social.db.holder.PrimaryKeysHolder;
import dan.rojas.epam.db.social.db.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class UserGenerator extends BaseGenerator<User> implements CommandLineRunner {

  @Value("${db.user.insert.statement}")
  private String userInsertStatement;

  @Value("${db.users.size}")
  private int usersSize;

  private final JdbcTemplate jdbcTemplate;
  private final PrimaryKeysHolder primaryKeysHolder;

  @Override
  protected JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  @Override
  protected String getInsertStatement() {
    return userInsertStatement;
  }

  @Override
  public void run(String... args) throws Exception {
    final List<String> names = getListFromJson("names.json");
    final List<String> lastnames = getListFromJson("lastnames.json");
    final List<User> users = new ArrayList<>();
    log.info("{} Names and {} Lastnames loaded", names.size(), lastnames.size());

    log.info("Generating Users...");
    IntStream.range(0, usersSize)
        .forEach(i -> users.add(generateUser(getRandomFromList(names), getRandomFromList(lastnames))));

    insertBatch(users, UserBatchPreparedStatement::new);
    log.info("{} users inserted", usersSize);

    final List<String> usersIdList = users.stream().map(User::getId).collect(Collectors.toList());
    primaryKeysHolder.setUsersIdList(usersIdList);
  }

  private User generateUser(final String name, final String lastname) {
    return User.builder()
        .id(getRandomId())
        .firstName(name)
        .surname(lastname)
        .birthdate(getRandomDateBirth(60))
        .build();
  }

}

