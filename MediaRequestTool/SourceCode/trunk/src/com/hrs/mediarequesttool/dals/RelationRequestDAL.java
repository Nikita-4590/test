package com.hrs.mediarequesttool.dals;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;

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
	
	public PagingResult<RelationRequest> paging(int page,String direction,String sort, String requestId, String status, String companyParam,String 
			mediaParam, String[] role) throws GenericException {		
		
		try {
			openSession();			
			PagingResult<RelationRequest> result = new PagingResult<RelationRequest>();			
			Page pagingSetting = new Page(page);
			int total = mapper.count(parse(requestId), parse(companyParam), parse(status),parse(mediaParam),role);
			result.setPage(page, total, pagingSetting.getLimit());
			if (!result.isExceed() && total != 0) {
				List<RelationRequest> _paging = mapper.paging(pagingSetting,sort,direction,parse(requestId),parse(status),parse(companyParam),parse(mediaParam),role);
				result.setList(_paging);
			}
			return result;
			
		} catch (Exception e) {
			throw new GenericException(e, this.getClass());
		} finally {
			closeSession();
		}
	}
	
	public List<RelationRequest> getAll(String requestId,String status,String companyParam) throws GenericException {		
		
		try {
			openSession();
			return mapper.getAll(parse(requestId),parse(companyParam),parse(status));
		} catch (Exception e) {
			throw new GenericException(e,this.getClass());
		} finally {
			closeSession();
		}		
	}
	
}
