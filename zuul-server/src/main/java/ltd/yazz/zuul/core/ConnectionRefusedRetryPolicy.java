package ltd.yazz.zuul.core;


import com.netflix.client.config.IClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryContext;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryPolicy;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;

import java.net.ConnectException;

/**
 *
 */
@Slf4j
public class ConnectionRefusedRetryPolicy extends RibbonLoadBalancedRetryPolicy {

    private final boolean okToRetryOnConnectErrors = true;
    private final boolean okToRetryOnAllErrors = false;

    public ConnectionRefusedRetryPolicy(String serviceId, RibbonLoadBalancerContext context, ServiceInstanceChooser loadBalanceChooser, IClientConfig clientConfig) {
        super(serviceId, context, loadBalanceChooser, clientConfig);
    }


    @Override
    public boolean canRetry(LoadBalancedRetryContext context) {
        return super.canRetry(context) || isRetryException(context.getLastThrowable());
    }

    public boolean isRetryException(Throwable t) {
        log.info("retry:{}",t.getClass().getName());
        if (okToRetryOnConnectErrors) {
            return t instanceof ConnectException;
        }
        return false;

    }
}