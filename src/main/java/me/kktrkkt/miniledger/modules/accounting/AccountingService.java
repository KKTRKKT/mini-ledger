package me.kktrkkt.miniledger.modules.accounting;

import lombok.RequiredArgsConstructor;
import me.kktrkkt.miniledger.modules.bankhistory.BankHistory;
import me.kktrkkt.miniledger.modules.bankhistory.BankHistoryService;
import me.kktrkkt.miniledger.modules.customer.company.category.Category;
import me.kktrkkt.miniledger.modules.customer.company.category.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountingService {

    private final AccountingRepository accountingRepository;
    private final BankHistoryService bankHistoryService;
    private final CategoryService categoryService;

    public List<AccountingResponse> getRecords(String companyId) {
        return accountingRepository.getRecords(companyId);
    }

    @Transactional
    public AccountingProcessResponse processAccounting(String customerId, MultipartFile csvFile, MultipartFile rulesFile) throws IOException {
        List<BankHistory> txs = bankHistoryService.save(customerId, csvFile);
        List<Category> categories = categoryService.save(rulesFile);
        List<Accounting> accountingList = txs.stream()
                .map(tx -> {
                    // 거래 내역의 적요에서 키워드 매칭
                    String description = tx.getDescription();
                    Category matchedCategory = categories.stream()
                            .filter(category -> category.getKeywords().stream()
                                    .anyMatch(description::contains))
                            .findFirst()
                            .orElse(null);
                    final Accounting.AccountingBuilder accountingBuilder =
                            Accounting.builder().bankHistory(tx);
                    return matchedCategory == null
                            ? accountingBuilder.build()
                            : accountingBuilder
                                    .categoryId(matchedCategory.getId())
                                    .companyId(matchedCategory.getCompany().getId())
                                    .build();
                })
                .collect(Collectors.toList());

        List<Accounting> saved = accountingRepository.saveAll(accountingList);

        return new AccountingProcessResponse(
                saved.size(),
                saved.stream().mapToInt(tx -> tx.getCategoryId() != null ? 1 : 0).sum(),
                saved.stream().mapToInt(tx -> tx.getCategoryId() == null ? 1 : 0).sum()
        );
    }
}
