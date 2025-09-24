package com.estore.admin.service;

import com.estore.admin.dto.ProductCategoryUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ProductManagementService {
    
    @Autowired
    private DiscoveryClient discoveryClient;
    
    @Autowired
    private RestTemplate restTemplate;

    public Object getAllProducts() {
        String productServiceUrl = getServiceUrl("product-service");
        return restTemplate.getForObject(productServiceUrl + "/api/products", Object.class);
    }

    public Object getProductsByCategory(String category) {
        String productServiceUrl = getServiceUrl("product-service");
        return restTemplate.getForObject(productServiceUrl + "/api/products/category/" + category, Object.class);
    }

    public Object updateProductCategory(ProductCategoryUpdateDto updateDto) {
        String productServiceUrl = getServiceUrl("product-service");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, String> requestBody = Map.of("category", updateDto.getCategory());
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        
        return restTemplate.exchange(
            productServiceUrl + "/api/products/" + updateDto.getProductId() + "/category",
            HttpMethod.PUT,
            request,
            Object.class
        ).getBody();
    }

    public Object deleteProduct(Long productId) {
        String productServiceUrl = getServiceUrl("product-service");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Void> request = new HttpEntity<>(headers);
        
        return restTemplate.exchange(
            productServiceUrl + "/api/products/" + productId,
            HttpMethod.DELETE,
            request,
            Object.class
        ).getBody();
    }

    private String getServiceUrl(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances.isEmpty()) {
            throw new RuntimeException("No instances of " + serviceName + " available");
        }
        ServiceInstance instance = instances.get(0);
        return "http://" + instance.getHost() + ":" + instance.getPort();
    }
}