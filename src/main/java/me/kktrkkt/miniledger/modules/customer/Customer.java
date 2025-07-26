package me.kktrkkt.miniledger.modules.customer;

import jakarta.persistence.*;
import lombok.*;
import me.kktrkkt.miniledger.infra.entity.BaseEntity;
import me.kktrkkt.miniledger.infra.entity.CustomSequence;
import me.kktrkkt.miniledger.modules.company.Company;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Customer extends BaseEntity<Customer> {

    @Id
    @CustomSequence(prefix = "cus_")
    @Column(updatable = false, nullable = false)
    private String id;

    @Builder.Default
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private Set<Company> companies = new HashSet<>();
}