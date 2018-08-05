package ltd.yazz.zuul.configuration;

import ltd.yazz.zuul.core.CustomServiceRouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Router {

    @Autowired
    private DiscoveryClient discovery;

    @Bean
    public ServiceRouteMapper serviceRouteMapper() {
        return new CustomServiceRouteMapper(discovery);
    }
}
