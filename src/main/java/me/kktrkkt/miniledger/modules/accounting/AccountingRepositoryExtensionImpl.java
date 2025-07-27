package me.kktrkkt.miniledger.modules.accounting;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static me.kktrkkt.miniledger.modules.accounting.QAccounting.accounting;
import static me.kktrkkt.miniledger.modules.bankhistory.QBankHistory.bankHistory;
import static me.kktrkkt.miniledger.modules.company.category.QCategory.category;
import static me.kktrkkt.miniledger.modules.company.QCompany.company;


@Transactional(readOnly = true)
public class AccountingRepositoryExtensionImpl extends QuerydslRepositorySupport implements AccountingRepositoryExtension {

    final JPAQueryFactory jpaQueryFactory;

    public AccountingRepositoryExtensionImpl(EntityManager em) {
        super(Accounting.class);
         jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<AccountingResponse> getRecords(String companyId) {
        return jpaQueryFactory
                .select(Projections.constructor(AccountingResponse.class,
                        category.id,
                        category.name.nullif("미분류"),
                        bankHistory.transactionDateTime,
                        bankHistory.description,
                        bankHistory.depositAmount,
                        bankHistory.withdrawalAmount,
                        bankHistory.balanceAfter,
                        bankHistory.branch
                ))
                .from(accounting)
                .innerJoin(accounting.bankHistory, bankHistory)
                .leftJoin(category).on(accounting.categoryId.eq(category.id))
                .leftJoin(company).on(accounting.companyId.eq(company.id))
                .where(accounting.companyId.eq(companyId))
                .orderBy(bankHistory.transactionDateTime.desc())
                .fetch();
    }
}
