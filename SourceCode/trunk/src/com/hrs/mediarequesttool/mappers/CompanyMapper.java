package com.hrs.mediarequesttool.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.hrs.mediarequesttool.common.Page;
import com.hrs.mediarequesttool.pojos.Company;

@MapperScan
public interface CompanyMapper {
	Company get(int id);

	List<Company> paging(@Param("page") Page page, @Param("sort") String sort,
			@Param("direction") String direction);

	List<Company> getAll(@Param("sort") String sort,
			@Param("direction") String direction,
			@Param("query_name") String queryName,
			@Param("query_id") String queryId);

	void insert(Company company);

	void delete(Company company);

	void update(Company company);

	int generateId();

	int count();
}
