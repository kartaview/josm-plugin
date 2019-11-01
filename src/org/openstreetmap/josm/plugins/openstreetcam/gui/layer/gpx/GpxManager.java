/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer.gpx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.openstreetmap.josm.data.gpx.GpxData;
import org.openstreetmap.josm.io.GpxWriter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;


/**
 * Handles GPX file related operations.
 *
 * @author beataj
 * @version $Revision$
 */
public final class GpxManager {


    /**
     * Saves the sequence to the given file.
     *
     * @param sequence a {@code Sequence} represents the currently selected sequence
     * @param fileName specifies the name of the GPX file with absolute path
     * @throws GpxManagerException if the operation fails
     */
    public void saveSequence(final Sequence sequence, final String fileName) throws GpxManagerException {
        try (final GpxWriter gpxWriter = new GpxWriter(new FileOutputStream(new File(fileName)))) {
            final GpxData gpxData = GpxBuilder.buildSequenceGpx(sequence);
            gpxWriter.write(gpxData);
        } catch (final IOException e) {
            throw new GpxManagerException(e);
        }
    }
}