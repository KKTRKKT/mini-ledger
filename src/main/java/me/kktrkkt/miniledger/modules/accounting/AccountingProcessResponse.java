package me.kktrkkt.miniledger.modules.accounting;

public record AccountingProcessResponse(
    Integer totalCount,   // 총 거래 건수
    Integer successCount, // 분류 성공한 거래 건수
    Integer failureCount // 분류 실패한 거래 건수
) { }
