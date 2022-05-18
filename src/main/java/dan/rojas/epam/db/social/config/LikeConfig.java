package dan.rojas.epam.db.social.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class LikeConfig {

  @Value("${db.like.insert.statement}")
  private String likeInsertStatement;

  @Value("${db.like.max}")
  private int maxLikes;

  @Value("${db.like.min}")
  private int minLikes;

  @Value("${db.like.percentage}")
  private double percentage;
}
