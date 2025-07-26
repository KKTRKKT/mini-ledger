package me.kktrkkt.miniledger.modules.bankhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Transactional(readOnly = true)
public interface BankHistoryRepository extends JpaRepository<BankHistory, Long> {
    Optional<BankHistory> findByTransactionDateTimeAndDescriptionAndDepositAmountAndWithdrawalAmount(LocalDateTime transactionDateTime, String description, BigDecimal depositAmount, BigDecimal withdrawalAmount);
}
