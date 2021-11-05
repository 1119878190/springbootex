package com.shiro.demo.configure;

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置类
 */
@Configuration
public class ShiroConfig  {


    //3. ShiroFileFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        // 添加shiro的内置过滤器
        /**
         * anno：无序认证，即可访问
         * authe: 必须认证了才能访问
         * user: 必须拥有记住我 才能使用
         * perms: 拥有对某个资源的权限才能访问；
         * role: 拥有某个角色权限才能访问
          */
        Map<String, String> filterMap = new LinkedHashMap<>();

        // 授权 用户需要某个权限才能访问 可以用注解 @RequiredPermissions
//        filterMap.put("/user/add","perms[user:add]");
//        filterMap.put("/user/update","perms[user:update]");

        // 登录拦截
//        filterMap.put("/user/add","authc");
//        filterMap.put("/user/update","authc");
        // 认证
        filterMap.put("/user/*","authc");



        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        // 如果没有认证则跳转到登录页面
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        // 设置未授权页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noauth");

        return shiroFilterFactoryBean;
    }


    // 2.DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 关联 userRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    // 1.创建Realm 对象，需要自定义类
    @Bean
    public UserRealm userRealm(){

        return new UserRealm();
    }

    /**
     * 开启Shiro的注解
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启aop注解支持
     *
     * @param defaultWebSecurityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier(value = "getDefaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
