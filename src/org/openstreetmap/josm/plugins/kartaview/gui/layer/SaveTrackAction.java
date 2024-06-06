/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.openstreetmap.josm.plugins.kartaview.gui.layer.gpx.GpxManager;
import org.openstreetmap.josm.plugins.kartaview.gui.layer.gpx.GpxManagerException;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.kartaview.DataSet;


/**
 * Saves the selected track to a GPX file.
 *
 * @author beataj
 * @version $Revision$
 */
class SaveTrackAction extends JosmAction {

    private static final long serialVersionUID = -7721418682849752904L;
    private static final String EXT = ".gpx";
    private final transient GpxManager gpxManager;
    private static final String TRACK = "Track";


    SaveTrackAction() {
        super(GuiConfig.getInstance().getLayerSaveSequenceMenuItemLbl(), IconConfig.getInstance().getSaveIconName(),
                GuiConfig.getInstance().getLayerSaveSequenceMenuItemLbl(), null, true);
        gpxManager = new GpxManager();
    }


    @Override
    public void actionPerformed(final ActionEvent event) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setDialogTitle(event.getActionCommand());
        fileChooser.setSelectedFile(new File(getFileName()));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new GPXFilter());
        if (fileChooser.showSaveDialog(MainApplication.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
            final String absPath = fileChooser.getSelectedFile().getAbsolutePath();
            if (new File(absPath).exists()) {
                final String msgTxt =
                        fileChooser.getSelectedFile().getName() + " " + GuiConfig.getInstance().getInfoFileExistsText();
                final int overwriteResp = JOptionPane.showConfirmDialog(MainApplication.getMainFrame(), msgTxt,
                        GuiConfig.getInstance().getInfoFileExistsTitle(), JOptionPane.YES_NO_OPTION);
                if (overwriteResp == JOptionPane.YES_OPTION) {
                    saveSequence(absPath);
                }
            } else {
                saveSequence(absPath);
            }
        }
    }


    private void saveSequence(final String fileName) {
        try {
            gpxManager.saveSequence(DataSet.getInstance().getSelectedSequence(), fileName);
        } catch (final GpxManagerException | IOException e) {
            JOptionPane.showMessageDialog(MainApplication.getMainFrame(),
                    GuiConfig.getInstance().getErrorSequenceSaveText(), GuiConfig.getInstance().getErrorTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getFileName() {
        return TRACK + "_" + DataSet.getInstance().getSelectedPhoto().getSequenceId() + EXT;
    }


    private class GPXFilter extends FileFilter {

        @Override
        public boolean accept(final File f) {
            return f.getName().toLowerCase().endsWith(EXT);
        }

        @Override
        public String getDescription() {
            return "GPX Files";
        }
    }
}