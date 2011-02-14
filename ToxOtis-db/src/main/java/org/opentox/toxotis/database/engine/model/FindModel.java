package org.opentox.toxotis.database.engine.model;

import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.database.DbReader;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FindModel extends DbReader<Model> {

    private final VRI baseUri;

    public FindModel(final VRI baseUri) {
        this.baseUri = baseUri;
    }

    @Override
    public IDbIterator<Model> list() throws DbException {
        setTable("Model");
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
