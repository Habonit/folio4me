package com.folio4me.exception;

/**
 * 유효하지 않은 기간 형식에 대한 예외.
 * 기간 형식: YYYYMMDD~YYYYMMDD 또는 YYYYMMDD~present
 */
public class InvalidPeriodException extends RuntimeException {

    private final String period;

    public InvalidPeriodException(String period) {
        super("유효하지 않은 기간 형식입니다: " + period + " (예: 20230301~20231231 또는 20230301~present)");
        this.period = period;
    }

    public InvalidPeriodException(String period, String message) {
        super(message);
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }
}
