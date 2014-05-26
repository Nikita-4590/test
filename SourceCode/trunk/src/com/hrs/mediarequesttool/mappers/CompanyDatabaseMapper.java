package com.hrs.mediarequesttool.mappers;

import org.apache.ibatis.annotations.Param;

import com.hrs.mediarequesttool.pojos.CompanyDatabase;

public interface CompanyDatabaseMapper {
	CompanyDatabase get(@Param("company_id") int companyId);
}
