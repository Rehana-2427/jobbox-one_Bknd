package com.jobbox.Project_Jobbox.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jobbox.Project_Jobbox.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

	@Query("select c from Company c where c.companyName=?1")
	Company findByName(String companyName);

	@Query("select c from Company c where c.companyName=?1")
	Company findCompanyByName(String companyName);

	@Query("SELECT c FROM Company c Where c.companyStatus IS NULL")
	Page<Company> displayComapnies(PageRequest pageRequest);

	@Query("SELECT c FROM Company c Where c.companyStatus='Approved'")
	Page<Company> companiesList( PageRequest pageRequest);

	@Query("SELECT COUNT(DISTINCT c.companyId) FROM Company c WHERE c.companyStatus = 'Approved'")
	Integer countApprovedCompanies();

	@Query("SELECT c FROM Company c WHERE  c.companyName  LIKE %?1% AND c.companyStatus = 'Approved'")
	Page<Company> findCompanyBySearch(String search, PageRequest pageRequest);

	@Query("select c.companyName from Company c where c.companyId=?1")
	String getCompanyName(int companyId);

	@Query("SELECT COUNT(DISTINCT c.companyId) FROM Company  c")
	Integer getCountOfTotalCompany();

	@Query("SELECT COUNT(DISTINCT c.companyId) FROM Company c WHERE c.companyStatus IS NULL")
	Integer countOfCompanies();

	@Query("SELECT MONTH(c.actionDate) as month, COUNT(c.companyId) as companyCount " + "FROM Company c  "
			+ "WHERE c.companyStatus = 'Approved' " + "GROUP BY MONTH(c.actionDate)")
	List<Object[]> getCountValidateCompanyByEachMonth();

	@Query("SELECT c.logo, c.banner FROM Company c WHERE c.companyName = ?1")
	Object[] findLogoAndBannerByCompanyName(String companyName);

	@Query("SELECT c.logo FROM Company c WHERE c.logo IS NOT NULL")
	List<byte[]> findCompanyLogos();

	@Query("SELECT c.companyId FROM Company c WHERE c.logo =?1")
	int fetchCompanyIdByLogo(byte[] companyLogo);
	
	@Query("select c from Company c where c.websiteLink=?1")
	Company findCompany(String domain);


    @Query("SELECT DISTINCT c.companyType FROM Company c WHERE c.companyType IS NOT NULL")
    List<String> findDistinctCompanyTypes();

    @Query("SELECT DISTINCT c.industryService FROM Company c WHERE c.industryService IS NOT NULL ")
	List<String> findDistinctIndustryTypes();

    @Query("SELECT DISTINCT c.location FROM Company c WHERE c.location IS NOT NULL")
	List<String> findDistinctLocations();

    @Query("SELECT c FROM Company c WHERE " +
            "(COALESCE(:companyType, '') = '' OR c.companyType = :companyType) " +
            "AND (COALESCE(:industryType, '') = '' OR c.industryService = :industryType) " +
            "AND (COALESCE(:location, '') = '' OR c.location LIKE %:location%) " +
            "AND c.companyStatus = 'Approved'")
	Page<Company> findByFilters(String companyType, String industryType, String location, Pageable pageable);

    @Query("FROM Company c WHERE LOWER(c.companyName) LIKE LOWER(CONCAT('%', :companyName, '%')) AND c.companyStatus = 'Approved'")
	List<Company> findByNameContainingIgnoreCase(String companyName);

    boolean existsByCompanyName(String companyName);
}
