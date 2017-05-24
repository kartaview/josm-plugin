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
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer;

import static org.openstreetmap.josm.tools.I18n.tr;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URI;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.LayerListPopup;
import org.openstreetmap.josm.gui.dialogs.layer.DeleteLayerAction;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.FilterDialog;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.OpenBrowser;


/**
 * Defines JOSM layer related functionality.
 *
 * @author Beata
 * @version $Revision$
 */
abstract class AbtractLayer extends Layer {

    private static final String DELETE_ACTION = "delete";


    AbtractLayer() {
        super(GuiConfig.getInstance().getPluginShortName());
        final LayerListDialog layerListDialog = LayerListDialog.getInstance();
        layerListDialog.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), DELETE_ACTION);
        layerListDialog.getActionMap().put(DELETE_ACTION, new OpenStreetCamDeleteLayerAction());
    }


    @Override
    public Icon getIcon() {
        return IconConfig.getInstance().getLayerIcon();
    }

    @Override
    public Object getInfoComponent() {
        return GuiConfig.getInstance().getPluginTlt();
    }

    @Override
    public Action[] getMenuEntries() {
        final LayerListDialog layerListDialog = LayerListDialog.getInstance();
        final String fiterIconName = PreferenceManager.getInstance().loadListFilter().isDefaultFilter()
                ? IconConfig.getInstance().getFilterIconName() : IconConfig.getInstance().getFilterSelectedIconName();
        return new Action[] { layerListDialog.createActivateLayerAction(this),
                layerListDialog.createShowHideLayerAction(), new OpenStreetCamDeleteLayerAction(),
                SeparatorLayerAction.INSTANCE, new DisplayFilterDialogAction(fiterIconName),
                SeparatorLayerAction.INSTANCE, new OpenFeedbackPageAction(), SeparatorLayerAction.INSTANCE,
                new LayerListPopup.InfoAction(this) };
    }

    @Override
    public String getToolTipText() {
        return GuiConfig.getInstance().getPluginLongName();
    }

    @Override
    public boolean isMergable(final Layer layer) {
        return false;
    }

    @Override
    public void mergeFrom(final Layer layer) {
        // this operation is not supported
    }

    @Override
    public void visitBoundingBox(final BoundingXYVisitor visitor) {
        // no logic to add here
    }

    /**
     * Deletes the OpenStreetCam layer.
     *
     * @author ioanao
     * @version $Revision$
     */
    private final class OpenStreetCamDeleteLayerAction extends AbstractAction {

        private static final long serialVersionUID = 1569467764140753112L;
        private final DeleteLayerAction deleteAction = LayerListDialog.getInstance().createDeleteLayerAction();

        private OpenStreetCamDeleteLayerAction() {
            super(GuiConfig.getInstance().getLayerDeleteMenuItemLbl());
            new ImageProvider(IconConfig.getInstance().getDeleteIconName()).getResource().attachImageIcon(this, true);
            putValue(SHORT_DESCRIPTION, tr(GuiConfig.getInstance().getLayerDeleteMenuItemTlt()));
            putValue(NAME, tr(GuiConfig.getInstance().getLayerDeleteMenuItemLbl()));
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            PreferenceManager.getInstance().saveLayerOpenedFlag(false);
            deleteAction.actionPerformed(e);
        }
    }


    /**
     * Displays the filter dialog window.
     *
     * @author beataj
     * @version $Revision$
     */
    private final class DisplayFilterDialogAction extends JosmAction {

        private static final long serialVersionUID = 8325126526750975651L;


        private DisplayFilterDialogAction(final String iconName) {
            super(GuiConfig.getInstance().getDlgFilterTitle(), iconName, GuiConfig.getInstance().getDlgFilterTitle(),
                    ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getDlgFilterShortcutText()),
                    true);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            final ImageIcon icon = PreferenceManager.getInstance().loadListFilter().isDefaultFilter()
                    ? IconConfig.getInstance().getFilterIcon() : IconConfig.getInstance().getFilterSelectedIcon();
            final FilterDialog filterDialog = new FilterDialog(icon);
            filterDialog.setVisible(true);
        }
    }


    /**
     * Opens the feedback page.
     *
     * @author beataj
     * @version $Revision$
     */
    private final class OpenFeedbackPageAction extends JosmAction {

        private static final long serialVersionUID = 4196639030623647016L;

        private OpenFeedbackPageAction() {
            super(GuiConfig.getInstance().getLayerFeedbackMenuItemLbl(), IconConfig.getInstance().getFeedbackIconName(),
                    GuiConfig.getInstance().getLayerFeedbackMenuItemLbl(),
                    ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getLayerFeedbackShortcutText()),
                    true);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            try {
                OpenBrowser.displayUrl(new URI(Config.getInstance().getFeedbackUrl()));
            } catch (final Exception e) {
                JOptionPane.showMessageDialog(Main.parent, GuiConfig.getInstance().getErrorFeedbackPageTxt(),
                        GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}