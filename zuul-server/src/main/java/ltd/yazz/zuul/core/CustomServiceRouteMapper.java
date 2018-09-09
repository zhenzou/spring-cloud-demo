package ltd.yazz.zuul.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 自定义zuul前缀
 * 默认情况下，zuul代理在eureka注册的服务都是使用serviceId作为路径
 * 通过metadata+自定义Mapper实现服务自定义路径
 *
 * @author september zzzhen1994@gmail.com
 */
@Slf4j
public class CustomServiceRouteMapper implements ServiceRouteMapper {

    /**
     * 在metadata中配置服务路由的key
     */
    public static final String ROUTE_METADATA_KEY = "route";

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private DiscoveryClient discovery;

    private Map<String, String> routes = new ConcurrentHashMap<>();

    private ZuulProperties properties;

    public CustomServiceRouteMapper(DiscoveryClient discovery, ZuulProperties properties) {
        this.discovery = discovery;
        this.properties = properties;
        executor.scheduleAtFixedRate(this::loadRoutes, 0, 1, TimeUnit.MINUTES);
    }

    private void loadRoutes() {
        log.info("LOAD ROUTERS START");
        List<String> services = discovery.getServices();
        final Map<String, String> routes = new ConcurrentHashMap<>(services.size());
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
            this.properties.getRoutes().put(path, new ZuulProperties.ZuulRoute("/" + path + "/**", server));
            log.info("LOAD ROUTER {} => {}", path, server);
            routes.put(server, path);
        });
        this.routes = routes;
        log.info("LOAD ROUTERS END");
    }

    @Override
    public String apply(String serviceId) {
        final String path = this.routes.getOrDefault(serviceId, serviceId);
        log.debug("serviceId {} => {}", serviceId, path);
        return path;
    }
}
