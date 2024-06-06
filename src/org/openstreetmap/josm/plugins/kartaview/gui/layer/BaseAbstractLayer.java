/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.layer.Layer;


/**
 * Base abstract class used for the KartaView plugin layers.
 *
 * @author nicoleta.viregan
 */
abstract class BaseAbstractLayer extends Layer {

    private final JosmAction deleteLayerAction;
    private final JosmAction displayFilterAction;
    private final JosmAction openFeedbackAction;
    private final JosmAction openPreferencesAction;
    protected final PaintHandler paintHandler = new PaintHandler();

    BaseAbstractLayer(final String name, final JosmAction deleteLayerAction, final JosmAction displayFilterAction) {
        super(name);
        this.deleteLayerAction = deleteLayerAction;
        this.displayFilterAction = displayFilterAction;
        openFeedbackAction = new OpenFeedbackPageAction();
        openPreferencesAction = new OpenPreferenceDialogAction();
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

    public void enableFilterAction(final boolean enabled) {
        displayFilterAction.setEnabled(enabled);
    }

    JosmAction getDeleteLayerAction() {
        return deleteLayerAction;
    }

    JosmAction getDisplayFilterAction() {
        return displayFilterAction;
    }

    JosmAction getOpenFeedbackAction() {
        return openFeedbackAction;
    }

    JosmAction getOpenPreferencesAction() {
        return openPreferencesAction;
    }
}