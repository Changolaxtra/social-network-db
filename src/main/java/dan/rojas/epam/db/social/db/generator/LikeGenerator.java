package dan.rojas.epam.db.social.db.generator;

import dan.rojas.epam.db.social.config.LikeConfig;
import dan.rojas.epam.db.social.db.batch.LikeBatchPreparedStatement;
import dan.rojas.epam.db.social.db.holder.PrimaryKeysHolder;
import dan.rojas.epam.db.social.db.model.Like;
import dan.rojas.epam.db.social.db.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Order(4)
@Component
@RequiredArgsConstructor
public class LikeGenerator implements CommandLineRunner {

  private final BatchRepository batchRepository;
  private final PrimaryKeysHolder primaryKeysHolder;
  private final RandomDataGenerator randomDataGenerator;
  private final LikeConfig likeConfig;


  @Override
  public void run(String... args) throws Exception {
    final Set<String> postsIdList =
        getRandomPartitionSet(primaryKeysHolder.getPostsIdList(), likeConfig.getPercentage());
    final List<String> usersIdList = primaryKeysHolder.getUsersIdList();
    final List<Like> likes = new ArrayList<>();

    log.info("Generating Likes...");
    for (final String postId : postsIdList) {
      final int totalLikesByPost = ThreadLocalRandom.current()
          .nextInt(likeConfig.getMinLikes(), likeConfig.getMaxLikes());
      final List<String> users = new ArrayList<>();
      int generatedLikes = 0;

      while (generatedLikes < totalLikesByPost) {
        final String userId = randomDataGenerator.getRandomFromList(usersIdList);
        if (!users.contains(userId)) {
          likes.add(generateLike(postId, userId));
          users.add(userId);
          generatedLikes++;
        }
      }
    }

    batchRepository.insert(likeConfig.getLikeInsertStatement(),likes, LikeBatchPreparedStatement::new);
    log.info("{} likes inserted", likes.size());
  }

  private Set<String> getRandomPartitionSet(final List<String> postsIdList, final double percentage) {
    final Set<String> result = new HashSet<>();
    final double total = postsIdList.size() * (percentage / 100);
    while (result.size() < total) {
      result.add(randomDataGenerator.getRandomFromList(postsIdList));
    }
    return result;
  }

  private Like generateLike(final String postId, final String userId) {
    return Like.builder()
        .userId(userId)
        .postId(postId)
        .timestamp(randomDataGenerator.getRandomDateBetween(2024, 2025))
        .build();
  }

}
