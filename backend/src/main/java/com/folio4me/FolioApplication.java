package com.folio4me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * folio4me 백엔드 애플리케이션의 메인 진입점.
 *
 * <p>포트폴리오 콘텐츠 수집을 위한 대화형 백엔드 서비스를 제공합니다.</p>
 */
@SpringBootApplication
public class FolioApplication {

    /**
     * 애플리케이션을 시작합니다.
     *
     * @param args 커맨드 라인 인자
     */
    public static void main(String[] args) {
        SpringApplication.run(FolioApplication.class, args);
    }
}
