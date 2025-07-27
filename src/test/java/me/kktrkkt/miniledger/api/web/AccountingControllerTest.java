package me.kktrkkt.miniledger.api.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import infra.MockMvcTest;
import me.kktrkkt.miniledger.modules.company.Company;
import me.kktrkkt.miniledger.modules.company.CompanyFactory;
import me.kktrkkt.miniledger.modules.company.category.CategoryRulesDto;
import me.kktrkkt.miniledger.modules.customer.Customer;
import me.kktrkkt.miniledger.modules.customer.CustomerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
@Transactional
class AccountingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerFactory customerFactory;

    @Autowired
    private CompanyFactory companyFactory;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer cus1;
    private Company com1;
    private Company com2;

    @BeforeEach
    void beforeEach() {
        this.cus1 = customerFactory.createCustomer();
        this.com1 = companyFactory.createCompany("A 커머스", this.cus1);
        this.com2 = companyFactory.createCompany("B 커머스", this.cus1);
    }

    @DisplayName("요청 파일 없이 회계 처리 API 호출 시 4xx 클라이언트 오류 발생")
    @Test
    void processAccounting_nullInput_throwsException() throws Exception {
        this.mockMvc.perform(post(AccountingController.ACCOUNTING_PROCESS_API))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @DisplayName("잘못된 파일 형식으로 회계 처리 API 호출 시 4xx 클라이언트 오류 발생")
    @Test
    void processAccounting_invalidFileType_throwsException() throws Exception {
        this.mockMvc.perform(post(AccountingController.ACCOUNTING_PROCESS_API)
                        .param("customerId", "testCustomer")
                        .param("csvFile", "invalidFile.txt")
                        .param("rulesFile", "invalidRules.json"))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @DisplayName("정상적인 파일로 회계 처리 API 호출 시 200 OK 응답")
    @Test
    void processAccounting_validInput_returnsSuccess() throws Exception {
        MockMultipartFile csvFile = new MockMultipartFile(
                "csvFile",
                "bank_transactions.csv",
                "text/csv",
                getClass().getResourceAsStream("/bank_transactions.csv")
        );

        // 1. JSON 로드
        InputStream inputStream = getClass().getResourceAsStream("/rules.json");
        CategoryRulesDto rulesDto = objectMapper.readValue(inputStream, CategoryRulesDto.class);

        // 2. company_id 수정 (예: 첫 번째 회사만 변경)
        rulesDto.getCompanies().get(0).setCompany_id(com1.getId());
        rulesDto.getCompanies().get(1).setCompany_id(com2.getId());

        // 3. 다시 JSON 문자열로 직렬화
        byte[] modifiedJsonBytes = objectMapper.writeValueAsBytes(rulesDto);

        // 4. MockMultipartFile로 생성
        MockMultipartFile rulesFile = new MockMultipartFile(
                "rulesFile",
                "rules.json",
                "application/json",
                modifiedJsonBytes
        );

        this.mockMvc.perform(multipart(HttpMethod.POST, AccountingController.ACCOUNTING_PROCESS_API)
                        .file(csvFile)
                        .file(rulesFile)
                        .param("customerId", cus1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successCount").value(8))
                .andExpect(jsonPath("$.failureCount").value(1))
                .andExpect(jsonPath("$.totalCount").value(9))
                .andDo(print());
    }

    @DisplayName("회계 기록 조회 API 호출 시 200 OK 응답")
    @Test
    void getRecords_validCompanyId_returnsRecords() throws Exception {
        processAccounting_validInput_returnsSuccess();
        this.mockMvc.perform(get(AccountingController.ACCOUNTING_RECORDS_API)
                        .param("companyId", com1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5))
                .andDo(print());
        this.mockMvc.perform(get(AccountingController.ACCOUNTING_RECORDS_API)
                        .param("companyId", com2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andDo(print());
    }

    @DisplayName("회계 기록 조회 API 호출 시 잘못된 회사 ID로 4xx 클라이언트 오류 발생")
    @Test
    void getRecords_invalidCompanyId_throwsException() throws Exception {
        this.mockMvc.perform(get(AccountingController.ACCOUNTING_RECORDS_API)
                        .param("companyId", "invalidCompany"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());
    }

    @DisplayName("회계 기록 조회 API 호출 시 회사 ID 누락으로 4xx 클라이언트 오류 발생")
    @Test
    void getRecords_missingCompanyId_throwsException() throws Exception {
        this.mockMvc.perform(get(AccountingController.ACCOUNTING_RECORDS_API))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}