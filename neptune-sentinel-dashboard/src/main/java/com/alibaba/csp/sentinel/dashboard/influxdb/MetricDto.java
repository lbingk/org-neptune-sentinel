package com.alibaba.csp.sentinel.dashboard.influxdb;

import lombok.Getter;
import lombok.Setter;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

/**
 * @author cdfive
 * @date 2018-10-19
 */
@Setter
@Getter
@Measurement(name = "sentinel_metric")
public class MetricDto{

    @Column(name = "time")
    private Instant time;

    @Column(name = "id")
    private Long id;

    @Column(name = "gmtCreate")
    private Long gmtCreate;

    @Column(name = "gmtModified")
    private Long gmtModified;

    @Column(name = "app", tag = true)
    private String app;

    @Column(name = "resource", tag = true)
    private String resource;

    @Column(name = "passQps")
    private Long passQps;

    @Column(name = "successQps")
    private Long successQps;

    @Column(name = "blockQps")
    private Long blockQps;

    @Column(name = "exceptionQps")
    private Long exceptionQps;

    @Column(name = "rt")
    private double rt;

    @Column(name = "count")
    private int count;

    @Column(name = "resourceCode")
    private int resourceCode;

}