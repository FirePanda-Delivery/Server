package ru.diplom.FirePandaDelivery.exception;

public class ImageExtensionNotSupportedException extends Exception {

    public ImageExtensionNotSupportedException() {
    }

    public ImageExtensionNotSupportedException(String message) {
        super(message);
    }

    public ImageExtensionNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageExtensionNotSupportedException(Throwable cause) {
        super(cause);
    }

    public ImageExtensionNotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
