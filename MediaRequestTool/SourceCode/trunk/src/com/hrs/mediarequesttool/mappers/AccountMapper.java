package com.hrs.mediarequesttool.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.hrs.mediarequesttool.common.Page;
import com.hrs.mediarequesttool.pojos.Account;

@MapperScan
public interface AccountMapper {
	List<Account> paging(@Param("company_id") int companyId,
			@Param("page") Page page, @Param("sort") String sort,
			@Param("direction") String direction,
			@Param("media_name") String mediaName,
			@Param("login_id") String loginId,
			@Param("shop_name") String shopName,
			@Param("status") Boolean status);

	int count(@Param("company_id") int companyId,
			@Param("media_name") String mediaName,
			@Param("login_id") String loginId,
			@Param("shop_name") String shopName,
			@Param("status") Boolean status);

	List<Account> getAll(@Param("company_id") int companyId,
			@Param("sort") String sort, @Param("direction") String direction,
			@Param("media_name") String mediaName,
			@Param("login_id") String loginId,
			@Param("shop_name") String shopName);
}
