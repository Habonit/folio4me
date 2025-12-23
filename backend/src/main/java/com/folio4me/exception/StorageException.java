package com.folio4me.exception;

/**
 * 파일 저장/로드 관련 예외.
 */
public class StorageException extends RuntimeException {

    private final String path;

    public StorageException(String message) {
        super(message);
        this.path = null;
    }

    public StorageException(String path, String message) {
        super(message);
        this.path = path;
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
        this.path = null;
    }

    public StorageException(String path, String message, Throwable cause) {
        super(message, cause);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
