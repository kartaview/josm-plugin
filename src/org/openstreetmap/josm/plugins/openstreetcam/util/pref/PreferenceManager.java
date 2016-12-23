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
package org.openstreetmap.josm.plugins.openstreetcam.util.pref;

import java.util.Date;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;


/**
 * Utility class, manages save and load (put & get) operations of the preference variables. The preference variables are
 * saved into a global preference file. Preference variables are static variables which can be accessed from any plugin
 * class. Values saved in this global file, can be accessed also after a JOSM restart.
 *
 * @author Beata
 * @version $Revision$
 */
public final class PreferenceManager {

    private static final PreferenceManager INSTANCE = new PreferenceManager();

    private PreferenceManager() {}

    public static PreferenceManager getInstance() {
        return INSTANCE;
    }

    /**
     * Loads the photo search error suppress flag. If this value is true, then all the future photo search service
     * errors will be suppressed.
     *
     * @return a boolean value
     */
    public boolean loadPhotosErrorSuppressFlag() {
        return Main.pref.getBoolean(Keys.SUPPRESS_PHOTOS_ERROR);
    }

    /**
     * Saves the photo search error suppress flag.
     *
     * @param flag a boolean value
     */
    public void savePhotosErrorSuppressFlag(final boolean flag) {
        Main.pref.put(Keys.SUPPRESS_PHOTOS_ERROR, flag);
    }

    /**
     * Loads the sequence error suppress flag. If this value is true, then all the future sequence retrieve errors will
     * be suppressed.
     *
     * @return a boolean value
     */
    public boolean loadSequenceErrorSuppressFlag() {
        return Main.pref.getBoolean(Keys.SUPPRESS_SEQUENCE_ERROR);
    }

    /**
     * Saves the sequence error suppress flag.
     *
     * @param flag a boolean value
     */
    public void saveSequenceErrorSuppressFlag(final boolean flag) {
        Main.pref.put(Keys.SUPPRESS_SEQUENCE_ERROR, flag);
    }

    /**
     * Saves the 'filtersChanged' flag to the preference file.
     *
     * @param changed a boolean value
     */
    public void saveFiltersChangedFlag(final boolean changed) {
        Main.pref.put(Keys.FILTERS_CHANGED, "");
        Main.pref.put(Keys.FILTERS_CHANGED, "" + changed);
    }

    /**
     * Returns the 'filtersChanged' flag key.
     *
     * @return a string
     */
    public String getFiltersChangedFlagKey() {
        return Keys.FILTERS_CHANGED;
    }

    /**
     * Loads the list filters from the preference file.
     *
     * @return a {@code ListFilter}
     */
    public ListFilter loadListFilter() {
        final String dateStr = Main.pref.get(Keys.DATE);
        Date date = null;
        if (!dateStr.isEmpty()) {
            date = new Date(Long.parseLong(dateStr));
        }
        final String onlyUserFlagStr = Main.pref.get(Keys.ONLY_USER_FLAG);
        final boolean onlyUserFlag = onlyUserFlagStr.isEmpty() ? ListFilter.DEFAULT.isOnlyUserFlag()
                : new Boolean(onlyUserFlagStr).booleanValue();
        return new ListFilter(date, onlyUserFlag);
    }

    /**
     * Saves the list filter to the preference file.
     *
     * @param filter a {@code ListFilter} represents the curent filter settings
     */
    public void saveListFilter(final ListFilter filter) {
        if (filter != null) {
            final String dateStr = filter.getDate() != null ? "" + filter.getDate().getTime() : "";
            Main.pref.put(Keys.DATE, dateStr);
            Main.pref.put(Keys.ONLY_USER_FLAG, filter.isOnlyUserFlag());
        }
    }

    /**
     * Loads the dialog appearance status from the preference file.
     *
     * @return a {@code Boolean}
     */
    public Boolean loadDialogOpened() {
        final Boolean dialogIsOpened = Main.pref.getBoolean(Keys.DIALOG_OPENED);
        return dialogIsOpened;
    }

    /**
     * Saves the dialog appearance status to the preference file.
     *
     * @param dialogIsOpened a {@code Boolean} represents the dialog showing/hiding status
     */
    public void saveDialogOpened(final Boolean dialogIsOpened) {
        Main.pref.put(Keys.DIALOG_OPENED, dialogIsOpened);
    }

    /**
     * Loads the layer appearance status from the preference file.
     *
     * @return a {@code Boolean}
     */
    public Boolean loadLayerOpened() {
        final String layerOpened = Main.pref.get(Keys.LAYER_OPENED);
        if (layerOpened == null) {
            return true;
        }
        return Boolean.valueOf(layerOpened);
    }

    /**
     * Saves the layer appearance status to the preference file.
     *
     * @param layerIsOpened a {@code Boolean} represents the layer showing/hiding status
     */
    public void saveLayerOpened(final Boolean layerIsOpened) {
        Main.pref.put(Keys.LAYER_OPENED, layerIsOpened);
    }
}