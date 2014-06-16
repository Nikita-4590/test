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
	
	int count (@Param("roles") String[] roles);
	
	List<RelationRequest> paging(@Param("page") Page page,
			@Param("sort") String sort, 
			@Param("direction") String direction,
			@Param("roles") String[] roles,@Param("priority") String priority);
	
	// get Request detail information
	RelationRequest get(@Param("request_id") int requestId, @Param("pgcrypto") String pgcrypto);
	
	
	/*
	 * don't change
	 * was called when search
	 */
	int getCountSearch(@Param("status") String status, @Param("searchParam") String searchParam, @Param("noneStatus") String[] noneStatus);
	List<RelationRequest> getAllRecord(@Param("page") Page page,
			@Param("sort") String sort, 
			@Param("direction") String direction,
			@Param("searchParam") String searchParam,
			@Param("status") String status,@Param("priority") String priority,
			@Param("noneStatus") String[] noneStatus);
	void updateRequest(RelationRequest request);
	
	void updateOnlyDirectorOfRequest(RelationRequest request);
	
	void updateRequestToDestroy(RelationRequest request);
}	
