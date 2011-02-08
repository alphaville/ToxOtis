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
package org.opentox.toxotis.exceptions.impl;

import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.exceptions.IServiceInvocationException;

/**
 * Generic exception includes all kinds of possible exceptional events that
 * might
 * @author Sopasakis Pantelis
 */
public class ServiceInvocationException extends Exception implements IServiceInvocationException {

    private int httpStatus = -1;
    private String actor;
    private String details;
    private static final String ERROR_CODE = "GenericServiceException";
    private ErrorReport errorReport;

    /**
     * Creates a new instance of <code>ServiceInvocationException</code> without detail message.
     */
    public ServiceInvocationException() {
    }

    /**
     * Constructs an instance of <code>ServiceInvocationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ServiceInvocationException(String msg) {
        super(msg);
    }

    public ServiceInvocationException(Throwable cause) {
        super(cause);
    }

    public ServiceInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String getActor() {
        return actor;
    }

    @Override
    public void setActor(String actor) {
        this.actor = actor;
    }

    @Override
    public String getDetails() {
        StringBuilder errorDetailsMessage = new StringBuilder();
        if (details != null) {
            errorDetailsMessage.append(details);
            errorDetailsMessage.append("\n");
        }
        StackTraceElement[] stackTRace = getStackTrace();
        if (stackTRace != null) {
            errorDetailsMessage.append("Internal Stack Trace:\n");
            for (StackTraceElement ste : stackTRace) {
                errorDetailsMessage.append(ste);
                errorDetailsMessage.append("\n");
            }
        }
        return new String(errorDetailsMessage);
    }

    @Override
    public ErrorReport asErrorReport() {
        ErrorReport er = new ErrorReport();
        er.setMessage(getMessage());
        er.setDetails(getDetails());
        er.setActor(getActor());
        er.setErrorCode(this.getClass().getName());
        er.setHttpStatus(getHttpStatus());
        er.setErrorCause(getErrorReport());
        er.setEnabled(true);
        return er;
    }

    

    @Override
    public ErrorReport getErrorReport() {
        if (this.errorReport == null) {
            errorReport = new ErrorReport(getHttpStatus(), getActor(), getMessage(), getDetails(), actor);
        }
        return errorReport;
    }

    @Override
    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public void setErrorReport(ErrorReport errorReport) {
        this.errorReport = errorReport;
    }

    @Override
    public String errorCode() {
        return ERROR_CODE;
    }
}
