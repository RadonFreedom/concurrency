package fre.shown.concurrency;

import java.lang.annotation.*;

/**
 *
 * 被注解的类是一个demo类，至少包含一个可直接运行的方法
 *
 * @author Radon Freedom
 * created at 2019.02.21 12:28
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Demo {
}
