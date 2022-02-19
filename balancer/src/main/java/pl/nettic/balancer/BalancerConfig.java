package pl.nettic.balancer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@LoadBalancerClient(name = "ocr-parser", configuration = DiscoveryConfiguration.class)
class BalancerConfig {
	
	@Value("${allowedOrigin}")
	private String allowedOrigin;
	
	@LoadBalanced
	@Bean
	WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}
	
	@Bean
	public WebMvcConfigurer getCorsConfig() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins(allowedOrigin)
						.allowedMethods("GET", "PUT");
			}
		};
	}
}
