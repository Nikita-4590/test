package com.hrs.mediarequesttool.pojos;

public class Media {
	private int id;
	private String media_id;
	private String media_name;
	private int media_kbn;
	private String description;
	private String table_name;
	private String table_columns;
	private String table_create_sql;
	private String entry_media_arr;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getMedia_name() {
		return media_name;
	}

	public void setMedia_name(String media_name) {
		this.media_name = media_name;
	}

	public int getMedia_kbn() {
		return media_kbn;
	}

	public void setMedia_kbn(int media_kbn) {
		this.media_kbn = media_kbn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTable_name() {
		return table_name;
	}

	public String getTable_columns() {
		return table_columns;
	}

	public String getTable_create_sql() {
		return table_create_sql;
	}

	public String getEntry_media_arr() {
		return entry_media_arr;
	}
}
