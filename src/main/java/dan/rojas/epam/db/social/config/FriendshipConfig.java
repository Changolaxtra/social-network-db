package dan.rojas.epam.db.social.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class FriendshipConfig {

  @Value("${db.friendship.insert.statement}")
  private String friendshipInsertStatement;

  @Value("${db.friendship.min}")
  private int minFriendships;

  @Value("${db.friendship.max}")
  private int maxFriendships;
}
