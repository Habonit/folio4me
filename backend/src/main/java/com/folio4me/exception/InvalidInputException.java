package com.folio4me.exception;

/**
 * 유효하지 않은 입력 값에 대한 예외.
 */
public class InvalidInputException extends RuntimeException {

    private final String fieldName;
    private final String invalidValue;

    public InvalidInputException(String message) {
        super(message);
        this.fieldName = null;
        this.invalidValue = null;
    }

    public InvalidInputException(String fieldName, String invalidValue, String message) {
        super(message);
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
        this.fieldName = null;
        this.invalidValue = null;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getInvalidValue() {
        return invalidValue;
    }
}
