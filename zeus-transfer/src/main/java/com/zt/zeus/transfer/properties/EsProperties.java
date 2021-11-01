package com.zt.zeus.transfer.properties;

import com.zt.zeus.transfer.enums.ReadModel;
import com.zt.zeus.transfer.enums.SearchType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/22 10:53
 * description: es配置
 */
@ToString
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.es")
public class EsProperties {

    private Integer pageSize;

    private String filePath;

    private String version;

    private String analysis;

    private SearchType searchType;

    private ReadModel readModel;

    private EsInfo es5;

    @ToString
    @Getter
    @Setter
    public static class EsInfo {
        private String key;
        private String host;
        private String appId;
        private String fullQuery;
        private String articleQuery;
    }

}

