package com.hrs.mediarequesttool.dals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.hrs.mediarequesttool.pojos.CommentProperty;
import com.hrs.mediarequesttool.pojos.RelationRequest;
import com.hrs.mediarequesttool.pojos.RequestChangeInfo;
import com.hrs.mediarequesttool.mail.HistorySender;
import com.hrs.mediarequesttool.auth.AuthProvider;
import com.hrs.mediarequesttool.pojos.User;
import com.hrs.mediarequesttool.common.Constants;
import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.mappers.CommentMapper;
import com.hrs.mediarequesttool.pojos.Comment;

@Service
public class CommentDAL extends AbstractDAL<CommentMapper> {

	private Gson gson;
	private static LinkedHashMap<String, String> REQUEST_CHANGE_INFO;
	
	static {
		REQUEST_CHANGE_INFO = new LinkedHashMap<String, String>();
		REQUEST_CHANGE_INFO.put("status_description", "ステータス");
		REQUEST_CHANGE_INFO.put("renkei_date", "連携開始日");
		REQUEST_CHANGE_INFO.put("director_name", "担当ディレクター");
	}
	
	// restricted constructor
	CommentDAL() {
	}

	public CommentDAL(SqlSessionFactory sessionFactory) {
		super(sessionFactory, CommentMapper.class);
		
		gson = new Gson();
	}
	
	public void updateRequest(String reason, RequestChangeInfo oldInfo, RequestChangeInfo newInfo, RelationRequest newRequest) throws GenericException {
		try {
			Comment comment = new Comment();
			
			comment.setRequest_id(newRequest.getRelation_request_id());
			comment.setComment_reason(reason);
			comment.setOld_value(toJSON(oldInfo));
			comment.setNew_value(toJSON(newInfo));
			
			insertComment(comment, RequestChangeInfo.class, newRequest);
		} catch (NullPointerException e) {
			throw new GenericException(e, CommentDAL.class);
		} 
	}
	
	private void insertComment(Comment comment, Class<?> type, RelationRequest newRequest) throws GenericException {
		User user = AuthProvider.getUser();

		if (user == null) {
			throw new GenericException();
		}
		
		try {
			openSession();
			comment.setUser_id(user.getId());
			
			mapper.insert(comment);
			
			if (newRequest.getStatus().equals(Constants.STATUS_FINISHED)) {
				// send mail
				Comment newComment = mapper.get(comment.getRequest_comment_id());

				if (newComment != null) {
					getProperties(newComment, type);

					HistorySender.execute(newComment, newRequest);
				}
			}
		} catch (Exception e) {
			throw new GenericException(e);
		} finally {
			closeSession();
		}
	}
	
	private void getProperties(Comment comment, Class<?> type) throws Exception {
		Map<String, CommentProperty> properties = new LinkedHashMap<String, CommentProperty>();

		parseProperties(properties, parseJson(comment.getOld_value(), type), true);
		parseProperties(properties, parseJson(comment.getNew_value(), type), false);

		List<CommentProperty> propArr = new ArrayList<CommentProperty>();

		for (Map.Entry<String, CommentProperty> entry : properties.entrySet()) {
			propArr.add(entry.getValue());
		}

		comment.setProperties(propArr);
	}
	
	private void parseProperties(Map<String, CommentProperty> properties, Object object, boolean old) throws Exception {
		if (object == null) {
			return;
		}
		LinkedHashMap<String, String> propertyMap;
		Class<?> type;
		
		if (object instanceof RequestChangeInfo) {
			propertyMap = REQUEST_CHANGE_INFO;
			type = RequestChangeInfo.class;
		} else {
			throw new Exception();
		}
		
		for (Map.Entry<String, String> entry : propertyMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			Field field = null;

			try {
				field = type.getDeclaredField(key);
			} catch (NoSuchFieldException e) {
				if (field == null) {
					Class<?> superType = type.getSuperclass();
					// type = superType.asSubclass(type);
					field = superType.getDeclaredField(key);
				}
			}
			field.setAccessible(true);
			Object actualValue = field.get(object);
			
			if (!properties.containsKey(key)) {
				properties.put(key, new CommentProperty(value));
			}

			if (old) {
				properties.get(key).setOldValue(actualValue);
			} else {
				properties.get(key).setNewValue(actualValue);
			}
		}
	}
	
	private String toJSON(Object object) {
		return gson.toJson(object);
	}
	
	private <T> T parseJson(String json, Class<T> type) {
		if (json == null) {
			return null;
		}

		return gson.fromJson(json, type);
	}
}
