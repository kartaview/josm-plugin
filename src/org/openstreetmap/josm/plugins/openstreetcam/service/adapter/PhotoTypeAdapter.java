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
package org.openstreetmap.josm.plugins.openstreetcam.service.adapter;

import java.io.IOException;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


/**
 * Custom type adapter for the {@code Photo} object.
 *
 * @author Beata
 * @version $Revision$
 */
public class PhotoTypeAdapter extends TypeAdapter<Photo> {

    private final PhotoReader photoReader = new PhotoReader();
    private final PhotoWriter photoWriter = new PhotoWriter();


    @Override
    public Photo read(final JsonReader reader) throws IOException {
        return photoReader.read(reader);
    }


    @Override
    public void write(final JsonWriter writer, final Photo photo) throws IOException {
        photoWriter.write(writer, photo);
    }
}