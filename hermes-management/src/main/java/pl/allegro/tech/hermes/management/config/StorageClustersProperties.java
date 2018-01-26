package pl.allegro.tech.hermes.management.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "storage")
public class StorageClustersProperties {

    private String pathPrefix = "/hermes";
    private int retryTimes = 3;
    private int retrySleep = 1000;
    private int sharedCountersExpiration = 72;
    private List<StorageProperties> clusters = Collections.emptyList();

    @NestedConfigurationProperty
    private StorageAuthorizationProperties authorization;

    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getRetrySleep() {
        return retrySleep;
    }

    public void setRetrySleep(int retrySleep) {
        this.retrySleep = retrySleep;
    }

    public int getSharedCountersExpiration() {
        return sharedCountersExpiration;
    }

    public void setSharedCountersExpiration(int sharedCountersExpiration) {
        this.sharedCountersExpiration = sharedCountersExpiration;
    }

    public StorageAuthorizationProperties getAuthorization() {
        return authorization;
    }

    public void setAuthorization(StorageAuthorizationProperties authorization) {
        this.authorization = authorization;
    }

    public List<StorageProperties> getClusters() {
        return clusters;
    }

    public void setClusters(List<StorageProperties> clusters) {
        this.clusters = clusters;
    }
}
