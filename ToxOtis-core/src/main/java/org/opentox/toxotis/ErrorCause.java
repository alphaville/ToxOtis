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
package org.opentox.toxotis;

import org.opentox.toxotis.ontology.collection.KnoufBibTex;
import org.opentox.toxotis.ontology.collection.KnoufDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;

/**
 * Some standard sources of exceptional events that might happen on the server
 * side and are announceable to the client providing some classification information
 * regarding the event.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum ErrorCause implements java.io.Serializable {

    /**
     * The client requested for some algorithm under /algorithm that cannot be
     * found on the server. The server should respond with a status code 404.
     */
    AlgorithmNotFound,
    /**
     * The user provided authentication credentials or authentication token that
     * was structurally valid but could not be authenticated against the SSO service.
     * A possible explanation might be that the token of the user has expired. Clients
     * should not repeat the same request but should first authenticate themselves again
     * against the SSO service.
     */
    AuthenticationFailed,
    /**
     * Client attempted to create a new bibtex resource without providing an author
     * which is a mandatory field.
     */
    AuthorNotProvided,
    /**
     * The client requested for some bibtex resource under /bibtex that cannot be
     * found on the server. The server should respond with a status code 404.
     */
    BibTexNotFoundInDatabase,
    /**
     * Client provided a bibtex type that is not recognized by the server as a
     * valid bibtex type.
     */
    BibTypeNotSupported,
    /**
     * Some exception hinders the communication between two peers - usually the
     * service invoked by the client and some third web service. In Java this usually
     * appears as an IO Exception.
     */
    CommunicationError,
    /**
     * The connection with a remote service failed. Almost equivalent with {@link
     * #CommunicationError} sometimes.
     */
    ConnectionException,
    /**
     * The content type you provided is not accepted as a valid MIME for the POST
     * operation you attempt to perform.
     */
    ContentTypeNotSupported,
    /**
     * Exception related to the database connectivity of the server. This should be
     * considered as a severe internal server error (ISE) and the accompanying status
     * code should be 500.
     */
    DatabaseError,
    /**
     * The prediction feature the client provided is not found in the dataset or other
     * features specified by the client were not found in the dataset. This is a client
     * error and the accompanying status is expected to be 400.
     */
    FeatureNotInDataset,
    /**
     * Error related to an IO exception thrown while trying to access a file. Such
     * an exception when thrown by a server should be considered to be an ISE followed
     * by the HTTP status code 500.
     */
    FileReadingError,
    /**
     * Error related to writing to a file.
     */
    FileWritingError,
    /**
     * Dataset filtering algorithm failure due to some internal error or other non
     * standard unexpicable condition.
     */
    FilteringError,
    /**
     * The client posted to the bibtex creation service an invalid resource which
     * could not be parsed by the service as a valid representation.
     */
    ImproperBibTexResource,
    /**
     * The server cannot read from an input stream.
     */
    InputStreamUnreadable,
    /**
     * The dataset URI provided by the client is not valid either because it is not
     * found on the correspondig server (status 404 returned) of does not correspond
     * to a valid dataset resource.
     */
    InvalidDatasetURI,
    /**
     * The model URI provided by the client is not valid either because it is not
     * found on the correspondig server (status 404 returned) of does not correspond
     * to a valid model resource.
     */
    InvalidModelURI,
    /**
     * The feature URI provided by the client is not valid either because it is not
     * found on the correspondig server (status 404 returned) of does not correspond
     * to a valid feature resource.
     */
    InvalidFeatureURI,
    /**
     * The service URI provided by the client is not valid either because it is not
     * found on the correspondig server (status 404 returned) of does not correspond
     * to a valid service resource.
     */
    InvalidServiceURI,
    /**
     * The task URI provided by the client is not valid either because it is not
     * found on the correspondig server (status 404 returned) of does not correspond
     * to a valid task resource.
     */
    InvalidTaskURI,
    /**
     * Some remote service returned a success status code (200/201/202) and while
     * a URI was expected since it was so specified by the 'Accept' HTTP header of
     * the request, the returned response body cannot be cast as a valid URI.
     */
    InvalidUriReturnedFromRemote,
    /**
     * The token provided by the user is not (structurally) valid and cannot
     * correspond to a user.
     */
    InvalidToken,
    /**
     * The provided Knouf BibTeX class was not found. Valid classes are
     * {@link KnoufBibTex#Article() Article}, {@link KnoufBibTex#Book()  Book},
     * {@link KnoufBibTex#Conference()  Conference}, {@link KnoufBibTex#Entry()  Entry},
     * and {@link KnoufBibTex#Phdthesis()  PhD Thesis}.
     */
    KnoufBibTexClassNotFound,
    /**
     * The knouf datatype property requested was not found in the collection of
     * {@link KnoufDatatypeProperties BibTeX properties}
     */
    KnoufDatatypePropertyNotFound,
    /**
     * The requested media type, as specified in the request header 'Accept' is not
     * supported for this particular service.
     */
    MediaTypeNotSupported,
    /**
     * The client was expected to provide a numeric value for a parameter, however a
     * non-numeric value was provided. The same exception refers also to cases where
     * an integer value was expected but a float was provided.
     */
    NumberExpected,
    /**
     * OpenTox datatype property not found in the collection of standard {@link OTDatatypeProperties collection}
     * of datatype properties.
     */
    OTDatatypePropertyNotFound,
    /**
     * A parameter provided by the client is out of range.
     */
    OutOfRange,
    /**
     * A policy could not be created.
     */
    PolicyCreationError,
    /**
     * A policy could not be deleted.
     */
    PolicyDeletionError,
    /**
     * Error related to a prediction session. A prediction could not be carried out
     * due to some internal server error. Details should be provided to the client to
     * help debugging.
     */
    PredictionError,
    /**
     * A resource could not be converted to the requested representation and
     * returned to the client but the resource seems to be valid and the representation
     * should be normally available. This is considered to be a serious internal server
     * error.
     */
    PublicationError,
    /**
     * A resource was created but cannot be registered into the database. This is also
     * a {@link #DatabaseError } but is rendered more specific if you use this element.
     */
    RegistrationFailed,
    /**
     * Some input or output stream could not be closed due to an I/O or other exception.
     * Such an exception should be considered very serious and handled carefully as it
     * can lead to memory leaks and crash the server!
     */
    StreamCouldNotClose,
    /**
     * Some unexpected exception was thrown while training a model.
     */
    TrainingError,
    /**
     * The client requested for some task resource under /task that cannot be
     * found on the server. The server should respond with a status code 404.
     */
    TaskNotFoundError,
    /**
     * The client is authenticated but not authorized to perform the underlying operation.
     */
    UnauthorizedUser,
    /**
     * An exeption was thrown but it cannot be classified to any of the other
     * classes of exceptional events in this class.
     */
    UnknownCauseOfException;
}
