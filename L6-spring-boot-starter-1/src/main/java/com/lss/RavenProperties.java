package com.lss;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "raven")
public class RavenProperties {

    private List<String> where;
    private boolean on;
}
