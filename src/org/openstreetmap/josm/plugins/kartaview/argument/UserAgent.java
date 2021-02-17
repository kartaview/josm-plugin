/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.argument;

import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.data.Version;
import org.openstreetmap.josm.data.osm.User;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;


/**
 * Defines the user agent HTTP request argument.
 *
 * @author beataj
 * @version $Revision$
 */
public class UserAgent {

    private final String josmVersion;
    private final String kartaViewVersion;
    private final String osmUserInfo;


    public UserAgent() {
        josmVersion = Version.getInstance().getVersionString();
        kartaViewVersion = PreferenceManager.getInstance().loadPluginLocalVersion();
        final User user = UserIdentityManager.getInstance().asUser();
        osmUserInfo = user.getId() > 0 ? Long.toString(user.getId()) : user.getName();
    }


    public String getJosmVersion() {
        return josmVersion;
    }

    public String getKartaViewVersion() {
        return kartaViewVersion;
    }

    public String getOsmUserInfo() {
        return osmUserInfo;
    }

    @Override
    public String toString() {
        return "JOSM/" + josmVersion + ", KartaView/" + kartaViewVersion + ", User Info/" + osmUserInfo;
    }
}