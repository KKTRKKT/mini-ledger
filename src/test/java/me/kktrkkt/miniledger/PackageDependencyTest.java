package me.kktrkkt.miniledger;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packagesOf = MiniLedgerApplication.class)
public class PackageDependencyTest {

    private static final String API_WEB = "..api.web..";
    private static final String MODULES = "..modules..";

    private static final String CUSTOMER = "..modules.customer..";
    private static final String BANK_HISTORY = "..modules.bankhistory..";
    private static final String ACCOUNTING = "..modules.accounting..";

    @ArchTest
    private ArchRule apiWebPackageRule = classes().that().resideInAnyPackage(API_WEB)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(API_WEB);

    @ArchTest
    private ArchRule modulesPackageRule = classes().that().resideInAnyPackage(MODULES)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(MODULES, API_WEB);

    @ArchTest
    private ArchRule customerPackageRule = classes().that().resideInAnyPackage(CUSTOMER)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(CUSTOMER, BANK_HISTORY, ACCOUNTING, API_WEB);

    @ArchTest
    private ArchRule bankHistoryPackageRule = classes().that().resideInAnyPackage(BANK_HISTORY)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(BANK_HISTORY, ACCOUNTING, API_WEB);

    @ArchTest
    private ArchRule accountingPackageRule = classes().that().resideInAnyPackage(ACCOUNTING)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(ACCOUNTING, CUSTOMER, BANK_HISTORY, API_WEB);

    @ArchTest
    private ArchRule freeOfCycles = slices().matching("me.kktrkkt.miniledger.modules.(*)..")
            .should().beFreeOfCycles();
}
