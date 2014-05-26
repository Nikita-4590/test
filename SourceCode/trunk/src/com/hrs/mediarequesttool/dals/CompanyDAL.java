package com.hrs.mediarequesttool.dals;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import com.hrs.mediarequesttool.common.Page;
import com.hrs.mediarequesttool.common.PagingResult;
import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.mappers.CompanyMapper;
import com.hrs.mediarequesttool.pojos.Company;

@Service
public class CompanyDAL extends AbstractDAL<CompanyMapper> {

	// private CompanyMapper companyMapper;

	// restricted constructor
	CompanyDAL() {
	}

	public CompanyDAL(SqlSessionFactory sessionFactory) {
		super(sessionFactory, CompanyMapper.class);
	}

	public Company get(int companyId) throws GenericException {
		try {
			openSession();
			return mapper.get(companyId);
		} catch (Exception e) {
			throw new GenericException(e);
		} finally {
			closeSession();
		}
	}

	public List<Company> getAll(String sort, String direction, String query)
			throws GenericException {
		try {
			openSession();
			query = query == null ? "" : query;

			String queryStr = "%" + query + "%";
			queryStr = queryStr.replace(' ', '%');
			return mapper.getAll(sort, direction, queryStr, query);
		} catch (Exception e) {
			throw new GenericException(e, CompanyDAL.class);
		} finally {
			closeSession();
		}
	}

	public int insert(Company company) throws GenericException {
		try {
			openSession();
			// generating an ID
			int generateId = mapper.generateId();

			// assign ID to Company object
			company.setId(generateId);

			// insert Company object into database
			mapper.insert(company);

			// return generated ID
			return generateId;
		} catch (Exception e) {
			throw new GenericException(e);
		} finally {
			closeSession();
		}
	}

	public PagingResult<Company> paging(int page, String sort,
			String direction, String query) throws GenericException {
		try {
			openSession();
			PagingResult<Company> result = new PagingResult<Company>();

			Page pagingSetting = new Page(page);

			int totalRecord = mapper.count();

			result.setPage(page, totalRecord, pagingSetting.getLimit());

			if (!result.isExceed()) {
				result.setList(mapper.paging(new Page(page), sort, direction));
			}

			return result;
		} catch (Exception e) {
			throw new GenericException(e, this.getClass());
		} finally {
			closeSession();
		}
	}
}
