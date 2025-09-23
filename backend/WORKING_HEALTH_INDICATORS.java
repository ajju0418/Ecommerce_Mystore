/*
 * WORKING HEALTH INDICATOR EXAMPLES
 * 
 * This file contains examples of health indicators for different services.
 * Each class should be placed in its respective service module.
 */

// 1. API Gateway Health Indicator
// File: api-gateway/src/main/java/com/estore/gateway/GatewayHealthIndicator.java
/*
package com.estore.gateway;

import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class GatewayHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        return Health.up()
                .withDetail("service", "API Gateway")
                .withDetail("port", "9091")
                .build();
    }
}
*/

// 2. Cart Service Health Indicator  
// File: cart-service/src/main/java/com/estore/cart/CartServiceHealthIndicator.java
/*
package com.estore.cart;

import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CartServiceHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        return Health.up()
                .withDetail("service", "Cart Service")
                .withDetail("port", "9095")
                .build();
    }
}
*/

// 3. Eureka Server Health Indicator
// File: eureka-server/src/main/java/com/estore/eureka/EurekaHealthIndicator.java
/*
package com.estore.eureka;

import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class EurekaHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        return Health.up()
                .withDetail("service", "Eureka Server")
                .withDetail("port", "8761")
                .build();
    }
}
*/

// MAVEN DEPENDENCY (Add to each service's pom.xml)
/*
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
*/
