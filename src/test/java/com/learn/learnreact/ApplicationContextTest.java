package com.learn.learnreact;


import com.learn.learnreact.config.RootConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.learn.learnreact.config")
public class ApplicationContextTest {

  AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(RootConfig.class);

  @Test
  void findBean() {
    String[] beanDefinitionNames = ac.getBeanDefinitionNames();
    for (String s : beanDefinitionNames) {
      BeanDefinition beanDefinition = ac.getBeanDefinition(s);

      if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
        Object bean = ac.getBean(s);
        System.out.println("name => " + s);
        System.out.println("object => " + bean);
      }
    }
  }

  @Test
  void find() {
    Object suwan = ac.getBean("suwan");
    System.out.println(suwan);
  }

  @Test
  @DisplayName("빈 이름으로 조회")
  void findBeanByName() {
    ModelMapper getMapper = ac.getBean("getMapper", ModelMapper.class);
    System.out.println(getMapper.getClass().getTypeName());
    Assertions.assertThat(getMapper).isInstanceOf(ModelMapper.class);
  }

  @Test
  @DisplayName("타입으로 조회")
  void findBeanByType() {
    ModelMapper bean = ac.getBean(ModelMapper.class);
    System.out.println(bean.getClass().getTypeName());
    Assertions.assertThat(bean).isInstanceOf(ModelMapper.class);
  }

}
