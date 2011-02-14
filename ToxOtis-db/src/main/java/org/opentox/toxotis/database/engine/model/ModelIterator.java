package org.opentox.toxotis.database.engine.model;

import java.sql.ResultSet;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.database.DbIterator;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ModelIterator extends DbIterator<Model>{

    public ModelIterator(ResultSet rs) {
        super(rs);
    }    

    @Override
    public Model next() throws DbException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}