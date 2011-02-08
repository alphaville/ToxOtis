package org.opentox.toxotis.ontology;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
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
    

    public Blob toBlob() throws Exception{
        Blob blob = new SerialBlob(getBytes());
        return blob;

    }

    private byte[] getBytes() throws java.io.IOException {
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
        byte[] data = bos.toByteArray();
        return data;
    }



}