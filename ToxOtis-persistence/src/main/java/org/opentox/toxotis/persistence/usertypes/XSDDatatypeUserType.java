package org.opentox.toxotis.persistence.usertypes;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class XSDDatatypeUserType implements org.hibernate.usertype.UserType {

    private static final int[] SQL_TYPES = {Types.VARCHAR};

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Class returnedClass() {
        return XSDDatatype.class;
    }

    public boolean equals(Object x, Object y)
            throws HibernateException {
        if (x == y) {
            return true;
        } else if (x == null || y == null) {
            return false;
        } else {
            return x.equals(y);
        }
    }

    public Object deepCopy(Object value) {
        return value;
    }

    public boolean isMutable() {
        return false;
    }

    public Object nullSafeGet(ResultSet resultSet,
            String[] names,
            Object owner)
            throws HibernateException, SQLException {
        String name = resultSet.getString(names[0]);        
        return resultSet.wasNull() ? null : new XSDDatatype(name);

    }

    public void nullSafeSet(PreparedStatement statement,
            Object value,
            int index)
            throws HibernateException, SQLException {

        if (value == null) {
            statement.setNull(index, Types.VARCHAR);
        } else {
            XSDDatatype valueXsd = (XSDDatatype) value;
            String xsdUri = valueXsd.getURI();
            xsdUri = xsdUri.replaceAll("http://www.w3.org/2001/XMLSchema#", "");
            statement.setString(index, xsdUri);
        }
    }

    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    public Serializable disassemble(Object o) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object assemble(Serializable srlzbl, Object o) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object replace(Object o, Object o1, Object o2) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
