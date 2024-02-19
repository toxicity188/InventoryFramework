package kr.toxicity.inventory.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InventoryText {
    String asset();
    int x() default 0;
    int y() default 0;
    int scale() default 16;
    int space() default 2;
    double multiplier() default 1.0;
}
