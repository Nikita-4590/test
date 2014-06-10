package com.hrs.mediarequesttool.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import com.hrs.mediarequesttool.pojos.Status;
@MapperScan
public interface StatusMapper {
	
	List<Status> getListNextStatus(@Param("listNextStatus") String[] listNextStatus);
	
	Status get(@Param("type") String statusType);
}
