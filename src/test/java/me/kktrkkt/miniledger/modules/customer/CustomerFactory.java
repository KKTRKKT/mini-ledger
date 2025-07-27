package me.kktrkkt.miniledger.modules.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerFactory {

    private final CustomerRepository customerRepository;

    public Customer createCustomer() {
        return customerRepository.save(Customer.builder().build());
    }
}
