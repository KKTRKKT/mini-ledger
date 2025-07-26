package me.kktrkkt.miniledger.modules.accounting;


import jakarta.persistence.*;
import lombok.*;
import me.kktrkkt.miniledger.infra.entity.BaseEntity;
import me.kktrkkt.miniledger.modules.bankhistory.BankHistory;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Accounting extends BaseEntity<Accounting> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    private String companyId;

    private String categoryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private BankHistory bankHistory;
}
