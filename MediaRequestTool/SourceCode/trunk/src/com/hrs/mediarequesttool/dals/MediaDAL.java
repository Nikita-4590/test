package com.hrs.mediarequesttool.dals;

import org.apache.ibatis.session.SqlSessionFactory;
import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.mappers.MediaMapper;
import com.hrs.mediarequesttool.pojos.Media;

public class MediaDAL extends AbstractDAL<MediaMapper> {
	// restricted constructor
	MediaDAL() {
	}

	public MediaDAL(SqlSessionFactory sessionFactory) {
		super(sessionFactory, MediaMapper.class);
	}

	public Media getByMediaId(String mediaId) throws GenericException {
		try {
			openSession();
			return mapper.getByMediaId(mediaId);
		} catch (Exception e) {
			throw new GenericException(e, MediaDAL.class);
		} finally {
			closeSession();
		}
	}

	public Media get(int id) throws GenericException {
		try {
			openSession();
			return mapper.get(id);
		} catch (Exception e) {
			throw new GenericException(e, MediaDAL.class);
		} finally {
			closeSession();
		}
	}
}
