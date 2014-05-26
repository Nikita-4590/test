package com.hrs.mediarequesttool.dals;

import org.apache.ibatis.session.SqlSessionFactory;

import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.mappers.CompanyDatabaseMapper;
import com.hrs.mediarequesttool.pojos.CompanyDatabase;

public class CompanyDatabaseDAL extends AbstractDAL<CompanyDatabaseMapper> {
	// restricted constructor
	CompanyDatabaseDAL() {
	}

	public CompanyDatabaseDAL(SqlSessionFactory sessionFactory) {
		super(sessionFactory, CompanyDatabaseMapper.class);
	}

	public CompanyDatabase get(int companyId) throws GenericException {
		try {
			openSession();
			return mapper.get(companyId);
		} catch (Exception e) {
			throw new GenericException(e);
		} finally {
			closeSession();
		}
	}
}
