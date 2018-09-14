package com.freshman.webapi.shiro;

import com.freshman.webapi.dao.UserMapper;
import com.freshman.webapi.service.UserService;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    private String patternUrl = "/shiro";

    @Bean
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return em;
    }

    @Bean(name = "myShiroRealm")
    public MyShiroRealm myShiroRealm(EhCacheManager cacheManager) {
        MyShiroRealm realm = new MyShiroRealm();
        realm.setCacheManager(cacheManager);
        return realm;
    }

    /**
     * 注册DelegatingFilterProxy（Shiro）
     * 集成Shiro有2种方法：
     * 1. 按这个方法自己组装一个FilterRegistrationBean（这种方法更为灵活，可以自己定义UrlPattern，
     * 在项目使用中你可能会因为一些很但疼的问题最后采用它， 想使用它你可能需要看官网或者已经很了解Shiro的处理原理了）
     * 2. 直接使用ShiroFilterFactoryBean（这种方法比较简单，其内部对ShiroFilter做了组装工作，无法自己定义UrlPattern，
     * 默认拦截 /*）
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //  该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        // 可以自己灵活的定义很多，避免一些根本不需要被Shiro处理的请求被包含进来
        filterRegistration.addUrlPatterns(patternUrl + "/*");
        return filterRegistration;
    }

    /**
     * Shiro生命周期处理器
     * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(MyShiroRealm myShiroRealm) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(myShiroRealm);
        //用户授权/认证信息Cache, 采用EhCache 缓存
        dwsm.setCacheManager(getEhCacheManager());
        return dwsm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }

    /**
     * 加载shiroFilter权限控制规则（从数据库读取然后配置）
     */
    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean, UserService stuService, UserMapper scoreDao) {
        //下面这些规则配置最好配置到配置文件中
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // authc：该过滤器下的页面必须验证后才能访问，它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
        // 这里为了测试，只限制/user，实际开发中请修改为具体拦截的请求规则
        filterChainDefinitionMap.put(patternUrl + "/user", "authc");
        // anon：它对应的过滤器里面是空的,什么都没做
        System.out.println("从数据库读取权限规则，加载到shiroFilter中");
        // 这里为了测试，固定写死的值，也可以从数据库或其他配置中读取
        filterChainDefinitionMap.put(patternUrl + "/user/edit/**", "authc,perms[user:edit]");
        filterChainDefinitionMap.put(patternUrl + "/user/delete**", "authc,perms[user:delete");
        filterChainDefinitionMap.put(patternUrl + "/sLogin", "anon");
        filterChainDefinitionMap.put(patternUrl + "/sLoginAjax", "anon");
        filterChainDefinitionMap.put(patternUrl + "/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }

    /**
     * ShiroFilter
     * 注意这里参数中的 StudentService 和 IScoreDao 只是一个例子，因为我们在这里可以用这样的方式获取到相关访问数据库的对象，
     * 然后读取数据库相关配置，配置到 shiroFilterFactoryBean 的访问规则中。实际项目中，请使用自己的Service来处理业务逻辑。
     *
     * @param stuService
     * @param scoreDao
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager, UserService stuService, UserMapper scoreDao) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new MyShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl(patternUrl + "/sLogin");
        // 登录成功后要跳转的连接
        shiroFilterFactoryBean.setSuccessUrl(patternUrl + "/user");
        // 无权限的连接
        shiroFilterFactoryBean.setUnauthorizedUrl(patternUrl + "/403");

        loadShiroFilterChain(shiroFilterFactoryBean, stuService, scoreDao);
        return shiroFilterFactoryBean;
    }
}
