package kr.toxicity.inventory.api.annotation;

import kr.toxicity.inventory.api.enums.AnimationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InventoryAnimation {
    String asset();
    AnimationType type();
    long playTime();
    String xEquation() default "0";
    String yEquation() default "0";
}
