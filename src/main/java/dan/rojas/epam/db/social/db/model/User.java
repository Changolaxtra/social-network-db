package dan.rojas.epam.db.social.db.model;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class User {
  String id;
  String firstName;
  String surname;
  Date birthdate;
}
