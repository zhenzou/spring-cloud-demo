package ltd.yazz.zuul.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class SimpleDynamicRouteLocator extends DiscoveryClientRouteLocator {

    private static final String ROUTE_METADATA_KEY = "route";

    private DiscoveryClient discovery;

    public SimpleDynamicRouteLocator(String servletPath, DiscoveryClient discovery, ZuulProperties properties, ServiceRouteMapper serviceRouteMapper, ServiceInstance localServiceInstance) {
        super(servletPath, discovery, properties, serviceRouteMapper, localServiceInstance);
        this.discovery = discovery;
    }

    @Override
    protected LinkedHashMap<String, ZuulProperties.ZuulRoute> locateRoutes() {
        if (discovery == null) {
            return super.locateRoutes();
        }
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>(super.locateRoutes());

        log.info("LOAD ROUTERS START");
        List<String> services = discovery.getServices();
        services.forEach(server -> {
            List<ServiceInstance> instances = discovery.getInstances(server);
            if (CollectionUtils.isEmpty(instances)) {
                return;
            }
            ServiceInstance instance = instances.get(0);
            String path = instance.getMetadata().get(ROUTE_METADATA_KEY);
            if (StringUtils.isEmpty(path)) {
                return;
            }

            // 忽略zuul的ignore-service配置
            path = "/" + path + "/**";
            routesMap.put(path, new ZuulProperties.ZuulRoute(path, server));
            log.info("LOAD ROUTER {} => {}", path, server);
        });
        if (routesMap.get(DEFAULT_ROUTE) != null) {
            ZuulProperties.ZuulRoute defaultRoute = routesMap.get(DEFAULT_ROUTE);
            // Move the defaultServiceId to the end
            routesMap.remove(DEFAULT_ROUTE);
            routesMap.put(DEFAULT_ROUTE, defaultRoute);
        }
        log.info("LOAD ROUTERS END");
        return routesMap;
    }
}
