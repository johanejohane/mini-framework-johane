package johane.annotation;

import java.lang.annotation.*;

/**
 * Protège une méthode de controller.
 * - sans "roles" : il suffit que l'utilisateur soit connecté ;
 * - avec "roles" : l'utilisateur doit en plus avoir l'un des rôles listés.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Auth {
    String[] roles() default {};
}