package me.kktrkkt.miniledger.modules.accounting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountingRepository extends JpaRepository<Accounting, Long>, AccountingRepositoryExtension {
}
