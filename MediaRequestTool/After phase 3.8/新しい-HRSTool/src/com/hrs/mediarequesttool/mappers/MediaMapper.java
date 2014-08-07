package com.hrs.mediarequesttool.mappers;

import org.apache.ibatis.annotations.Param;
import com.hrs.mediarequesttool.pojos.Media;

public interface MediaMapper {
	Media get(@Param("id") int id);
	Media getByMediaId(@Param("media_id") String mediaId);
}
