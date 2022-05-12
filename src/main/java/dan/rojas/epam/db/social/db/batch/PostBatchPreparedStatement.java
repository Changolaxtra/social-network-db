package dan.rojas.epam.db.social.db.batch;

import dan.rojas.epam.db.social.db.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostBatchPreparedStatement implements BatchPreparedStatementSetter {

  private final List<Post> posts;

  @Override
  public void setValues(PreparedStatement ps, int i) throws SQLException {
    final Post post = posts.get(i);
    ps.setString(1, post.getId());
    ps.setString(2, post.getUserId());
    ps.setString(3, post.getText());
    ps.setDate(4, new Date(post.getTimestamp().getTime()));
  }

  @Override
  public int getBatchSize() {
    return Optional.ofNullable(posts)
        .map(List::size)
        .orElse(0);
  }
}
