package org.openstreetmap.josm.plugins.openstreetcam.observer;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;

/**
 * The observable interface for the {@code RowSelectionObserver}.
 *
 * @author nicoletav
 */
public interface  RowSelectionObservable {
    
    /**
     * Registers the given observer.
     *
     * @param observer a {@code RowSelectionObserver}
     */
    void registerObserver(final RowSelectionObserver observer);
    
    /**
     * Notifies the observers listening.
     * 
     * @param detection corresponding to the selected row
     */
    void notifyRowSelectionObserver(final Detection detection);
}