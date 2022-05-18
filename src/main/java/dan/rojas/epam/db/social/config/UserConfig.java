package dan.rojas.epam.db.social.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class UserConfig {
  @Value("${db.user.insert.statement}")
  private String userInsertStatement;

  @Value("${db.users.size}")
  private int usersSize;
}
