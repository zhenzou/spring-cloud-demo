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

    private final boolean okToRetryOnConnectErrors;
    private final boolean okToRetryOnAllError;

    public ConnectionRefusedRetryPolicy(String serviceId,
                                        RibbonLoadBalancerContext context,
                                        ServiceInstanceChooser loadBalanceChooser,
                                        IClientConfig clientConfig,
                                        boolean okToRetryOnConnectErrors,
                                        boolean okToRetryOnAllError) {

        super(serviceId, context, loadBalanceChooser, clientConfig);
        this.okToRetryOnConnectErrors = okToRetryOnConnectErrors;
        this.okToRetryOnAllError = okToRetryOnAllError;
    }


    @Override
    public boolean canRetry(LoadBalancedRetryContext context) {
        return super.canRetry(context) || isRetryableException(context.getLastThrowable());
    }

    private boolean isRetryableException(Throwable t) {
        boolean retryable = okToRetryOnAllError || (okToRetryOnConnectErrors && t instanceof ConnectException);
        if (retryable) {
            log.info("retry:{}", t.getClass().getName());
            return true;
        }
        log.info("do not retry:{}", t.getClass().getName());
        return false;

    }
}