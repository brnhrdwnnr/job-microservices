package com.bernhard.companyms.company;


import java.util.List;

public interface CompanyService {
    List<Company> getAllCompanies();
    void createCompany(Company Company);
    Company getCompanyById(Long id);
    boolean deleteCompanyById(Long id);
    boolean updateCompany(Long id, Company company);
}
