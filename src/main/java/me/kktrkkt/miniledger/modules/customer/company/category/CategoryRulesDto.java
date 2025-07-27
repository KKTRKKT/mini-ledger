package me.kktrkkt.miniledger.modules.customer.company.category;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRulesDto {

    private List<CompanyRule> companies;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyRule {
        private String company_id;
        private String company_name;
        private List<CategoryRule> categories;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryRule {
        private String category_id;
        private String category_name;
        private Set<String> keywords;
    }
}
