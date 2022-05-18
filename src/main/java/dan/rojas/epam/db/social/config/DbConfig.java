package dan.rojas.epam.db.social.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class DbConfig {

  @Value("${db.id.length}")
  private int idLength;

  @Value("${db.batch.size}")
  private int batchSize;

  @Value("${db.batch.threads}")
  private int threads;

}
