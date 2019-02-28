/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2018, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;


/**
 * Defines a builder for the cluster entity.
 *
 * @author beataj
 * @version $Revision$
 */
public class ClusterBuilder {

    private Long id;
    private Long latestChangeTimestamp;
    private LatLon point;
    private Double facing;
    private Sign sign;
    private Double confidenceLevel;
    private List<Long> detectionIds;
    private OsmComparison osmComparison;
    private OsmElement osmElement;
    private List<Detection> detections;
    private List<Photo> photos;
    private String componentValue;
    private Short laneCount;

    public ClusterBuilder() {}

    public ClusterBuilder(final Cluster cluster) {
        id(cluster.getId());
        latestChangeTimestamp(cluster.getLatestChangeTimestamp());
        point(cluster.getPoint());
        facing(cluster.getFacing());
        sign(cluster.getSign());
        confidenceLevel(cluster.getConfidenceLevel());
        detectionIds(cluster.getDetectionIds());
        osmComparison(cluster.getOsmComparison());
        osmElement(cluster.getOsmElement());
        componentValue(cluster.getComponentValue());
        laneCount(cluster.getLaneCount());
    }

    public void id(final Long id) {
        this.id = id;
    }

    public void latestChangeTimestamp(final Long latestChangeTimestamp) {
        this.latestChangeTimestamp = latestChangeTimestamp;
    }

    public void point(final LatLon point) {
        this.point = point;
    }

    public void facing(final Double facing) {
        this.facing = facing;
    }

    public void sign(final Sign sign) {
        this.sign = sign;
    }

    public void confidenceLevel(final Double confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public void detectionIds(final List<Long> detectionIds) {
        this.detectionIds = detectionIds;
    }

    public void osmComparison(final OsmComparison osmComparison) {
        this.osmComparison = osmComparison;
    }

    public void osmElement(final OsmElement osmElement) {
        this.osmElement = osmElement;
    }

    public void detections(final List<Detection> detections) {
        this.detections = detections;
    }

    public void photos(final List<Photo> photos) {
        this.photos = photos;
    }

    public void componentValue(final String componentValue) {
        this.componentValue = componentValue;
    }

    public void laneCount(final Short laneCount){
        this.laneCount = laneCount;
    }

    Long getId() {
        return id;
    }

    Long getLatestChangeTimestamp() {
        return latestChangeTimestamp;
    }

    LatLon getPoint() {
        return point;
    }

    Double getFacing() {
        return facing;
    }

    Sign getSign() {
        return sign;
    }

    Double getConfidenceLevel() {
        return confidenceLevel;
    }

    List<Long> getDetectionIds() {
        return detectionIds;
    }

    OsmComparison getOsmComparison() {
        return osmComparison;
    }

    OsmElement getOsmElement() {
        return osmElement;
    }

    List<Detection> getDetections() {
        return detections;
    }

    List<Photo> getPhotos() {
        return photos;
    }

    String getComponentValue() {
        return componentValue;
    }

    public Short getLaneCount() { return laneCount; }

    public Cluster build() {
        return new Cluster(this);
    }
}