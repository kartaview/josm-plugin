package org.openstreetmap.josm.plugins.openstreetcam.entity;


/**
 * Defines the author of a detection or contribution.
 *
 * @author ioanao
 * @version $Revision$
 */
public class Author {

    private final Long externalId;
    private final AuthorType type;


    public Author(final Long externalId, final AuthorType type) {
        this.externalId = externalId;
        this.type = type;
    }


    public Long getExternalId() {
        return externalId;
    }

    public AuthorType getType() {
        return type;
    }
}