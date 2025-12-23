package com.folio4me.dto;

/**
 * 이미지 업로드 응답 DTO.
 * POST /sessions/{id}/images 응답.
 */
public class ImageUploadResponse {

    /** 업로드된 이미지 파일명 */
    private String filename;

    /** 이미지 접근 URL 경로 */
    private String url;

    /** 파일 크기 (바이트) */
    private long size;

    /** 업로드 성공 여부 */
    private boolean success;

    /** 오류 메시지 (실패 시) */
    private String errorMessage;

    public ImageUploadResponse() {
    }

    public static ImageUploadResponse success(String filename, String url, long size) {
        ImageUploadResponse response = new ImageUploadResponse();
        response.filename = filename;
        response.url = url;
        response.size = size;
        response.success = true;
        return response;
    }

    public static ImageUploadResponse failure(String errorMessage) {
        ImageUploadResponse response = new ImageUploadResponse();
        response.success = false;
        response.errorMessage = errorMessage;
        return response;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
