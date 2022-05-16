package com.mfeng.photos.clone.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mfeng.photos.clone.controller.PhotosController;

@Component // Automatically create class on app start
public class PhotoModelAssembler implements RepresentationModelAssembler<Photo, EntityModel<Photo>>{
	
	/**
	 * Convert a Photo object to EntityModel<Photo>
	 */
	@Override
	public EntityModel<Photo> toModel(Photo photo) {
		return EntityModel.of(photo, 
				linkTo(methodOn(PhotosController.class).getPhotoById(photo.getId())).withSelfRel(),
				linkTo(methodOn(PhotosController.class).getAllPhotos()).withRel("photos"),
				linkTo(methodOn(PhotosController.class).downloadPhotoById(photo.getId())).withRel("download"));
		
	}
}
