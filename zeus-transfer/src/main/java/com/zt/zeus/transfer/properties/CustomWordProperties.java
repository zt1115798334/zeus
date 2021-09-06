package com.zt.zeus.transfer.properties;

import com.zt.zeus.transfer.enums.Carrier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.custom-word")
public class CustomWordProperties {
    private List<String> word;
    private List<String> author;
    private List<Carrier> carrier;

}
