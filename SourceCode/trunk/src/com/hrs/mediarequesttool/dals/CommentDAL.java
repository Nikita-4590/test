package com.hrs.mediarequesttool.dals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.hrs.mediarequesttool.dals.DALFactory;
import com.hrs.mediarequesttool.dals.MediaDAL;
import com.hrs.mediarequesttool.dals.MediaLabelDAL;
import com.hrs.mediarequesttool.pojos.Media;
import com.hrs.mediarequesttool.pojos.MediaLabel;
import com.hrs.mediarequesttool.pojos.CommentProperty;
import com.hrs.mediarequesttool.mail.HistorySender;
import com.hrs.mediarequesttool.auth.AuthProvider;
import com.hrs.mediarequesttool.pojos.User;
import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.mappers.CommentMapper;
import com.hrs.mediarequesttool.pojos.Comment;
import com.hrs.mediarequesttool.pojos.RelationRequest;

@Service
public class CommentDAL extends AbstractDAL<CommentMapper> {

	private Gson gson;
	private static LinkedHashMap<String, String> REQUEST_MAP;
	
	static {
		REQUEST_MAP = new LinkedHashMap<String, String>();
		
		REQUEST_MAP = new LinkedHashMap<String, String>();
		REQUEST_MAP.put("relation_request_id", "依頼ID");
		REQUEST_MAP.put("company_name", "御社名");
		REQUEST_MAP.put("requester_name", "ご担当者名");
		REQUEST_MAP.put("requester_mail", "ご連絡先メールアドレス");
		REQUEST_MAP.put("requester_phone", "ご連絡先電話番号");
		REQUEST_MAP.put("media_name", "媒体名");
		REQUEST_MAP.put("url", "管理画面URL");
		REQUEST_MAP.put("login_id_1", "login_id_1");
		REQUEST_MAP.put("login_id_2", "login_id_2");
		REQUEST_MAP.put("crawl_date", "連携開始日時");
	}
	
	// restricted constructor
	CommentDAL() {
	}

	public CommentDAL(SqlSessionFactory sessionFactory) {
		super(sessionFactory, CommentMapper.class);
		
		gson = new Gson();
	}
	
	public void updateRequest(RelationRequest oldRequest, RelationRequest newRequest) throws GenericException {
		User user = AuthProvider.getUser();

		if (user == null) {
			throw new GenericException();
		}
		
		try {
			Comment comment = new Comment();
			
			comment.setRequest_id(newRequest.getRelation_request_id());
			
			comment.setUser_id(user.getId());
			comment.setOld_value(toJSON(oldRequest));
			comment.setNew_value(toJSON(newRequest));
			
			insertComment(comment, RelationRequest.class);
			
			
		} catch (NullPointerException e) {
			throw new GenericException(e, CommentDAL.class);
		} 
	}
	
	private void insertComment(Comment comment, Class<?> type) throws GenericException {
		try {
			openSession();
			mapper.insert(comment);
			
			Comment newComment = mapper.get(comment.getRequest_comment_id());

			if (newComment != null) {
				getProperties(newComment, type);

				HistorySender.execute(newComment);
			}
		} catch (Exception e) {
			throw new GenericException(e);
		} finally {
			closeSession();
		}
	}
	
	private void getProperties(Comment comment, Class<?> type) throws Exception {
		Map<String, CommentProperty> properties = new LinkedHashMap<String, CommentProperty>();

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
		
		if (object instanceof RelationRequest) {
			propertyMap = REQUEST_MAP;
			type = RelationRequest.class;
			try {
				RelationRequest request = RelationRequest.class.cast(object);
				MediaDAL mediaDAL = DALFactory.getDAL(MediaDAL.class, this.sessionFactory);
				Media media = mediaDAL.get(request.getMedia_id());
				MediaLabelDAL dal = DALFactory.getDAL(MediaLabelDAL.class, this.sessionFactory);
				MediaLabel label = dal.get(media.getMedia_id());

				propertyMap.put("login_id_1", label.getLogin_id_1());
				propertyMap.put("login_id_2", label.getLogin_id_2());
			} catch (Exception e) {
				throw new Exception(e);
			}
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
