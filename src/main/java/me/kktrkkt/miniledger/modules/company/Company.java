package me.kktrkkt.miniledger.modules.company;

import jakarta.persistence.*;
import lombok.*;
import me.kktrkkt.miniledger.infra.entity.BaseEntity;
import me.kktrkkt.miniledger.infra.entity.CustomSequence;
import me.kktrkkt.miniledger.modules.company.category.Category;
import me.kktrkkt.miniledger.modules.customer.Customer;
import org.hibernate.annotations.Comment;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company extends BaseEntity<Company> {

    @Id
    @CustomSequence(prefix = "com_")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    @Comment("사업체명")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Customer customer;

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE)
    private Set<Category> categories = new HashSet<>();
}