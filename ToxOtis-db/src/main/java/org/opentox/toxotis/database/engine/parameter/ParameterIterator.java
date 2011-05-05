package org.opentox.toxotis.database.engine.parameter;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.database.DbIterator;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.LiteralValue;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ParameterIterator extends DbIterator<Parameter> {

    private final VRI baseUri;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ParameterIterator.class);

    public ParameterIterator(final ResultSet rs, final VRI baseUri) {
        super(rs);
        this.baseUri = baseUri;
    }

    @Override
    public Parameter next() throws DbException {
        Parameter nextParam = null;
        try {
            nextParam = new Parameter(new VRI(baseUri).augment("parameter", rs.getString("id")));
            nextParam.setName(rs.getString("name"));
            nextParam.setTypedValue(new LiteralValue(rs.getString("value"),
                    (XSDDatatype) TypeMapper.getInstance().getTypeByName(rs.getString("valueType"))));
            nextParam.setScope(Parameter.ParameterScope.valueOf(rs.getString("scope")));
        } catch (final SQLException ex) {
            final String msg = "Error reading result set on error reports";
            logger.warn(msg, ex);
            throw new DbException(msg, ex);
        }
        return nextParam;

    }
}
