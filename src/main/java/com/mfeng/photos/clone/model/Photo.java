package com.mfeng.photos.clone.model;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Model class for a photo.
 * @author Maggie
 *
 */
@Table("PHOTOS") // Map Photo class to Photos table. Table name all caps
public class Photo {
	
	@Id // Primary Key 
	private Long id;
	
	@NotEmpty // filename cannot be empty
	private String fileName; // Will convert camelCase name to file_name in table
	
	@JsonIgnore // Don't convert property into JSON
	private byte[] data; // Raw data 
	
	@NotEmpty
	private String contentType;
	
	public Photo() {}
	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFilename() {
		return fileName;
	}
	
	public void setFilename(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
}
