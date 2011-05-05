package org.opentox.toxotis.database.engine;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.toxotis.database.DbOperation;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DeleteOldComponents extends DbOperation {

    private int days = 15;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int delete() throws DbException {
        Connection connection = null;
        try {
            connection = getConnection();
            Statement statement = connection.createStatement();
            //TODO: Change this command using...
            //  select timediff(deletionDate,created) from OTComponent where enabled=false;
            // CHECK OUT http://dev.mysql.com/doc/refman/5.5/en/date-and-time-functions.html#function_timediff
            return statement.executeUpdate("DELETE FROM OTComponent WHERE (month(current_date())*30+"
                    + "day(current_date())-(month(OTComponent.created )*30+day(OTComponent.created ))) >= " + getDays()
                    + " AND OTComponent.enabled=false");
        } catch (SQLException ex) {
            throw new DbException(ex);
        } catch (DbException ex) {
            throw ex;
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new DbException(ex);
            }
        }

    }

    @Override
    public String getSqlTemplate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
