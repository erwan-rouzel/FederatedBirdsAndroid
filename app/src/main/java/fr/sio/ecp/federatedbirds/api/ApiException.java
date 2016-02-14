package fr.sio.ecp.federatedbirds.api;

import fr.sio.ecp.federatedbirds.model.Error;

/**
 * A custom exception that will be thrown be our API servlets and propagated to the client application.
 * The exception is created with an HTTP status code, a code and a message.
 * These information will be sent as JSON to the client by the JsonServlet
 */
public class ApiException extends Exception {

    private Error mError;

    public ApiException(int status, String code, String message) {
        super(message);
        // When the exception is created, prepare an Error object that will be sent to the client
        mError = new Error();
        mError.status = status;
        mError.code = code;
        mError.message = message;
    }

    public Error getError() {
        return mError;
    }

    public String toString() {
        return mError.code + " - " + mError.message + " (" + mError.status + ")";
    }
}
