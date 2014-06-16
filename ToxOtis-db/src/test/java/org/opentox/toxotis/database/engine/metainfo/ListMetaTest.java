/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.database.engine.metainfo;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.database.engine.model.ModelIterator;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.pool.DataSourceFactory;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.MetaInfoDeblobber;

/**
 *
 * @author imt
 */
public class ListMetaTest {

    public ListMetaTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        org.opentox.toxotis.database.TestUtils.setUpDB();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DataSourceFactory.getInstance().close();
    }

    @Before
    public void setUp() {
    }

    @After
    public synchronized void tearDown() {
    }

    @Test
    public synchronized void testList() throws InterruptedException, DbException, SQLException {
        int limit = 255;
        String listMetaInfo = "SELECT `MetaInfo`.`id`, uncompress(`MetaInfo`.`meta`) FROM `MetaInfo`";
        String setTitle = "UPDATE `MetaInfo` SET `title`='%s' WHERE `id`=%s";
        DataSourceFactory factory = DataSourceFactory.getInstance();
        Connection connection = null;
        try {
            connection = factory.getDataSource().getConnection();
        } catch (final SQLException ex) {
            final String msg = "Cannot get connection from the connection pool";
            throw new DbException(msg, ex);
        }
        java.sql.Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(listMetaInfo);
        while (rs.next()) {
            String id = rs.getString(1);
            System.out.println(id);
            MetaInfoDeblobber mid = new MetaInfoDeblobber(rs.getBlob(2));
            MetaInfo mi = mid.toMetaInfo();
            java.util.Set<LiteralValue> setOfTitles = mi.getTitles();
            String title = "";
            LiteralValue lit = null;
            if (setOfTitles != null && setOfTitles.size() > 0) {
                Iterator<LiteralValue> iter = setOfTitles.iterator();
                while (iter.hasNext()) {
                    lit = iter.next();
                    if (lit != null) {
                        title += lit.getValue() + " ";
                    }
                }
            }
            if (title.length() > limit) {
                title = title.substring(0, limit-1);
            }
            String updateCommand = String.format(setTitle, title, id);
            java.sql.PreparedStatement updateStatement = connection.prepareStatement(updateCommand);
            System.out.println(updateStatement.executeUpdate());
            updateStatement.close();
        }
        rs.close();
        connection.close();

    }

}
