package org.opentox.toxotis.core.component.qprf;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import javax.sql.rowset.serial.SerialBlob;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class QprfReportMetaSerializer {

    private final QprfReportMeta meta;

    public QprfReportMetaSerializer(QprfReportMeta meta) {
        this.meta = meta;
    }

    public Blob toBlob() throws Exception {
        if (meta == null) {
            return null;
        }
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
