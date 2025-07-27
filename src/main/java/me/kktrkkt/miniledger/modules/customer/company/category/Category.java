package me.kktrkkt.miniledger.modules.customer.company.category;

import jakarta.persistence.*;
import lombok.*;
import me.kktrkkt.miniledger.infra.entity.BaseEntity;
import me.kktrkkt.miniledger.modules.customer.company.Company;
import org.hibernate.annotations.Comment;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Category extends BaseEntity<Category> {

    @Id
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    @Comment("분류명")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Company company;

    @Builder.Default
    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn)
    private Set<String> keywords = new HashSet<>();
}
