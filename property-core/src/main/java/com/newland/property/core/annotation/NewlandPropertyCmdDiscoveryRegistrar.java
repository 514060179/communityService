package com.newland.property.core.annotation;

import com.newland.property.dto.CmdListenerDto;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义侦听扫描
 * Created by wuxw on 2018/7/2.
 */
public class NewlandPropertyCmdDiscoveryRegistrar implements ImportBeanDefinitionRegistrar,ResourceLoaderAware, BeanClassLoaderAware {

    private ResourceLoader resourceLoader;

    private ClassLoader classLoader;

    public NewlandPropertyCmdDiscoveryRegistrar(){

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        try {
            registerListener(importingClassMetadata,registry);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 注册侦听器到指定的包下，寻找标有特定注解的类并处理。
     *
     * @param metadata 提供注解元数据的信息，用于获取配置的元数据。
     * @param registry Bean定义注册表，用于注册Bean定义。
     * @throws NoSuchMethodException 如果尝试调用的方法不存在。
     * @throws InvocationTargetException 如果在尝试调用方法时发生异常。
     * @throws IllegalAccessException 如果没有权限访问方法。
     */
    public void registerListener(AnnotationMetadata metadata,
                                 BeanDefinitionRegistry registry) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 创建类路径扫描器
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        // 设置资源加载器
        scanner.setResourceLoader(this.resourceLoader);
        Set<String> basePackages;
        // 从注解中获取配置属性
        Map<String, Object> attrs = metadata
                .getAnnotationAttributes(NewlandPropertyCmdDiscovery.class.getName());

        // 获取并校验cmdPublishClass配置
        Object cmdPublishClassObj =  attrs.get("cmdPublishClass");

        Assert.notNull(cmdPublishClassObj,"NewlandPropertyCmdDiscovery 没有配置 cmdPublishClass 属性");

        Class<?> cmdPublishClass = (Class<?>) cmdPublishClassObj;

        // 创建注解类型过滤器，用于过滤标有NewlandPropertyCmd注解的类
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(
                NewlandPropertyCmd.class);

        scanner.addIncludeFilter(annotationTypeFilter);
        // 获取基包
        basePackages = getBasePackages(metadata);

        // 遍历基包，查找所有候选组件
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    // 验证注解的类是否为接口
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();

                    // 获取NewlandPropertyCmd注解的属性
                    Map<String, Object> attributes = annotationMetadata
                            .getAnnotationAttributes(
                                    NewlandPropertyCmd.class.getCanonicalName());
                    // 处理并获取Bean名称和服务码
                    String beanName = getListenerName(attributes,beanDefinition);
                    String serviceCode = attributes.get("serviceCode").toString();

                    // 使用反射调用cmdPublishClass的addListener方法，注册监听器
                    Method method = cmdPublishClass.getMethod("addListener", CmdListenerDto.class);
                    method.invoke(null,new CmdListenerDto(beanName,serviceCode));
                }
            }
        }
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false) {

            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {
                    // TODO until SPR-11711 will be resolved
                    if (beanDefinition.getMetadata().isInterface()
                            && beanDefinition.getMetadata()
                            .getInterfaceNames().length == 1
                            && Annotation.class.getName().equals(beanDefinition
                            .getMetadata().getInterfaceNames()[0])) {
                        try {
                            Class<?> target = ClassUtils.forName(
                                    beanDefinition.getMetadata().getClassName(),
                                    NewlandPropertyCmdDiscoveryRegistrar.this.classLoader);
                            return !target.isAnnotation();
                        }
                        catch (Exception ex) {
                            this.logger.error(
                                    "Could not load target class: "
                                            + beanDefinition.getMetadata().getClassName(),
                                    ex);

                        }
                    }
                    return true;
                }
                return false;

            }
        };
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(NewlandPropertyCmdDiscovery.class.getCanonicalName());

        Set<String> basePackages = new HashSet<String>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }


    /**
     * 获取监听器的名称。首先尝试从提供的listeners映射中获取"name"或"value"键对应的字符串，
     * 如果没有找到或者字符串为空，则基于bean定义的类名生成一个名称。
     *
     * @param listeners 包含监听器配置信息的映射，可能为null。
     * @param beanDefinition bean的定义信息，用于当listeners为空时，生成默认名称。
     * @return 监听器的名称字符串。
     */
    private String getListenerName(Map<String, Object> listeners,AnnotatedBeanDefinition beanDefinition) {
        // 如果listeners为null，基于bean定义的类名生成名称
        if (listeners == null) {
            String shortClassName = ClassUtils.getShortName(beanDefinition.getBeanClassName());
            return Introspector.decapitalize(shortClassName);
        }
        // 尝试从listeners中获取"value"键对应的名称
        String value = (String) listeners.get("value");
        // 如果"value"不存在或为空，尝试获取"name"键对应的名称
        if (!StringUtils.hasText(value)) {
            value = (String) listeners.get("name");
        }
        // 如果找到了有文本内容的名称，则返回该名称
        if (StringUtils.hasText(value)) {
            return value;
        }

        // 如果没有找到有效的名称，基于bean定义的类名生成默认名称
        String shortClassName = ClassUtils.getShortName(beanDefinition.getBeanClassName());
        value = Introspector.decapitalize(shortClassName);
        return value;
    }


}
