package dan.rojas.epam.db.social.db.model;

import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
public class Quote {
  String quote;
  String author;
}
