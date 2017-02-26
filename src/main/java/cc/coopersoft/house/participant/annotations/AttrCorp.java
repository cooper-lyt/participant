package cc.coopersoft.house.participant.annotations;

import org.apache.deltaspike.security.api.authorization.SecurityBindingType;

import java.lang.annotation.*;

/**
 * Created by cooper on 25/02/2017.
 */

@Retention(value = RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@SecurityBindingType
public @interface AttrCorp {
}
