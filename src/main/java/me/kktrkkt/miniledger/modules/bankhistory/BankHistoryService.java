package me.kktrkkt.miniledger.modules.bankhistory;

import lombok.RequiredArgsConstructor;
import me.kktrkkt.miniledger.infra.csv.Parser;
import me.kktrkkt.miniledger.modules.customer.Customer;
import me.kktrkkt.miniledger.modules.customer.CustomerRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankHistoryService {

    private final Parser csvParser;
    private final BankHistoryRepository bankHistoryRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public List<BankHistory> save(String customerId, MultipartFile csvFile) {
        List<Map<String, String>> lines = csvParser.parse(csvFile);

        List<BankHistory> newBankHistoryList = lines.stream()
                .map(tx->bankHistoryRepository.
                        findByTransactionDateTimeAndDescriptionAndDepositAmountAndWithdrawalAmount(
                            LocalDateTime.parse(tx.get("거래일시"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                            tx.get("적요"),
                            new BigDecimal(tx.get("입금액")),
                            new BigDecimal(tx.get("출금액"))
                        ).orElseGet(()->{
                            BankHistory bankHistory = convertToEntity(tx);
                            bankHistory.setCustomer(customerRepository.findById(customerId)
                                    .orElseThrow(() -> new IllegalArgumentException(
                                            "고객을 찾을 수 없습니다: " + customerId)));
                            return bankHistory;
                        }
                ))
                .toList();

        return bankHistoryRepository.saveAll(newBankHistoryList);
    }

    private BankHistory convertToEntity(Map<String, String> stringStringMap) {
        return BankHistory.builder()
                .transactionDateTime(LocalDateTime.parse(stringStringMap.get("거래일시"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .description(stringStringMap.get("적요"))
                .depositAmount(new BigDecimal(stringStringMap.get("입금액")))
                .withdrawalAmount(new BigDecimal(stringStringMap.get("출금액")))
                .balanceAfter(new BigDecimal(stringStringMap.get("거래후잔액")))
                .branch(stringStringMap.get("거래점"))
                .build();
    }
}
