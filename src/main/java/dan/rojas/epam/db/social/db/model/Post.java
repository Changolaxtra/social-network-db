package dan.rojas.epam.db.social.db.model;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class Post {
  String id;
  String userId;
  String text;
  Date timestamp;
}
