package dan.rojas.epam.db.social.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PostConfig {

  @Value("${db.post.insert.statement}")
  private String postInsertStatement;

}
