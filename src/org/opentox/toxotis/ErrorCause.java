package org.opentox.toxotis;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum ErrorCause implements java.io.Serializable{

    AlgorithmNotFoundInCache,
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
    UnauthorizedUser,
    UnknownCauseOfException,
    UpdateFailed,
            
    // CONFIG:
    XA1, XA2, XA3,

    //BATCH PROCESSOR:
    XBP1, XBP2, XBP3, XBP7,
    // PARALLEL PROCESSOR:
    XPP1, XPP2, XPP3, XPP7,

    // PIPELINE
    XPIP1001,                 
    Xtime11735;            
}
