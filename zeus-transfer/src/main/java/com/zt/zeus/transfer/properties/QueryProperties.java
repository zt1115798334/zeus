package com.zt.zeus.transfer.properties;

import com.zt.zeus.transfer.enums.Carrier;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.query")
public class QueryProperties {

    private QueryInfo relatedQuery;
    private QueryInfo authorQuery;

    @ToString
    @Getter
    @Setter
    public static class QueryInfo {
        private List<String> related;
        private List<String> author;
        private List<Carrier> carrier;
    }
}
