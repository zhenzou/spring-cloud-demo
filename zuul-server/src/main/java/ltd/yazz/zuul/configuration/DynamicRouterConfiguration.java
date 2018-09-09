package ltd.yazz.zuul.configuration;

import ltd.yazz.zuul.core.CustomServiceRouteMapper;
import ltd.yazz.zuul.core.SimpleDynamicRouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return new CustomServiceRouteMapper(discovery, properties);
    }

    @Bean
    public DiscoveryClientRouteLocator dynamicRouteLocator() {
        return new SimpleDynamicRouteLocator(this.server.getServlet().getServletPrefix(), this.discovery, this.properties,
                this.serviceRouteMapper, this.registration);
    }
}
