package me.kktrkkt.miniledger.modules.accounting;

import com.querydsl.core.annotations.QueryProjection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountingResponse(
    String categoryId,                 // 분류 ID
    String categoryName,               // 분류명
    LocalDateTime transactionDateTime, // 거래일시
    String description,                // 적요
    BigDecimal depositAmount,          // 입금액
    BigDecimal withdrawalAmount,       // 출금액
    BigDecimal balanceAfter,           // 거래후잔액
    String branch                      // 거래점
) {

    @QueryProjection
    public AccountingResponse(String categoryId, String categoryName, LocalDateTime transactionDateTime, String description, BigDecimal depositAmount, BigDecimal withdrawalAmount, BigDecimal balanceAfter, String branch) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.transactionDateTime = transactionDateTime;
        this.description = description;
        this.depositAmount = depositAmount;
        this.withdrawalAmount = withdrawalAmount;
        this.balanceAfter = balanceAfter;
        this.branch = branch;
    }
}
