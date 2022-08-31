package github.javaguide.spring;

import github.javaguide.annotation.RpcConfigs;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author dxu2
 * @date 2022/8/30
 */
public class CustomConfigBindingsRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

  private ConfigurableEnvironment environment;

  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(RpcConfigs.class.getName()));
    assert annotationAttributes != null;
    AnnotationAttributes[] values =  annotationAttributes.getAnnotationArray("value");

    CustomConfigBindingRegistrar customConfigBindingRegistrar = new CustomConfigBindingRegistrar();
    customConfigBindingRegistrar.setEnvironment(environment);
    for (AnnotationAttributes element : values) {
      customConfigBindingRegistrar.registerBeanDefinition(element, registry);
    }

  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = (ConfigurableEnvironment) environment;
  }
}
