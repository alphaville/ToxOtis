package org.opentox.toxotis.persistence.usertypes;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OntologicalClassUserType implements org.hibernate.usertype.UserType {

    private static final int[] SQL_TYPES = {java.sql.Types.VARCHAR};

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Class returnedClass() {
        return OntologicalClass.class;
    }

    public boolean equals(Object x, Object y) {
        return x == y;
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
        OntologicalClass oc = OTClasses.forName(name);
        return oc;
    }

    public void nullSafeSet(PreparedStatement statement,
            Object value,
            int index)
            throws HibernateException, SQLException {

        if (value == null) {
            statement.setNull(index, Types.VARCHAR);
        } else {
            OntologicalClass oc = (OntologicalClass) value;
            statement.setString(index, oc.getName());
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
