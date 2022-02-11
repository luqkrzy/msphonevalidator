package io.aeon.balancer;

import io.aeon.exception.ServiceDiscoveryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

class OcrParserInstanceListSuppler implements ServiceInstanceListSupplier {

	@Value("${client.name}")
	private String serviceId;
	
	@Autowired
	WebClient webClient;
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	ResponseEntity<Void> checkEurekaAvailability() {
		return webClient.get()
						.retrieve()
						.toBodilessEntity()
						.onErrorReturn(new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE))
						.blockOptional().orElseThrow(() -> new ServiceDiscoveryException("Unable to connect to service discovery") );
	}
	
	@Override
	public String getServiceId() {
		return serviceId;
	}
	
	@Override
	public Flux<List<ServiceInstance>> get() {
		checkEurekaAvailability();
		List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
		if (instances.size() == 0) {
			throw new ServiceDiscoveryException(String.format("Unable to fetch %s instances", serviceId));
		}
		return Flux.just(instances);
	}
}
