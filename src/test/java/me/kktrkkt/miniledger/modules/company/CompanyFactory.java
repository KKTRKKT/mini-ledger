package me.kktrkkt.miniledger.modules.company;

import lombok.RequiredArgsConstructor;
import me.kktrkkt.miniledger.modules.customer.Customer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompanyFactory {

    private final CompanyRepository companyRepository;

    public Company createCompany(String name, Customer customer) {
        return companyRepository.save(Company.builder()
                .name(name)
                .customer(customer)
                .build());
    }
}
