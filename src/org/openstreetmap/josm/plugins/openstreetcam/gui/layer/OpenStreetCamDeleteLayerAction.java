package org.openstreetmap.josm.plugins.openstreetcam.gui.layer;

import static org.openstreetmap.josm.tools.I18n.tr;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.layer.DeleteLayerAction;
import org.openstreetmap.josm.gui.help.HelpUtil;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.ImageProvider;


public final class OpenStreetCamDeleteLayerAction extends AbstractAction {

    private static final long serialVersionUID = 1569467764140753112L;
    private final DeleteLayerAction deleteAction = LayerListDialog.getInstance().createDeleteLayerAction();

    public OpenStreetCamDeleteLayerAction() {
        new ImageProvider("dialogs", "delete").getResource().attachImageIcon(this, true);
        putValue(SHORT_DESCRIPTION, tr("Delete the selected layers."));
        putValue(NAME, tr("Delete"));
        putValue("help", HelpUtil.ht("/Dialog/LayerList#DeleteLayer"));
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        PreferenceManager.getInstance().saveLayerOpened(false);
        deleteAction.actionPerformed(e);
    }

}