package com.recursivechaos.rcbot.plugins.persistence.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;

import com.recursivechaos.rcbot.plugins.stoopsnoop.objects.DictWord;
import com.recursivechaos.rcbot.plugins.stoopsnoop.query.DictWordDAO;

public class DictWordDAOImpl extends DAO implements DictWordDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<DictWord> getIgnoredWordList() {
		Criteria c = getSession().createCriteria(DictWord.class);
		return (List<DictWord>) c.list();
	}

}
