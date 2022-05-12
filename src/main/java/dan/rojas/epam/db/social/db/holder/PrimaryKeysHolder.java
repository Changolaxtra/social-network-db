package dan.rojas.epam.db.social.db.holder;

import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Setter
@Component
public class PrimaryKeysHolder {

  private List<String> usersIdList;
  private List<String> postsIdList;

  public List<String> getUsersIdList() {
    return Collections.unmodifiableList(Optional.ofNullable(usersIdList).orElse(new ArrayList<>()));
  }

  public List<String> getPostsIdList() {
    return Collections.unmodifiableList(Optional.ofNullable(postsIdList).orElse(new ArrayList<>()));
  }
}
