package fr.eseoye.eseoye.exceptions;

import fr.eseoye.eseoye.io.databases.tables.ITable;

public class DataCreationException extends RuntimeException {

    private static final long serialVersionUID = 6375008759164049427L;

    private final CreationExceptionReason reason;

    public <E extends ITable> DataCreationException(Class<E> cls, CreationExceptionReason reason) {
        super("An error occured in the class " + cls.getName());
        this.reason = reason;
    }

    public <E extends ITable> DataCreationException(Class<E> cls, String message, CreationExceptionReason reason) {
        super("An error occured in the class " + cls.getName() + " Message:[" + message + "]");
        this.reason = reason;
    }

    public DataCreationException(CreationExceptionReason reason) {
        this.reason = reason;
    }

    public DataCreationException(String message, CreationExceptionReason reason) {
        super(message);
        this.reason = reason;
    }

    public CreationExceptionReason getReason() {
        return reason;
    }

    public enum CreationExceptionReason {

        FAILED_IMAGE_UPLOAD,
        FAILED_DB_CREATION
    }
}
