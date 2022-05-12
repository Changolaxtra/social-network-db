package dan.rojas.epam.db.social.db.generator;

import dan.rojas.epam.db.social.db.batch.LikeBatchPreparedStatement;
import dan.rojas.epam.db.social.db.holder.PrimaryKeysHolder;
import dan.rojas.epam.db.social.db.model.Like;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class LikeGenerator extends BaseGenerator<Like> implements CommandLineRunner {

  private final JdbcTemplate jdbcTemplate;
  private final PrimaryKeysHolder primaryKeysHolder;

  @Value("${db.like.insert.statement}")
  private String likeInsertStatement;

  @Value("${db.like.max}")
  private int maxLikes;

  @Value("${db.like.min}")
  private int minLikes;

  @Value("${db.like.percentage}")
  private double percentage;

  @Override
  protected JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  @Override
  protected String getInsertStatement() {
    return likeInsertStatement;
  }

  @Override
  public void run(String... args) throws Exception {
    final Set<String> postsIdList = getRandomPartitionSet(primaryKeysHolder.getPostsIdList(), percentage);
    final List<String> usersIdList = primaryKeysHolder.getUsersIdList();
    final List<Like> likes = new ArrayList<>();

    log.info("Generating Likes...");
    for (final String postId : postsIdList) {
      final int totalLikesByPost = ThreadLocalRandom.current().nextInt(minLikes, maxLikes);
      final List<String> users = new ArrayList<>();
      int generatedLikes = 0;

      while (generatedLikes < totalLikesByPost) {
        final String userId = getRandomFromList(usersIdList);
        if (!users.contains(userId)) {
          likes.add(generateLike(postId, userId));
          users.add(userId);
          generatedLikes++;
        }
      }
    }

    insertBatch(likes, LikeBatchPreparedStatement::new);
    log.info("{} likes inserted", likes.size());
  }

  private Set<String> getRandomPartitionSet(final List<String> postsIdList, final double percentage) {
    final Set<String> result = new HashSet<>();
    final double total = postsIdList.size() * (percentage / 100);
    while (result.size() < total) {
      result.add(getRandomFromList(postsIdList));
    }
    return result;
  }

  private Like generateLike(final String postId, final String userId) {
    return Like.builder()
        .userId(userId)
        .postId(postId)
        .timestamp(getRandomDateBetween(2006, 2025))
        .build();
  }

}
