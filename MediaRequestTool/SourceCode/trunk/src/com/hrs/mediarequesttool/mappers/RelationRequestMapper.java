package com.hrs.mediarequesttool.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.hrs.mediarequesttool.common.Page;
//import com.hrs.mediarequest.common.Page;
import com.hrs.mediarequesttool.pojos.RelationRequest;
@MapperScan
public interface RelationRequestMapper {
	List<RelationRequest> getAll(@Param("requestId") String direction,
			@Param("companyParam") String companyParam,
			@Param("status") String status);
	
	int count ( @Param("requestId") String requestId,
			@Param("companyParam") String companyParam, 
			@Param("status") String status,
			@Param("mediaParam") String mediaParam,
			@Param("sql") String[] sql);
	
	List<RelationRequest> paging(@Param("page") Page page,
			@Param("sort") String sort, 
			@Param("direction") String direction,
			@Param("requestId") String requestId,
			@Param("status") String status,
			@Param("companyParam") String companyParam,
			@Param("mediaParam") String mediaParam,
			@Param("sql") String[] sql);
	
	// get Request detail information
	RelationRequest get(@Param("request_id") int requestId, @Param("pgcrypto") String pgcrypto);
	
	void updateRequest(RelationRequest request);
}	
