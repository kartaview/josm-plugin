/*
 *  Copyright 2016 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.openstreetview.gui.layer;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;


/**
 * Holds constants used by the layer clases.
 *
 * @author Beata
 * @version $Revision$
 */
final class Constants {

    private Constants() {}

    static final Map<RenderingHints.Key, Object> RENDERING_MAP = createRenderingMap();

    private static Map<RenderingHints.Key, Object> createRenderingMap() {
        final Map<RenderingHints.Key, Object> map = new HashMap<RenderingHints.Key, Object>();
        map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        map.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return map;
    }

    /* constants used for drawing a sequence */
    static final Composite OPAQUE_COMPOSITE = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
    static final Composite TRANSPARENT_COMPOSITE = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.20f);
    static final BasicStroke SEQUENCE_LINE = new BasicStroke(2.5F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static final Color SEQUENCE_LINE_COLOR = new Color(0, 143, 222);
    static final int MIN_ARROW_ZOOM = 16;
    static final double ARROW_LENGTH = 3.5D;
    static final int ANGLE_360 = 360;

    static final String BING_LAYER_NAME = "Bing aerial imagery";
    static final String MAPBOX_LAYER_NAME = "Mapbox Satellite";
}