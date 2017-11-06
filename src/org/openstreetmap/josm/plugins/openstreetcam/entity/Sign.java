package org.openstreetmap.josm.plugins.openstreetcam.entity;


/**
 * Defines the detection entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public class Sign {

    private final String region;
    private final SignCategory category;
    private final String type;
    private final String name;
    private String internalName;

    public Sign(final String region, final SignCategory category, final String type, final String name) {
        this.region = region;
        this.category = category;
        this.type = type;
        this.name = name;
    }

    public Sign(final String region, final SignCategory category, final String type, final String name,
            final String internalName) {
        this(region, category, type, name);
        this.internalName = internalName;
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
}