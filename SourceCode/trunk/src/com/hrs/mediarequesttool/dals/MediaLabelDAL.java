package com.hrs.mediarequesttool.dals;

import org.apache.ibatis.session.SqlSessionFactory;

import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.mappers.MediaLabelMapper;
import com.hrs.mediarequesttool.pojos.MediaLabel;

public class MediaLabelDAL extends AbstractDAL<MediaLabelMapper> {
	// restricted constructor
	MediaLabelDAL() {
	}

	public MediaLabelDAL(SqlSessionFactory sessionFactory) {
		super(sessionFactory, MediaLabelMapper.class);
	}

	public MediaLabel get(String mediaID) throws GenericException {
		try {
			openSession();
			return mapper.get(mediaID);
		} catch (Exception e) {
			throw new GenericException(e, MediaLabelDAL.class);
		} finally {
			closeSession();
		}
	}
}
