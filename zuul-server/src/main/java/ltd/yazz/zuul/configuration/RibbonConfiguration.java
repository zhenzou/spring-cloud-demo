package ltd.yazz.zuul.configuration;

import lombok.extern.slf4j.Slf4j;
import ltd.yazz.zuul.core.ConnectionRefusedRetryPolicy;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicy;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryFactory;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RibbonConfiguration {


    @Bean
    public RibbonLoadBalancedRetryFactory buildPolicyFactory(SpringClientFactory factory) {

        return new RibbonLoadBalancedRetryFactory(factory) {
            @Override
            public LoadBalancedRetryPolicy createRetryPolicy(String service, ServiceInstanceChooser serviceInstanceChooser) {
                RibbonLoadBalancerContext lbContext = factory
                        .getLoadBalancerContext(service);
                return new ConnectionRefusedRetryPolicy(service, lbContext, serviceInstanceChooser, factory.getClientConfig(service));
            }
        };
    }

}
