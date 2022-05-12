package dan.rojas.epam.db.social.db.generator;

import dan.rojas.epam.db.social.db.batch.PostBatchPreparedStatement;
import dan.rojas.epam.db.social.db.holder.PrimaryKeysHolder;
import dan.rojas.epam.db.social.db.model.Post;
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

@Slf4j
@Order(3)
@Component
@RequiredArgsConstructor
public class PostGenerator extends BaseGenerator<Post> implements CommandLineRunner {

  private final JdbcTemplate jdbcTemplate;
  private final PrimaryKeysHolder primaryKeysHolder;

  @Value("${db.post.insert.statement}")
  private String postInsertStatement;

  @Override
  protected JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  @Override
  protected String getInsertStatement() {
    return postInsertStatement;
  }

  @Override
  public void run(String... args) throws Exception {
    final List<Post> posts = new ArrayList<>();
    final List<String> quotes = getListFromJson("quotes.json");
    log.info("Quotes loaded: {}", quotes.size());
    final List<String> usersIdList = primaryKeysHolder.getUsersIdList();

    log.info("Generating Posts...");
    for(final String userId : usersIdList){
      for(final String quote: quotes){
        posts.add(generatePost(userId, quote));
      }
    }

    insertBatch(posts, PostBatchPreparedStatement::new);
    log.info("{} posts inserted", posts.size());

    final List<String> postsIdList = posts.stream().map(Post::getId).collect(Collectors.toList());
    primaryKeysHolder.setPostsIdList(postsIdList);
  }

  private Post generatePost(final String userId, final String quote) {
    return Post.builder()
        .id(getRandomId())
        .userId(userId)
        .text(quote)
        .timestamp(getRandomDateBetween(2000,2005))
        .build();
  }

}
