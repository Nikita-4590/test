package com.hrs.mediarequesttool.mappers;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.hrs.mediarequesttool.pojos.User;

@MapperScan
public interface UserMapper {
	
	User getbyUserID (@Param("user_id") String userId);
	
	void countLoginFailure(@Param("id") int id);
	
	void updateLastLoginTime(@Param("id") int id);

}
