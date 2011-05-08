package org.opentox.toxotis.database.engine.bibtex;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.exception.DbException;

/**
 * Associate a list of BibTeX with a Model.
 * @author Pantelis Sopasakis
 */
public class AssociateBibTeX extends DbWriter {

    private static String SQL;
    private String modelId;
    private String[] bibTexIds;
    private PreparedStatement prepared;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AssociateBibTeX.class);

    public AssociateBibTeX(VRI modelId, VRI... bibTex) {
        this.modelId = modelId.toString();
        bibTexIds = new String[bibTex.length];
        int i = 0;
        for (VRI vri : bibTex) {
            bibTexIds[i] = vri.toString();
            i++;
        }
    }

    /**
     *
     * @param modelId
     *      The id of the model
     * @param bibTex
     *      The <b>URIs</b> of the BibTeX resources (not the IDs)
     */
    public AssociateBibTeX(String modelId, String... bibTex) {
        this.modelId = modelId;
        this.bibTexIds = bibTex;
    }

    @Override
    public int write() throws DbException {
        if (SQL == null) {
            setInsertType(InsertType.INSERT_IGNORE);
            setTable("ModelBibTeX");
            setTableColumns("modelId", "bibTeXUri");
            SQL = getSql();
        }
        Connection connection = getConnection();
        try {
            prepared = connection.prepareStatement(SQL);
            for (String bibtex : bibTexIds) {
                prepared.setString(1, modelId);
                prepared.setString(2, bibtex);
                prepared.addBatch();
            }
            return prepared.executeUpdate();
        } catch (SQLException ex) {
            throw new DbException(ex);
        }finally{
            SQLException sqle = null;
            if (prepared!=null){
                try {
                    prepared.close();
                } catch (SQLException ex) {
                    final String msg = "SQL exception occured while closing the  SQL statement for "
                            + "adding a model in the database consisting of particular batched statements";
                    logger.warn(msg, ex);
                    sqle = ex;
                }
            }
            close();
            if (sqle!=null){
                throw new DbException(sqle);
            }
        }
    }
}
