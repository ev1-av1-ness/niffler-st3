package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.model.AuthorityEntity;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({DBUserExtension.class})
public @interface DBUser {
    String username();

    String password();
}
