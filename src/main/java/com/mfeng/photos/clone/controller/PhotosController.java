package com.mfeng.photos.clone.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.mfeng.photos.clone.model.Photo;
import com.mfeng.photos.clone.model.PhotoModelAssembler;
import com.mfeng.photos.clone.service.PhotoService;

/* Each method of REST controller returns some data instead of 
 * HTML website 
 */
@RestController // Specify as REST controller for HTTP request handlings
@RequestMapping("/api")
public class PhotosController {
	
	/* Dependency injection of Service class
	 * to PhotosController class
	 */
	private final PhotoService photoService;
	
	private final PhotoModelAssembler photoAssembler;
	
	public PhotosController(PhotoService photoService, PhotoModelAssembler photoAssembler) {
		this.photoService = photoService;
		this.photoAssembler = photoAssembler;
	}
	
	@GetMapping("/photos")
	public CollectionModel<EntityModel<Photo>> getAllPhotos() {
		// Automatically will convert returned value into JSON format
		// Convert Iterable to List
		Iterable<Photo> iterable = photoService.getAllPhotos();
		List<Photo> list = new ArrayList<>();
		iterable.forEach(list::add);
		/* Convert List<Photo> to List<EntityModel<Photo>> via stream functions
		 * Add links to each photo
		 */
		List<EntityModel<Photo>> photos = list.stream().map(photoAssembler::toModel).collect(Collectors.toList());
		CollectionModel<EntityModel<Photo>> photosModel = CollectionModel.of(photos,
				linkTo(methodOn(PhotosController.class).getAllPhotos()).withSelfRel());
		return photosModel;
	}
	
	// PathVariable for replacing the parameter value into the url
	@GetMapping("/photos/{id}")
	public EntityModel<Photo> getPhotoById(@PathVariable Long id) {
		Photo photo = photoService.getPhoto(id);
		if (photo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		// Return data and links so client knows other parts of the API it can access
		return photoAssembler.toModel(photo);
	}
	
	@DeleteMapping("/photos/{id}") // Map HTTP DELETE request
	public ResponseEntity<EntityModel<Photo>> deletePhotoById(@PathVariable Long id) {
		photoService.removePhoto(id);
		// Return HTTP 204 no content response
		return ResponseEntity.noContent().build();
	}
	
	// curl -X POST -H  "Content-Type: multipart/form-data" -F "data=@filename_with_extension; type=image/jpeg" localhost:8080/photos
	// RequestBody to convert JSON into Photo object
	// Valid - validate JSON input 
	@PostMapping("/photos") // Map HTTP POST request
	public ResponseEntity<EntityModel<Photo>> createNewPhoto(@RequestPart("data") MultipartFile file) throws IOException {
		EntityModel<Photo> photoModel = photoAssembler.toModel(photoService
				.addPhoto(file.getOriginalFilename(), file.getContentType(), file.getBytes()));
		/* Return HTTP 201 Created response and 
		 * Location header is the photo's self link
		 */
		return ResponseEntity
				.created(photoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(photoModel);
	}
	
	// Download a photo
	@GetMapping("/photos/{id}/download")
	public ResponseEntity<byte[]> downloadPhotoById(@PathVariable Long id) {
		Photo photo = photoService.getPhoto(id);
		if (photo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		byte[] data = photo.getData();
		HttpHeaders headers = new HttpHeaders();
		// Describe the type of media being sent
		headers.setContentType(MediaType.valueOf(photo.getContentType()));
		/* Describe how content is displayed
		 * inline = in browser
		 * attachment = saved locally
		 */
		ContentDisposition contentDisposition = ContentDisposition
				.builder("inline")
				.filename(photo.getFilename())
				.build();
		headers.setContentDisposition(contentDisposition);
		
		return new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
	}
}
