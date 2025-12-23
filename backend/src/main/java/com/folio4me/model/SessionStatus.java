package com.folio4me.model;

/**
 * 세션의 상태를 정의합니다.
 */
public enum SessionStatus {

    /** 진행 중 */
    ACTIVE,

    /** 완료됨 (about.confirmed = true) */
    COMPLETED,

    /** 종료됨 (비기술 직군 등) */
    TERMINATED,

    /** 만료됨 (TTL 초과) */
    EXPIRED
}
