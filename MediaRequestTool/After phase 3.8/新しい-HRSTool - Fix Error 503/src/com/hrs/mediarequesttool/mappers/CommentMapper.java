package com.hrs.mediarequesttool.mappers;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.hrs.mediarequesttool.pojos.Comment;

@MapperScan
public interface CommentMapper {

	void insert(Comment comment);
	
	Comment get(@Param("comment_id") int commentId);
}
