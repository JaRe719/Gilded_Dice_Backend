package de.jare.gildeddice;

import de.jare.gildeddice.configurations.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class GildedDiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GildedDiceApplication.class, args);
    }

}
