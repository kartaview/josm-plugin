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
     * Loads the error suppress flag. If this value is true, then all the future service errors will be suppressed.
     *
     * @return a boolean value
     */
    public boolean loadErrorSuppressFlag() {
        return Main.pref.getBoolean(Keys.SUPPRESS_ERROR);
    }

    /**
     * Saves the error suppress flag to the preference file.
     *
     * @param flag a boolean value
     */
    public void saveErrorSuppressFlag(final boolean flag) {
        Main.pref.put(Keys.SUPPRESS_ERROR, flag);
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
}