package io.aeon.balancer;

import com.netflix.discovery.EurekaClient;
import io.aeon.exception.ServiceDiscoveryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;

class OcrParserInstanceListSuppler implements ServiceInstanceListSupplier {
	
	@Value("${client.name}")
	private String serviceId;
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Autowired
	private EurekaClient eurekaClient;
	
	@Override
	public String getServiceId() {
		return serviceId;
	}
	
	@Override
	public Flux<List<ServiceInstance>> get() {
		List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
		if (instances.size() == 0) {
			throw new ServiceDiscoveryException(String.format("Unable to fetch %s instances", serviceId));
		}
		return Flux.just(instances);
	}
}
