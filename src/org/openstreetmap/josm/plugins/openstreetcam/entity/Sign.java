package org.openstreetmap.josm.plugins.openstreetcam.entity;


/**
 * Defines the detection entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public class Sign {

    private final String name;
    private final String internalName;
    private final String iconName;
    private final String region;
    private final String type;


    public Sign(final String name, final String internalName, final String iconName, final String region,
            final String type) {
        this.name = name;
        this.internalName = internalName;
        this.iconName = iconName;
        this.region = region;
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getIconName() {
        return iconName;
    }
}