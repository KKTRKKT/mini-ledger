package me.kktrkkt.miniledger.api.web;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kktrkkt.miniledger.modules.accounting.AccountingProcessResponse;
import me.kktrkkt.miniledger.modules.accounting.AccountingResponse;
import me.kktrkkt.miniledger.modules.accounting.AccountingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountingController {

    static final String ACCOUNTING_BASE_API = "/api/v1/accounting";
    static final String ACCOUNTING_PROCESS_API = ACCOUNTING_BASE_API + "/process";
    static final String ACCOUNTING_RECORDS_API = ACCOUNTING_BASE_API + "/records";

    private final AccountingService accountingService;

    /**
     * 자동 회계 처리 API
     *
     * @param csvFile bank_transactions.csv 파일
     * @param rulesFile rules.json 파일
     * @return 처리 결과
     */
    @PostMapping(path = ACCOUNTING_PROCESS_API, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AccountingProcessResponse> processAccounting(
            @NotNull(message = "고객사 ID는 필수입니다")
            @RequestParam("customerId") String customerId,

            @NotNull(message = "CSV 파일은 필수입니다")
            @RequestParam("csvFile") MultipartFile csvFile,

            @NotNull(message = "규칙 파일은 필수입니다")
            @RequestParam("rulesFile") MultipartFile rulesFile) {

        log.info("자동 회계 처리 요청 - CSV 파일: {}, 규칙 파일: {}",
                csvFile.getOriginalFilename(),
                rulesFile.getOriginalFilename());

        // 파일 유효성 검증
        validateFiles(csvFile, rulesFile);

        try {
            AccountingProcessResponse response = accountingService.processAccounting(customerId, csvFile, rulesFile);

            log.info("자동 회계 처리 완료 - 성공: {}, 실패: {}, 전체: {}",
                    response.successCount(),
                    response.failureCount(),
                    response.totalCount());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("자동 회계 처리 중 오류 발생", e);
            throw new RuntimeException("회계 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 회계 기록 조회 API
     *
     * @param companyId 고객사 ID
     * @return 회계 기록 리스트
     */
    @GetMapping(ACCOUNTING_RECORDS_API)
    public List<AccountingResponse> records(
            @RequestParam(name = "companyId") String companyId) {
        return accountingService.getRecords(companyId);
    }

    /**
     * 파일 유효성 검증
     */
    private void validateFiles(MultipartFile csvFile, MultipartFile rulesFile) {
        // CSV 파일 검증
        if (csvFile == null || csvFile.isEmpty()) {
            throw new IllegalArgumentException("CSV 파일이 비어있습니다");
        }

        if (!isValidCsvFile(csvFile)) {
            throw new IllegalArgumentException("유효하지 않은 CSV 파일입니다");
        }

        // Rules 파일 검증
        if (rulesFile == null || rulesFile.isEmpty()) {
            throw new IllegalArgumentException("규칙 파일이 비어있습니다");
        }

        if (!isValidJsonFile(rulesFile)) {
            throw new IllegalArgumentException("유효하지 않은 JSON 파일입니다");
        }
    }

    /**
     * CSV 파일 유효성 검증
     */
    private boolean isValidCsvFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return false;
        }

        String contentType = file.getContentType();
        return fileName.toLowerCase().endsWith(".csv") ||
                "text/csv".equals(contentType) ||
                "application/csv".equals(contentType);
    }

    /**
     * JSON 파일 유효성 검증
     */
    private boolean isValidJsonFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return false;
        }

        String contentType = file.getContentType();
        return fileName.toLowerCase().endsWith(".json") ||
                "application/json".equals(contentType);
    }
}
