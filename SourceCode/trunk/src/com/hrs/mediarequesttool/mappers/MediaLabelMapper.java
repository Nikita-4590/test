package com.hrs.mediarequesttool.mappers;

import org.apache.ibatis.annotations.Param;

import com.hrs.mediarequesttool.pojos.MediaLabel;

public interface MediaLabelMapper {
	MediaLabel get(@Param("media_id") String mediaID);
}
