package me.kktrkkt.miniledger.modules.accounting;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AccountingRuleDto(
        @JsonProperty("companies")
        List<CompanyRuleDto> companies
) {

    public record CompanyRuleDto(
            @JsonProperty("company_id")
            String companyId,

            @JsonProperty("company_name")
            String companyName,

            @JsonProperty("categories")
            List<CategoryRuleDto> categories
    ) {}

    public record CategoryRuleDto(
            @JsonProperty("category_id")
            String categoryId,

            @JsonProperty("category_name")
            String categoryName,

            @JsonProperty("keywords")
            List<String> keywords
    ) {}
}