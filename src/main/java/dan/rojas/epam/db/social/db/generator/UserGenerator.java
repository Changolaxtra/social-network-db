package dan.rojas.epam.db.social.db.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dan.rojas.epam.db.social.db.batch.UserBatchPreparedStatement;
import dan.rojas.epam.db.social.db.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class UserGenerator extends AbstractGenerator implements CommandLineRunner {

  @Value("${db.batch.size}")
  private int batchSize;

  @Value("${db.users.size}")
  private int usersSize;

  @Value("${db.user.insert.statement}")
  private String userInsertStatement;


  private final JdbcTemplate jdbcTemplate;

  @Override
  public void run(String... args) throws Exception {
    final List<String> names = getListFromJson("names.json");
    final List<String> lastnames = getListFromJson("lastnames.json");
    final List<User> users = new ArrayList<>();
    final int namesSize = names.size();
    final int lastnamesSize = lastnames.size();
    log.info("{} Names and {} Lastnames loaded", namesSize, lastnamesSize);

    final Random random = new Random();
    IntStream.range(0, usersSize)
        .forEach(i -> users.add(generateUser(names.get(random.nextInt(namesSize-1)),
            lastnames.get(random.nextInt(lastnamesSize-1)))));

    final ExecutorService executor = Executors.newFixedThreadPool(10);

    final AtomicInteger subLists = new AtomicInteger();
    users.stream()
        .collect(Collectors.groupingBy(userList -> subLists.getAndIncrement() / batchSize))
        .values()
        .forEach(this::userBatchInsert);

  }

  private List<String> getListFromJson(final String jsonFile) throws IOException {
    final ObjectMapper mapper = new ObjectMapper();
    final InputStream inputStream = new ClassPathResource(jsonFile).getInputStream();
    return mapper.readValue(inputStream, new TypeReference<List<String>>(){});
  }

  private User generateUser(final String name, final String lastname) {
    return User.builder()
        .id(getRandomId())
        .firstName(name)
        .surname(lastname)
        .birthdate(getRandomDate(60))
        .build();
  }

  private void userBatchInsert(final List<User> partition) {
    jdbcTemplate.batchUpdate(userInsertStatement, new UserBatchPreparedStatement(partition));
  }

}

