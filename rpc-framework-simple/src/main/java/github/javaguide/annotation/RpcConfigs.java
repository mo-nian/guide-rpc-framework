package github.javaguide.annotation;

import github.javaguide.spring.CustomConfigBindingsRegistrar;
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
@Import(value = CustomConfigBindingsRegistrar.class)
public @interface RpcConfigs {

  /**
   * 配置类
   */
  RpcConfig[] value();
}
