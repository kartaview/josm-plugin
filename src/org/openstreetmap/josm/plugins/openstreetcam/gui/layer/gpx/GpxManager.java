/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
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