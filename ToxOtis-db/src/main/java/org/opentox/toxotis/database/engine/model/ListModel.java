package org.opentox.toxotis.database.engine.model;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.DbReader;
import org.opentox.toxotis.database.ResultSetIterator;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ListModel extends DbReader<String>{

    /**
     * Lists BibTeX IDs
     * @return
     */
    @Override
    public IDbIterator<String> list() throws DbException{
        setTable("Model");
        setTableColumns("id");
        try {
            PreparedStatement ps = getConnection().prepareStatement(getSql());
            ResultSet results = ps.executeQuery();
            return new ResultSetIterator(results);
        }catch (final SQLException ex){
            throw new DbException(ex);
        }
    }

}