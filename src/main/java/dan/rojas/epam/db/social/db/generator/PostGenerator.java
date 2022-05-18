package dan.rojas.epam.db.social.db.generator;

import dan.rojas.epam.db.social.config.PostConfig;
import dan.rojas.epam.db.social.db.batch.PostBatchPreparedStatement;
import dan.rojas.epam.db.social.db.holder.PrimaryKeysHolder;
import dan.rojas.epam.db.social.db.model.Post;
import dan.rojas.epam.db.social.db.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Order(3)
@Component
@RequiredArgsConstructor
public class PostGenerator implements CommandLineRunner {

  private final BatchRepository batchRepository;
  private final PrimaryKeysHolder primaryKeysHolder;
  private final RandomDataGenerator randomDataGenerator;
  private final PostConfig postConfig;

  @Override
  public void run(String... args) throws Exception {
    final List<Post> posts = new ArrayList<>();
    final List<String> quotes = randomDataGenerator.getListFromJson("quotes.json");
    log.info("Quotes loaded: {}", quotes.size());
    final List<String> usersIdList = primaryKeysHolder.getUsersIdList();

    log.info("Generating Posts...");
    for (final String userId : usersIdList) {
      for (final String quote : quotes) {
        posts.add(generatePost(userId, quote));
      }
    }

    batchRepository.insert(postConfig.getPostInsertStatement(), posts, PostBatchPreparedStatement::new);
    log.info("{} posts inserted", posts.size());

    final List<String> postsIdList = posts.stream().map(Post::getId).collect(Collectors.toList());
    primaryKeysHolder.setPostsIdList(postsIdList);
  }

  private Post generatePost(final String userId, final String quote) {
    return Post.builder()
        .id(randomDataGenerator.getRandomId())
        .userId(userId)
        .text(quote)
        .timestamp(randomDataGenerator.getRandomDateBetween(2000, 2005))
        .build();
  }

}
