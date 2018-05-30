/**
 * @author b2mark
 * @version 1.0
 * @since 2018
 */
package com.b2mark.kyc.ops;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


@Component
public class LearningSpringBootHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        try {
            URL url = new URL("http://example.com/admin");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int statusCode = conn.getResponseCode();
            if (statusCode >= 200 && statusCode < 300) {
                return Health.up().build();
            } else {
                return Health.down()
                        .withDetail("HTTP Status Code", statusCode)
                        .build();
            }
        } catch (IOException e) {
            return Health.down(e).build();
        }
    }
}
