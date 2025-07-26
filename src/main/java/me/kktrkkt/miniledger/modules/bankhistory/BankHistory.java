package me.kktrkkt.miniledger.modules.bankhistory;

import jakarta.persistence.*;
import lombok.*;
import me.kktrkkt.miniledger.infra.entity.BaseEntity;
import me.kktrkkt.miniledger.modules.customer.Customer;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankHistory extends BaseEntity<BankHistory> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Customer customer;

    @Column(nullable = false)
    @Comment("거래 일시")
    private LocalDateTime transactionDateTime;

    @Comment("적요")
    private String description;

    @Column(precision = 20, scale = 2)
    @Comment("입금액")
    private BigDecimal depositAmount;

    @Column(precision = 20, scale = 2)
    @Comment("출금액")
    private BigDecimal withdrawalAmount;

    @Column(name = "balance_after", precision = 20, scale = 2)
    @Comment("거래 후 잔액")
    private BigDecimal balanceAfter;

    @Comment("거래 지점")
    private String branch;
}