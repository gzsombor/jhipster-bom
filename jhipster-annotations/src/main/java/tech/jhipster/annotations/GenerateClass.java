/**
 * 
 */
package tech.jhipster.annotations;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(SOURCE)
@Target({TYPE, PACKAGE})
@Repeatable(GenerateClasses.class)
/**
 *
 */
public @interface GenerateClass {
    String name();
    String packageName() default "";
    Field[] extraFields() default {};
}