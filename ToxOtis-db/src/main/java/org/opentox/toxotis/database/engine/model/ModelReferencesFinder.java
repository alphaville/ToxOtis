package org.opentox.toxotis.database.engine.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.database.DbOperation;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ModelReferencesFinder extends DbOperation {

    private final String modelId;

    @Override
    public String getSqlTemplate() {
        throw new AssertionError("Should not have been invoked!");
    }

    public ModelReferencesFinder(final String modelId) {
        this.modelId = modelId;
    }

    public Set<String> getReferences() throws DbException {
        Set<String> references = new HashSet<String>();
        Connection connection = getConnection();
        ResultSet results = null;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            results = statement.executeQuery(String.format("SELECT `bibTeXUri` FROM `ModelBibTeX` WHERE `modelId`='%s'", modelId));
            while (results.next()) {
                references.add(results.getString(1));
            }
        } catch (SQLException ex) {
            throw new DbException(ex);
        } finally {
            try {
                results.close();
            } catch (SQLException ex) {
                Logger.getLogger(ModelReferencesFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                statement.close();
            } catch (SQLException ex) {
                Logger.getLogger(ModelReferencesFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
            close();
        }
        return references;
    }
}
