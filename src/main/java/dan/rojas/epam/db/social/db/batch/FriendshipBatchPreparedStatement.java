package dan.rojas.epam.db.social.db.batch;

import dan.rojas.epam.db.social.db.model.Friendship;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FriendshipBatchPreparedStatement implements BatchPreparedStatementSetter {

  private final List<Friendship> friendships;

  @Override
  public void setValues(PreparedStatement ps, int i) throws SQLException {
    final Friendship friendship = friendships.get(i);
    ps.setString(1, friendship.getUserId1());
    ps.setString(2, friendship.getUserId2());
    ps.setDate(3, new Date(friendship.getTimestamp().getTime()));
  }

  @Override
  public int getBatchSize() {
    return Optional.ofNullable(friendships)
        .map(List::size)
        .orElse(0);
  }
}
