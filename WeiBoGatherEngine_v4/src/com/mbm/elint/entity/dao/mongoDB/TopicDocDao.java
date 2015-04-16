package com.mbm.elint.entity.dao.mongoDB;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.mongoDB.TopicDoc;

@Component
public class TopicDocDao implements MongoRepository<TopicDoc, Long> {
	@Autowired
	private MongoTemplate mongoTemplate;

	public List<TopicDoc> findAll() {
		return null;
	}

	public List<TopicDoc> findAll(Sort sort) {
		return null;
	}

	public <S extends TopicDoc> List<S> save(Iterable<S> entites) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<TopicDoc> findAll(Pageable arg0) {
		return null;
	}

	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void delete(Long arg0) {
		// TODO Auto-generated method stub

	}

	public void delete(TopicDoc arg0) {
		// TODO Auto-generated method stub

	}

	public void delete(Iterable<? extends TopicDoc> arg0) {
		// TODO Auto-generated method stub

	}

	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	public boolean exists(Long arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public Iterable<TopicDoc> findAll(Iterable<Long> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public TopicDoc findOne(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public <S extends TopicDoc> S save(S arg0) {
		this.mongoTemplate.save(arg0);
		return null;
	}

	public List<TopicDoc> findTopN(Pageable page) {
		Query query = new Query();
		query.with(page);
		List<TopicDoc> list = this.mongoTemplate.find(query, TopicDoc.class);
		return list;
	}

}
