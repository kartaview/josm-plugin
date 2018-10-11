package org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity;

import org.openstreetmap.josm.data.StructUtils;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;


public class SignEntry {
    // preference entities must be declared public, otherwise JOSM preference loading does not work!
    @StructUtils.StructEntry
    private Sign sign;

    public SignEntry() {}


    public SignEntry(final Sign sign) {
        this.sign = sign;
    }

    public Sign getSign() {
        return sign;
    }
}
