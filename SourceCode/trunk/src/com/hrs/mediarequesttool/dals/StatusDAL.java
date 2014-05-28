package com.hrs.mediarequesttool.dals;

import java.util.*;

import org.apache.ibatis.session.SqlSessionFactory;

import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.mappers.StatusMapper;
import com.hrs.mediarequesttool.pojos.Status;

public class StatusDAL extends AbstractDAL<StatusMapper> {
	StatusDAL() {

	}

	public StatusDAL(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory, StatusMapper.class);
	}

	public List<Status> getAll(String[] roles) throws GenericException {
		try {
			openSession();
			return mapper.getAll(roles);
		} catch (Exception e) {
			throw new GenericException(e, this.getClass());
		} finally {
			closeSession();
		}
	}
}
