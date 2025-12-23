package com.folio4me.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.folio4me.config.AppConfig;
import com.folio4me.exception.StorageException;
import com.folio4me.model.Portfolio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 파일 기반 저장소 서비스.
 * 세션별 포트폴리오 데이터 저장 및 로드.
 */
@Service
public class StorageService {

    private static final Logger log = LoggerFactory.getLogger(StorageService.class);
    private static final String PORTFOLIO_FILENAME = "portfolio.json";

    private final AppConfig appConfig;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public StorageService(AppConfig appConfig, ObjectMapper objectMapper) {
        this.appConfig = appConfig;
        this.objectMapper = objectMapper;
    }

    /**
     * 세션 디렉토리 생성.
     *
     * @param sessionId 세션 ID
     */
    public void createSessionDirectory(String sessionId) {
        Path sessionDir = getSessionPath(sessionId);
        try {
            Files.createDirectories(sessionDir);
            log.info("Created session directory: {}", sessionDir);
        } catch (IOException e) {
            throw new StorageException(sessionDir.toString(), "세션 디렉토리 생성 실패", e);
        }
    }

    /**
     * 포트폴리오 저장.
     *
     * @param sessionId 세션 ID
     * @param portfolio 포트폴리오 데이터
     */
    public void savePortfolio(String sessionId, Portfolio portfolio) {
        ReentrantLock lock = locks.computeIfAbsent(sessionId, k -> new ReentrantLock());
        lock.lock();
        try {
            Path portfolioPath = getPortfolioPath(sessionId);

            // 디렉토리가 없으면 생성
            Files.createDirectories(portfolioPath.getParent());

            // JSON으로 저장
            objectMapper.writeValue(portfolioPath.toFile(), portfolio);
            log.debug("Saved portfolio for session {}", sessionId);

        } catch (IOException e) {
            throw new StorageException(
                getPortfolioPath(sessionId).toString(),
                "포트폴리오 저장 실패",
                e
            );
        } finally {
            lock.unlock();
        }
    }

    /**
     * 포트폴리오 로드.
     *
     * @param sessionId 세션 ID
     * @return 포트폴리오 데이터, 없으면 null
     */
    public Portfolio loadPortfolio(String sessionId) {
        Path portfolioPath = getPortfolioPath(sessionId);

        if (!Files.exists(portfolioPath)) {
            return null;
        }

        ReentrantLock lock = locks.computeIfAbsent(sessionId, k -> new ReentrantLock());
        lock.lock();
        try {
            Portfolio portfolio = objectMapper.readValue(portfolioPath.toFile(), Portfolio.class);
            log.debug("Loaded portfolio for session {}", sessionId);
            return portfolio;
        } catch (IOException e) {
            throw new StorageException(
                portfolioPath.toString(),
                "포트폴리오 로드 실패",
                e
            );
        } finally {
            lock.unlock();
        }
    }

    /**
     * 세션 디렉토리 삭제.
     *
     * @param sessionId 세션 ID
     */
    public void deleteSessionDirectory(String sessionId) {
        Path sessionDir = getSessionPath(sessionId);

        if (!Files.exists(sessionDir)) {
            return;
        }

        try {
            // 디렉토리 내 파일 삭제
            Files.walk(sessionDir)
                .sorted((a, b) -> b.compareTo(a)) // 깊은 파일부터 삭제
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        log.warn("Failed to delete: {}", path, e);
                    }
                });

            log.info("Deleted session directory: {}", sessionDir);
        } catch (IOException e) {
            throw new StorageException(sessionDir.toString(), "세션 디렉토리 삭제 실패", e);
        }
    }

    /**
     * 이미지 저장.
     *
     * @param sessionId 세션 ID
     * @param filename 파일명
     * @param data 이미지 데이터
     * @return 저장된 파일 경로
     */
    public String saveImage(String sessionId, String filename, byte[] data) {
        Path imagePath = getSessionPath(sessionId).resolve("images").resolve(filename);

        try {
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, data);
            log.info("Saved image: {}", imagePath);
            return imagePath.toString();
        } catch (IOException e) {
            throw new StorageException(imagePath.toString(), "이미지 저장 실패", e);
        }
    }

    private Path getSessionPath(String sessionId) {
        return Paths.get(appConfig.getDataBasePath(), sessionId);
    }

    private Path getPortfolioPath(String sessionId) {
        return getSessionPath(sessionId).resolve(PORTFOLIO_FILENAME);
    }
}
