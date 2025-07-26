package me.kktrkkt.miniledger.modules.company.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.kktrkkt.miniledger.modules.company.Company;
import me.kktrkkt.miniledger.modules.company.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final ObjectMapper objectMapper;
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;

    // 1. rulesFile이 json이고 빈값이 아닌지 확인
    // 2. CategoryRulesDto 객체로 변환
    // 3. CategoryRulesDto 객체를 Category 엔티티로 변환
    // 4. Category 엔티티를 DB에 저장
    // 5. 저장된 Category 엔티티 리스트 반환
    @Transactional
    public List<Category> save(MultipartFile rulesFile) {
        if (rulesFile == null || rulesFile.isEmpty()) {
            throw new IllegalArgumentException("rulesFile이 비어 있습니다.");
        }

        try {
            // 1. JSON -> Dto 변환
            CategoryRulesDto rulesDto = objectMapper.readValue(rulesFile.getInputStream(), CategoryRulesDto.class);
            List<Category> allCategories = new ArrayList<>();

            // 2. Dto -> Entity 변환
            for (CategoryRulesDto.CompanyRule companyRule : rulesDto.getCompanies()) {
                Company company = companyRepository.findById(companyRule.getCompany_id())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "고객사를 찾을 수 없습니다: " + companyRule.getCompany_id()));

                for (CategoryRulesDto.CategoryRule categoryRule : companyRule.getCategories()) {
                    Category category = Category.builder()
                            .id(categoryRule.getCategory_id())
                            .name(categoryRule.getCategory_name())
                            .keywords(categoryRule.getKeywords())
                            .company(company)
                            .build();
                    allCategories.add(category);
                }
            }

            // 3. DB 저장
            return categoryRepository.saveAll(allCategories);

        } catch (IOException e) {
            throw new RuntimeException("rulesFile 파싱 중 오류 발생", e);
        }
    }

}
