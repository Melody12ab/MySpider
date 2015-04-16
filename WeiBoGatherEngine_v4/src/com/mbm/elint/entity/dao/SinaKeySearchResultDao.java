package com.mbm.elint.entity.dao;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.mbm.elint.entity.DocInfo;

@Component
//public class SinaKeySearchResultDao extends HibernateDao<SinaKeySearchResult, Long>  {
/**
 * 将此处的修改完对doc表的操作，将doc和SearchResult表进行合并
 */
public class SinaKeySearchResultDao extends HibernateDao<DocInfo, Long>  {

}
