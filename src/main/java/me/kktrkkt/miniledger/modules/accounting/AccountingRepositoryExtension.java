package me.kktrkkt.miniledger.modules.accounting;

import java.util.List;

public interface AccountingRepositoryExtension {

    List<AccountingResponse> getRecords(String companyId);
}
