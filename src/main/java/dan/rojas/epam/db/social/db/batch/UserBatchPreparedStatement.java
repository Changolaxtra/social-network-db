package dan.rojas.epam.db.social.db.batch;

import dan.rojas.epam.db.social.db.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class UserBatchPreparedStatement implements BatchPreparedStatementSetter {

  private final List<User> users;

  @Override
  public void setValues(PreparedStatement ps, int i) throws SQLException {
    final User user = users.get(i);
    ps.setString(1, user.getId());
    ps.setString(2, user.getFirstName());
    ps.setString(3, user.getSurname());
    ps.setDate(4, new Date(user.getBirthdate().getTime()));
  }

  @Override
  public int getBatchSize() {
    return users.size();
  }
}
