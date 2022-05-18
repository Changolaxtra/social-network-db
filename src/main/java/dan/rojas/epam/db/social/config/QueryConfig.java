package dan.rojas.epam.db.social.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class QueryConfig {

  @Value("${db.query.users.100likes.100friends}")
  private String queryFor100Likes;

}
