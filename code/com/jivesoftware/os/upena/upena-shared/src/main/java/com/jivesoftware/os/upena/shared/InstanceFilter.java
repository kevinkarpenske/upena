package com.jivesoftware.os.upena.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jivesoftware.os.amza.shared.KeyValueFilter;
import com.jivesoftware.os.amza.shared.TimestampedValue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class InstanceFilter implements KeyValueFilter<InstanceKey, Instance> {

    public final ClusterKey clusterKey;
    public final HostKey hostKey;
    public final ServiceKey serviceKey;
    public final ReleaseGroupKey releaseGroupKey;
    public final Integer logicalInstanceId;
    public final int start;
    public final int count;
    public int hit;

    @JsonCreator
    public InstanceFilter(@JsonProperty("clusterKey") ClusterKey clusterKey,
            @JsonProperty("hostKey") HostKey hostKey,
            @JsonProperty("serviceKey") ServiceKey serviceKey,
            @JsonProperty("releaseGroupKey") ReleaseGroupKey releaseGroupKey,
            @JsonProperty("logicalInstanceId") Integer logicalInstanceId,
            @JsonProperty("start") int start,
            @JsonProperty("count") int count) {
        this.clusterKey = clusterKey;
        this.hostKey = hostKey;
        this.serviceKey = serviceKey;
        this.releaseGroupKey = releaseGroupKey;
        this.logicalInstanceId = logicalInstanceId;
        this.start = start;
        this.count = count;
    }

    @Override
    public String toString() {
        return "InstanceFilter{"
                + "clusterKey=" + clusterKey
                + ", hostKey=" + hostKey
                + ", serviceKey=" + serviceKey
                + ", releaseGroupKey=" + releaseGroupKey
                + ", logicalInstanceId=" + logicalInstanceId
                + '}';
    }

    @Override
    public ConcurrentNavigableMap<InstanceKey, TimestampedValue<Instance>> createCollector() {
        return new Results();
    }

    public static class Results extends ConcurrentSkipListMap<InstanceKey, TimestampedValue<Instance>> {
    }

    @Override
    public boolean filter(InstanceKey key, Instance value) {
        if (clusterKey != null && value.clusterKey != null) {
            if (!value.clusterKey.equals(clusterKey)) {
                return false;
            }
        }
        if (hostKey != null && value.hostKey != null) {
            if (!value.hostKey.equals(hostKey)) {
                return false;
            }
        }
        if (serviceKey != null && value.serviceKey != null) {
            if (!value.serviceKey.equals(serviceKey)) {
                return false;
            }
        }
        if (releaseGroupKey != null && value.releaseGroupKey != null) {
            if (!value.releaseGroupKey.equals(releaseGroupKey)) {
                return false;
            }
        }

        if (logicalInstanceId != null) {
            if (logicalInstanceId != value.instanceId) {
                return false;
            }
        }
        hit++;
        if (hit < start) {
            return false;
        }
        if (hit > start + count) {
            return false;
        }
        return true;
    }

    @Override
    public void reset() {
        hit = 0;
    }
}
