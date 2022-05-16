package com.mfeng.photos.clone.repository;

import org.springframework.data.repository.CrudRepository;

import com.mfeng.photos.clone.model.Photo;

public interface PhotoRepository extends CrudRepository<Photo, Long>{

}
