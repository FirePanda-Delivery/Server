package ru.diplom.FirePandaDelivery.exception;

public class CourierIsAlreadyActiveException extends RuntimeException {
    public CourierIsAlreadyActiveException() {
    }

    public CourierIsAlreadyActiveException(String message) {
        super(message);
    }

    public CourierIsAlreadyActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public CourierIsAlreadyActiveException(Throwable cause) {
        super(cause);
    }

    public CourierIsAlreadyActiveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
