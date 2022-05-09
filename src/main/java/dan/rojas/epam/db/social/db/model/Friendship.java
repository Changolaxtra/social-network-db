package dan.rojas.epam.db.social.db.model;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class Friendship {
  String userId1;
  String userId2;
  Date timestamp;
}
