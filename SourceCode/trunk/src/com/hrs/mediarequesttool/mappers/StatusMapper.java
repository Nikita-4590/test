package com.hrs.mediarequesttool.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import com.hrs.mediarequesttool.pojos.Status;
@MapperScan
public interface StatusMapper {
	List<Status> getAll(@Param("sql") String[] sql);
	
	List<Status> getListNextStatus(@Param("listNextStatus") String[] listNextStatus);
	
	Status getDescription(@Param("type") String statusType);
}
