package ltd.yazz.zuul.configuration;

import ltd.yazz.zuul.core.DynamicServiceRouteMapper;
import ltd.yazz.zuul.core.SimpleDynamicRouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态路由或者说，自注册路由
 * 两种方案：
 * 1. 自定义RouteMapper
 * 2. 自定义RouteLocator
 * 第一个方案肯定限制很多，如果不用其他的手段，不能 突破被忽略的服务
 * 第二个方案就很灵活了，可以满足所有的需求，现在只是简单的实现，复杂点的可以放到数据库中，而不是从服务注册的metadata中获取
 */
@Configuration
public class DynamicRouterConfiguration {

    @Autowired
    private DiscoveryClient discovery;

    @Autowired
    private ZuulProperties properties;

    @Autowired
    protected ServerProperties server;

    @Autowired(required = false)
    private Registration registration;

    @Autowired
    private ServiceRouteMapper serviceRouteMapper;

    public ServiceRouteMapper serviceRouteMapper() {
        return new DynamicServiceRouteMapper(discovery);
    }

    @Bean
    public DiscoveryClientRouteLocator dynamicRouteLocator() {
        return new SimpleDynamicRouteLocator(this.server.getServlet().getServletPrefix(), this.discovery, this.properties,
                this.serviceRouteMapper, this.registration);
    }
}
