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
    private final SignCategory category;
    private final String type;


    public Sign(final String name, final String internalName, final String iconName, final String region,
            final SignCategory category, final String type) {
        this.name = name;
        this.internalName = internalName;
        this.iconName = iconName;
        this.region = region;
        this.category = category;
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public SignCategory getCategory() {
        return category;
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