package github.javaguide.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author dxu2
 * @date 2022/8/31
 */
@Component
public final class SpringUtil {

  private static ApplicationContext ac;

  public static <T> T getBean(Class<T> tClass) {
    return ac.getBean(tClass);
  }

  public static Object getBean(String beanName) {
    return ac.getBean(beanName);
  }

  public static void setApplicationContext(ApplicationContext applicationContext) {
    ac = applicationContext;
  }


}
