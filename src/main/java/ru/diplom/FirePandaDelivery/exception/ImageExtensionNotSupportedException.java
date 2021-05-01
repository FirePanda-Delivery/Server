package ru.diplom.FirePandaDelivery.exception;

public class ImageExtensionNotSupportedException extends Exception {

    private final String extension;

    public ImageExtensionNotSupportedException() {
        this.extension = "no extension";
    }
    public ImageExtensionNotSupportedException(String message) {
        super(message);
        this.extension = "no extension";

    }

    public ImageExtensionNotSupportedException(String message, String extension) {
        super(message);
        this.extension = extension;
    }

    public ImageExtensionNotSupportedException(String message, Throwable cause, String extension) {
        super(message, cause);
        this.extension = extension;
    }

    public ImageExtensionNotSupportedException(Throwable cause, String extension) {
        super(cause);
        this.extension = extension;
    }

    public ImageExtensionNotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String extension) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.extension = extension;
    }
}
