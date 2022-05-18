package dan.rojas.epam.db.social.db.generator;

import dan.rojas.epam.db.social.config.UserConfig;
import dan.rojas.epam.db.social.db.batch.UserBatchPreparedStatement;
import dan.rojas.epam.db.social.db.holder.PrimaryKeysHolder;
import dan.rojas.epam.db.social.db.model.User;
import dan.rojas.epam.db.social.db.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class UserGenerator implements CommandLineRunner {

  private final BatchRepository batchRepository;
  private final PrimaryKeysHolder primaryKeysHolder;
  private final RandomDataGenerator randomDataGenerator;
  private final UserConfig userConfig;

  @Override
  public void run(String... args) throws Exception {
    final List<String> names = randomDataGenerator.getListFromJson("names.json");
    final List<String> lastnames = randomDataGenerator.getListFromJson("lastnames.json");
    final List<User> users = new ArrayList<>();
    log.info("{} Names and {} Lastnames loaded", names.size(), lastnames.size());

    log.info("Generating Users...");
    IntStream.range(0, userConfig.getUsersSize())
        .forEach(i ->
            users.add(generateUser(randomDataGenerator.getRandomFromList(names),
                randomDataGenerator.getRandomFromList(lastnames))));

    batchRepository.insert(userConfig.getUserInsertStatement(), users, UserBatchPreparedStatement::new);
    log.info("{} users inserted", users.size());

    final List<String> usersIdList = users.stream().map(User::getId).collect(Collectors.toList());
    primaryKeysHolder.setUsersIdList(usersIdList);
  }

  private User generateUser(final String name, final String lastname) {
    return User.builder()
        .id(randomDataGenerator.getRandomId())
        .firstName(name)
        .surname(lastname)
        .birthdate(randomDataGenerator.getRandomDateBirth(60))
        .build();
  }

}

