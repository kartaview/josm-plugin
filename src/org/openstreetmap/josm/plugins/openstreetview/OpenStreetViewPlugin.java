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
package org.openstreetmap.josm.plugins.openstreetview;

import java.awt.GraphicsEnvironment;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.plugins.openstreetview.gui.details.OpenStreetViewDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetview.gui.layer.OpenStreetViewLayer;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class OpenStreetViewPlugin extends Plugin {

    private OpenStreetViewDetailsDialog detailsDialog;
    private OpenStreetViewLayer layer;

    public OpenStreetViewPlugin(final PluginInformation pluginInfo) {
        super(pluginInfo);
    }

    @Override
    public void mapFrameInitialized(final MapFrame oldMapFrame, final MapFrame newMapFrame) {
        if (Main.map != null && !GraphicsEnvironment.isHeadless()) {
            detailsDialog = new OpenStreetViewDetailsDialog();
            newMapFrame.addToggleDialog(detailsDialog);
            layer = new OpenStreetViewLayer();
            newMapFrame.mapView.getLayerManager().addLayer(layer);
        }
    }
}