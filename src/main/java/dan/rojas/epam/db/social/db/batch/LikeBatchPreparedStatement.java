package dan.rojas.epam.db.social.db.batch;

import dan.rojas.epam.db.social.db.model.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LikeBatchPreparedStatement implements BatchPreparedStatementSetter {

  private final List<Like> likes;

  @Override
  public void setValues(PreparedStatement ps, int i) throws SQLException {
    final Like like = likes.get(i);
    ps.setString(1, like.getPostId());
    ps.setString(2, like.getUserId());
    ps.setDate(3, new Date(like.getTimestamp().getTime()));
  }

  @Override
  public int getBatchSize() {
    return Optional.ofNullable(likes)
        .map(List::size)
        .orElse(0);
  }
}
