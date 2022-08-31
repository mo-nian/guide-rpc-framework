package github.javaguide.spring;

import com.google.common.collect.Maps;
import github.javaguide.annotation.RpcConfig;
import java.util.Map;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author dxu2
 * @date 2022/8/30
 */
public class CustomConfigBindingRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

  private ConfigurableEnvironment environment;

  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

    AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(RpcConfig.class.getName()));
    registerBeanDefinition(attributes, registry);
  }

  protected void registerBeanDefinition(AnnotationAttributes attributes, BeanDefinitionRegistry registry) {
    // 获取前缀
    String prefix = environment.resolvePlaceholders(attributes.getString("prefix"));
    // 配置类
    Class<?> configClass = attributes.getClass("type");

    MutablePropertySources propertySources = environment.getPropertySources();
    AbstractEnvironment abstractEnvironment = new AbstractEnvironment() {
    };
    for (PropertySource<?> propertySource : propertySources) {
      abstractEnvironment.getPropertySources().addLast(propertySource);
    }

    Map<String, Object> configProperties = getConfigProperties(abstractEnvironment, prefix);

    BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(configClass);
    String beanName = BeanDefinitionReaderUtils.generateBeanName(builder.getRawBeanDefinition(), registry);

    configProperties.forEach(builder::addPropertyValue);

    // register beanDefinition
    registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
  }

  private Map<String, Object> getConfigProperties(AbstractEnvironment abstractEnvironment, String prefix) {

    Map<String, Object> propertiesMap = Maps.newHashMap();

    prefix += ".";
    for (PropertySource<?> propertySource : abstractEnvironment.getPropertySources()) {
      if (propertySource instanceof EnumerablePropertySource) {
        EnumerablePropertySource<?> enumerablePropertySource = (EnumerablePropertySource<?>) propertySource;
        String[] propertyNames = enumerablePropertySource.getPropertyNames();
        for (String propertyName : propertyNames) {
          if (!propertiesMap.containsKey(propertyName) && propertyName.startsWith(prefix)) {
            String configName = propertyName.substring(prefix.length());
            Object value = propertySource.getProperty(propertyName);
            if (value instanceof String) {
              value = environment.resolvePlaceholders((String) value);
            }
            propertiesMap.put(configName, value);
          }
        }
      }
    }

    return propertiesMap;
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = (ConfigurableEnvironment) environment;
  }
}
