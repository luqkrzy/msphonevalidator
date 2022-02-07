package io.aeon.balancer;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;

class OcrParserInstanceListSuppler implements ServiceInstanceListSupplier {
	
	private final String serviceId;
	
	public OcrParserInstanceListSuppler(String serviceId) {
		this.serviceId = serviceId;
	}
	
	@Override
	public String getServiceId() {
		return serviceId;
	}
	
	@Override
	public Flux<List<ServiceInstance>> get() {
		
		return Flux.just(List.of(new DefaultServiceInstance(serviceId + "1", serviceId, "localhost", 8000, false),
								 new DefaultServiceInstance(serviceId + "2", serviceId, "localhost", 8001, false)));
	}
}