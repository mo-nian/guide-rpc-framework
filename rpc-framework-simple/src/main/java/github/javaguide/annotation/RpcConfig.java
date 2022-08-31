package github.javaguide.annotation;

import github.javaguide.spring.CustomConfigBindingRegistrar;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * @author dxu2
 * @date 2022/8/30
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = CustomConfigBindingRegistrar.class)
public @interface RpcConfig {

  /**
   * 前缀
   */
  String prefix() default "";

  /**
   * 配置类型
   */
  Class<?> type();
}
