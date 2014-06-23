/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.ontology;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class MetaInfoBlobber {

    private final MetaInfo meta;

    public MetaInfoBlobber(final MetaInfo meta) {
        this.meta = meta;
    }

    public MetaInfo getMeta() {
        return meta;
    }

    /**
     * Converts a MetaInfo object to a BLOB.
     * @return
     *  The serializable object, instance of MetaInfo, converted into a SQL BLOB
     *  object.
     * @throws SerialException
     *  If an error occurs during serialization
     *  (thrown by the constructor of SerialBlob).
     * @throws SQLException
     *  If the Blob passed to this to this constructor is a <code>null</code>
     *  (thrown by the constructor of SerialBlob).
     * @throws IOException 
     *  If the serializable object cannot be written into bytes because an
     *  ObjectOutputStream cannot write to a ByteArrayOutputStream, or, because
     *  the ObjectOutputStream object cannot flush or close properly.
     */
    public Blob toBlob() throws SerialException, SQLException, IOException {
        if (meta == null) {
            return null;
        }
        return new SerialBlob(getBytes());

    }

    private byte[] getBytes() throws IOException {
        Object obj = meta;
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        bos.close();
        return bos.toByteArray();
    }
}