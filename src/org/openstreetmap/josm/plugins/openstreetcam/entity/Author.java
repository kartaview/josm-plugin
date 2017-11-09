package org.openstreetmap.josm.plugins.openstreetcam.entity;


/**
 * Defines the author of a detection or contribution.
 *
 * @author ioanao
 * @version $Revision$
 */
public class Author {

    private final Long externalId;
    private final String userName;


    public Author(final Long externalId, final String userName) {
        this.externalId = externalId;
        this.userName = userName;
    }


    public Long getExternalId() {
        return externalId;
    }

    public String getUserName() {
        return userName;
    }
}