package me.kktrkkt.miniledger.modules.bankhistory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BankHistoryCreateDto(
    LocalDateTime transactionDateTime,
    String description,
    BigDecimal depositAmount,
    BigDecimal withdrawalAmount,
    BigDecimal balanceAfter,
    String branch
) {
    public BankHistory toEntity() {
        return BankHistory.builder()
            .transactionDateTime(transactionDateTime)
            .description(description)
            .depositAmount(depositAmount)
            .withdrawalAmount(withdrawalAmount)
            .balanceAfter(balanceAfter)
            .branch(branch)
            .build();
    }
}
