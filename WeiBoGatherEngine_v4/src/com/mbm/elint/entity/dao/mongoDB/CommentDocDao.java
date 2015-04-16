package com.mbm.elint.entity.dao.mongoDB;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.mongoDB.CommentDoc;

@Component
public class CommentDocDao implements MongoRepository<CommentDoc,Long> {
	@Autowired
	private MongoTemplate mongoTemplate;

	public List<CommentDoc> findAll() {
		return null;
	}

	public List<CommentDoc> findAll(Sort sort) {
		return null;
	}

	public <S extends CommentDoc> List<S> save(Iterable<S> entites) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<CommentDoc> findAll(Pageable arg0) {
		return null;
	}

	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void delete(Long arg0) {
		// TODO Auto-generated method stub

	}

	public void delete(CommentDoc arg0) {
		// TODO Auto-generated method stub

	}

	public void delete(Iterable<? extends CommentDoc> arg0) {
		// TODO Auto-generated method stub

	}

	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	public boolean exists(Long arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public Iterable<CommentDoc> findAll(Iterable<Long> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public CommentDoc findOne(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public <S extends CommentDoc> S save(S arg0) {
		this.mongoTemplate.save(arg0);
		return null;
	}
}
