package com.hrs.mediarequesttool.dals;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import com.hrs.mediarequesttool.common.PropertiesLoader;
import com.hrs.mediarequesttool.common.Page;
import com.hrs.mediarequesttool.common.PagingResult;
import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.mappers.RelationRequestMapper;
import com.hrs.mediarequesttool.pojos.RelationRequest;

public class RelationRequestDAL extends AbstractDAL<RelationRequestMapper> {

	public RelationRequestDAL() {
	}

	public RelationRequestDAL(SqlSessionFactory sessionFactory) {
		super(sessionFactory, RelationRequestMapper.class);
	}
	
	public PagingResult<RelationRequest> paging(int page, String direction,
			String sort, String searchParam, String[] role,String priority) throws GenericException {

		try {
			openSession();
			PagingResult<RelationRequest> result = new PagingResult<RelationRequest>();
			Page pagingSetting = new Page(page);
			int total = mapper.count(parseIdSearch(searchParam), parseTextSearch(searchParam), role);
			result.setPage(page, total, pagingSetting.getLimit());
			if (!result.isExceed() && total != 0) {
				List<RelationRequest> relationRequests = mapper.paging(
						pagingSetting, sort, direction, parseIdSearch(searchParam),
						parseTextSearch(searchParam), role, priority);
				result.setList(relationRequests);
			}
			return result;
			
		} catch (Exception e) {
			throw new GenericException(e, this.getClass());
		} finally {
			closeSession();
		}
	}

	public RelationRequest get(int requestId) throws GenericException {
		try {
			openSession();
			String pgcrypto = PropertiesLoader.instance.getPgcryptoPasswd();
			return mapper.get(requestId, pgcrypto);
		} catch (Exception e) {
			throw new GenericException(e);
		} finally {
			closeSession();
		}
	}
	
	public void updateRequest(RelationRequest request) throws GenericException {
		try {
			openSession();
			mapper.updateRequest(request);
		} catch (Exception e) {
			throw new GenericException(e);
		} finally {
			closeSession();
		}
	}
}
