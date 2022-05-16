package com.mfeng.photos.clone.service;

import org.springframework.stereotype.Service;

import com.mfeng.photos.clone.model.Photo;
import com.mfeng.photos.clone.repository.PhotoRepository;

/* Service class stores database
 * 
 */
@Service
public class PhotoService {
	
	private final PhotoRepository photoRepository;
	
	public PhotoService(PhotoRepository photoRepository) {
		this.photoRepository = photoRepository;
	}

	public Photo addPhoto(String filename, String contentType, byte[] data) {
		Photo photo = new Photo();
		photo.setFilename(filename); // Set file name from uploaded file
		photo.setContentType(contentType);
		photo.setData(data); // Set raw data of image file
		photoRepository.save(photo);
		return photo;
	}

	public void removePhoto(Long id) {
		photoRepository.deleteById(id);
	}

	public Photo getPhoto(Long id) {
		return photoRepository.findById(id).orElse(null);
	}

	public Iterable<Photo> getAllPhotos() {
		return photoRepository.findAll();
	}
	
	public Photo replacePhoto(Long id, String newFilename, String newContentType, byte[] data) {
		Photo photo = getPhoto(id);
		if (photo == null) {
			return addPhoto(newFilename, newContentType, data);
		} 
		
		photo.setData(data);
		photo.setContentType(newContentType);
		photo.setFilename(newFilename);
		photoRepository.save(photo);
		return photo;
	}
	
	
}
