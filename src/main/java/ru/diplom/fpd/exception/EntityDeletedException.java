package ru.diplom.fpd.exception;

public class EntityDeletedException extends RuntimeException {

    private final String entity;

    public EntityDeletedException(String entity) {
        this.entity = entity;
    }

    public EntityDeletedException(String entity, String message) {
        super(message);
        this.entity = entity;
    }

    public EntityDeletedException(String entity, String message, Throwable cause) {
        super(message, cause);
        this.entity = entity;
    }

    public EntityDeletedException(Throwable cause, String entity) {
        super(cause);
        this.entity = entity;
    }

    public EntityDeletedException(String entity, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "EntityDeletedException{" +
                "entity='" + entity + ", " +
                "message='" + super.getMessage() +
                '}';
    }
}
