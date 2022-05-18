package dan.rojas.epam.db.social.db.generator;

import dan.rojas.epam.db.social.config.FriendshipConfig;
import dan.rojas.epam.db.social.db.batch.FriendshipBatchPreparedStatement;
import dan.rojas.epam.db.social.db.holder.PrimaryKeysHolder;
import dan.rojas.epam.db.social.db.model.Friendship;
import dan.rojas.epam.db.social.db.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Order(2)
@Component
@RequiredArgsConstructor
public class FriendshipGenerator implements CommandLineRunner {

  private final BatchRepository batchRepository;
  private final PrimaryKeysHolder primaryKeysHolder;
  private final RandomDataGenerator randomDataGenerator;
  private final FriendshipConfig friendshipConfig;


  @Override
  public void run(String... args) throws Exception {
    final List<Friendship> friendships = new ArrayList<>();
    final List<String> usersIdList = primaryKeysHolder.getUsersIdList();

    log.info("Generating Friendships...");
    for (final String userId : usersIdList) {
      final int totalFriendshipsByUser = ThreadLocalRandom.current()
          .nextInt(friendshipConfig.getMinFriendships(), friendshipConfig.getMaxFriendships());
      int generatedFriendships = 0;
      final List<String> currentFriendships = new ArrayList<>();
      while (generatedFriendships < totalFriendshipsByUser) {
        final String friendId = randomDataGenerator.getRandomFromList(usersIdList);
        if (isFriendshipValid(userId, friendId, currentFriendships)) {
          friendships.add(generateFriendship(userId, friendId));
          currentFriendships.add(friendId);
          generatedFriendships++;
        }
      }
    }

    batchRepository.insert(friendshipConfig.getFriendshipInsertStatement(),
        friendships, FriendshipBatchPreparedStatement::new);
    log.info("{} friendships inserted", friendships.size());
  }

  private Friendship generateFriendship(final String userId, final String friendId) {
    return Friendship.builder()
        .userId1(userId)
        .userId2(friendId)
        .timestamp(randomDataGenerator.getRandomDateBetween(2000, 2025))
        .build();
  }

  private boolean isFriendshipValid(final String userId,
                                    final String friendId,
                                    final List<String> currentFriendships) {
    return !StringUtils.equals(userId, friendId) && !currentFriendships.contains(friendId);
  }

}
