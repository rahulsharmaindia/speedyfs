package org.speedyfs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import org.speedyfs.model.FileData;

@Repository
public class TaskDaoImpl extends HibernateDao<FileData, String> implements TaskDao {
	@Override
	public void checkAndUpdateCreationDate() {
		Session session = currentSession();
		Query qry = session.createQuery("update ClientImageData c set c.creation_date=? where c.creation_date is null");
		qry.setParameter(0, String.valueOf(System.currentTimeMillis()));
		@SuppressWarnings("unused")
		int res = qry.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileData> getNullCreationDateData() {
		List<FileData> imageList = null;
		Criteria criteria = currentSession().createCriteria(FileData.class)
				.add(Restrictions.isNull("creation_date"));

		criteria.setCacheable(true);
		imageList = criteria.list();
		return imageList;
	}

	@Override
	public void updateData(List<FileData> imageList) {
		for (FileData imageData : imageList) {
			update(imageData);
		}
	}
}
