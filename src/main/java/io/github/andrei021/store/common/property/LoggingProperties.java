package io.github.andrei021.store.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.logging")
public record LoggingProperties(Integer previewLimit) {

    private static final int defaultPreviewLimit = 5;

    public LoggingProperties(Integer previewLimit) {
        this.previewLimit = (previewLimit != null) ? previewLimit : defaultPreviewLimit;
    }
}
