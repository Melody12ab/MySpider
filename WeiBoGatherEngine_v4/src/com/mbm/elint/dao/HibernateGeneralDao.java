package com.mbm.elint.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

@SuppressWarnings("unchecked")
@Component
@Transactional
public class HibernateGeneralDao<T> extends HibernateDaoSupport  {
	public void delete(String entityName, Serializable id) throws Exception {
		String hql = "DELETE FROM " + entityName + " WHERE id=?";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, id);
		query.executeUpdate();
	}

	public boolean isUnique(Class<T> entityClass, Object entity,
			String uniquePropertyNames) throws Exception {
		Assert.hasText(uniquePropertyNames);
		Criteria criteria = getSession().createCriteria(entityClass);
		criteria.setProjection(Projections.rowCount());
		String[] nameList = uniquePropertyNames.split(",");
		try {
			// 循环加入唯一列
			for (String name : nameList) {
				criteria.add(Restrictions.eq(name, PropertyUtils.getProperty(
						entity, name)));
			}
			// 以下代码为了如果是update的情况,排除entity自身.
			String idName = getIdName(entityClass);
			// 取得entity的主键值
			Serializable id = getId(entityClass, entity);
			// 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
			if (id != null)
				criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		boolean flag = (Integer) criteria.uniqueResult() == 0;
		return flag;
	}

	public T findByPropertyUnique(Class<T> entityClass, String propertyName,
			Object propertyValue) throws Exception {
		Criteria criteria = getSession().createCriteria(entityClass);
		criteria.add(Restrictions.eq(propertyName, propertyValue));
		return (T) criteria.uniqueResult();
	}

	public List<T> find(String hql){
		Session session = this.getSessionFactory().openSession(); 
		Query query = session.createQuery(hql);
		List all = query.list() ;
		session.close() ;
		return all ;
	}
	
	public List<T> find(String hql, int startIndex, int pageSize)
			throws Exception {
		// TODO Auto-generated method stub
		Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		Query query = session.createQuery(hql);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List all = query.list() ;
		session.close() ;
		return all ;
	}

	public List<T> find(String hql, int startIndex, int pageSize,
			Object... values) throws Exception {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		Query query = session.createQuery(hql);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		if (values != null) {
			// 向HQL中的占位符注入参数
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		List all = query.list() ;
		session.close() ;
		return all ;
	}

	public List executeSQL(String sql) throws Exception {
		Assert.hasText(sql);
		Session session = this.getSessionFactory().openSession() ;
		Query query = session.createSQLQuery(sql);
		List all = query.list();
		session.close() ;
		return all;
	}

	public void executeSQLVoid(String sql) throws Exception {
		Assert.hasText(sql);
		Session session = this.getSessionFactory().openSession() ;
		session.createSQLQuery(sql).executeUpdate();
		session.close() ;
	}


	public List findByCriterion(Class<T> entityClass, int startIndex,
			int pageSize, Criterion... criterions) throws Exception {
		// TODO Auto-generated method stub
		Criteria criteria = this.getHibernateTemplate().getSessionFactory().openSession().createCriteria(entityClass);
		criteria.setFirstResult(startIndex).setMaxResults(pageSize);
		if (criterions != null) {
			for (Criterion criterion : criterions) {
				criteria.add(criterion);
			}
		}
		return criteria.list();
	}

	public List findByProperty(Class<T> entityClass, String propertyName,
			Object propertyValue) throws Exception {
		Criteria criteria = this.getHibernateTemplate().getSessionFactory().openSession().createCriteria(entityClass);
		criteria.add(Restrictions.eq(propertyName, propertyValue));
		return criteria.list();
	}

	public List findByProperty(Class<T> entityClass, String propertyName,
			Object propertyValue, boolean isAsc, String orderPropertyName)
			throws Exception {
		Criteria criteria = this.getHibernateTemplate().getSessionFactory().openSession().createCriteria(entityClass);
		criteria.add(Restrictions.eq(propertyName, propertyValue));
		if (isAsc) {
			criteria.addOrder(Order.asc(orderPropertyName));
		} else {
			criteria.addOrder(Order.desc(orderPropertyName));
		}
		return criteria.list();
	}

	public Serializable getId(Class<T> entityClass, Object entity)
			throws Exception {
		Assert.notNull(entity);
		Assert.notNull(entityClass);
		return (Serializable) PropertyUtils.getProperty(entity,
				getIdName(entityClass));
	}

	public String getIdName(Class<T> clazz) throws Exception {
		Assert.notNull(clazz);
		ClassMetadata meta = getSessionFactory().getClassMetadata(clazz);
		Assert.notNull(meta, "Class " + clazz
				+ " not define in hibernate session factory.");
		String idName = meta.getIdentifierPropertyName();
		Assert.hasText(idName, clazz.getSimpleName()
				+ " has no identifier property define.");
		return idName;
	}

	public Long getSequenceValue(String sequenceName) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select " + sequenceName + ".nextval as num from dual";
		Long sequenceValue = (Long) this.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql).addScalar(
				"num", Hibernate.LONG).uniqueResult();
		return sequenceValue;
	}
	
	public void save(Object entity){
		Session session = this.getHibernateTemplate().getSessionFactory().openSession() ;
		session.save(entity) ;
		session.clear() ;
		session.close() ;
	}
	
	
	public void update(Object entity){
		this.getHibernateTemplate().update(entity) ;
	}

	public Long count(String beanName) throws Exception {
		// TODO Auto-generated method stub
		String hql = "select count(*) from " + beanName;
		Long total = (Long) getSession().createQuery(hql).list().get(0);
		return total;
	}

	public Long count(String beanName, String conditions) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveAll(Collection list) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().saveOrUpdateAll(list) ;
	}

}
