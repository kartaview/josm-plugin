/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.Map;


/**
 * Holds constants used by the layer clases.
 *
 * @author Beata
 * @version $Revision$
 */
final class Constants {

    /* constants used for drawing a sequence */
    static final Composite OPAQUE_COMPOSITE = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
    static final Composite TRANSPARENT_COMPOSITE = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.10f);
    static final BasicStroke SEQUENCE_LINE = new BasicStroke(2.5F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static final BasicStroke CLUSTER_DATA_LINE = new BasicStroke(2F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    static final Color SEQUENCE_LINE_COLOR = new Color(0, 143, 222);
    static final Color CLUSTER_DATA_LINE_COLOR = new Color(255, 69, 0);
    static final int MIN_ARROW_ZOOM = 16;
    static final double ARROW_LENGTH = 3.5D;
    static final double CLUSTER_EXTRAPOLATE_DISTANCE = 0.3;

    static final String BING_LAYER_NAME = "Bing aerial imagery";
    static final String MAPBOX_LAYER_NAME = "Mapbox Satellite";


    static final Color SEGMENT_COLOR = new Color(179, 0, 223);
    static final Stroke SEGMENT_STROKE = new BasicStroke(4F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static final float[] SEGMENT_TRANSPARENCY = { 0.35f, 0.45f, 0.55f, 0.65f, 0.75f, 0.85f, 0.9f, 0.95f, 1f };
    static final Float OPAQUE_ALPHA = 1.0f;
    static final Map<RenderingHints.Key, Object> RENDERING_MAP = createRenderingMap();


    private Constants() {}

    private static Map<RenderingHints.Key, Object> createRenderingMap() {
        final Map<RenderingHints.Key, Object> map = new HashMap<>();
        map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        map.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return map;
    }
}