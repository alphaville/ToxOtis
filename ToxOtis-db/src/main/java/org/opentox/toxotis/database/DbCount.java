/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */


package org.opentox.toxotis.database;

import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 */
public abstract class DbCount extends DbOperation {

    protected String table;
    protected String innerJoin;
    protected String where;
    protected String countableColumn = "*";
    protected boolean includeDisabled = false;

    public boolean isIncludeDisabled() {
        return includeDisabled;
    }

    public void setIncludeDisabled(boolean includeDisabled) {
        this.includeDisabled = includeDisabled;
    }

    public void setCountableColumn(String countableColumn) {
        this.countableColumn = countableColumn;
    }

    protected void setInnerJoin(String innerJoin) {
        this.innerJoin = innerJoin;
    }

    protected void setTable(String table) {
        this.table = table;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    @Override
    public String getSqlTemplate() {
        return "SELECT COUNT(%s) FROM %s %s %s";
    }

    protected String getSql() {

        StringBuilder innerJoinClause = new StringBuilder("");
        if (innerJoin != null) {
            innerJoinClause.append("INNER JOIN ");
            innerJoinClause.append(innerJoin);
            innerJoinClause.append(" ");
        }

        StringBuilder whereClause = new StringBuilder("");
        if (where != null) {
            whereClause.append("WHERE ");
            whereClause.append(where);
            whereClause.append(" ");
        }

        return String.format(getSqlTemplate(), countableColumn, table, innerJoinClause, whereClause);
    }

    public abstract int count() throws DbException;
}
