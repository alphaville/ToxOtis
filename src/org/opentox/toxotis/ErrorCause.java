package org.opentox.toxotis;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum ErrorCause implements java.io.Serializable{

    //TODO: Update this list, maybe remove some...
    AlgorithmNotFound,
    AuthenticationFailed,
    AuthorNotProvided,    
    BibTexNotFoundInDatabase,
    BibTypeNotSupported,
    CommunicationError,
    ConnectionException,
    ContentTypeNotSupported,
    DatabaseError,
    FeatureNotInDataset,
    FileReadingError,
    FileWritingError,
    FilteringError,
    ImproperBibTexResource,
    InputStreamUnreadable,
    InvalidDatasetURI,
    InvalidModelURI,
    InvalidFeatureURI,
    InvalidServiceURI,
    InvalidTaskURI,
    InvalidUriReturnedFromRemote,
    InvalidToken,
    KnoufBibTexClassNotFound,
    KnoufDatatypePropertyNotFound,
    MediaTypeNotSupported,
    NumberExpected,
    OTDatatypePropertyNotFound,
    OutOfRange,
    PolicyCreationError,
    PolicyDeletionError,
    PredictionError,
    PublicationError,
    RegistrationFailed,
    StreamCouldNotClose,
    TrainingError,
    TaskNotFoundError,
    UnauthorizedUser,
    UnknownCauseOfException;
}
