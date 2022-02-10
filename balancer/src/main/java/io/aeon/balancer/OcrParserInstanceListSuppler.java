package io.aeon.balancer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;

class OcrParserInstanceListSuppler implements ServiceInstanceListSupplier {
	
	private static final String serviceId = "ocr-parser";
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	
	@Override
	public String getServiceId() {
		return serviceId;
	}
	
	@Override
	public Flux<List<ServiceInstance>> get() {
		return Flux.just(discoveryClient.getInstances(serviceId));
	}
}
