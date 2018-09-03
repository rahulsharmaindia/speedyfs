package org.speedyfs.dao;

import org.speedyfs.model.ThumbFile;
import org.springframework.stereotype.Repository;

@Repository
public class MediaThumbnailDAOImpl extends HibernateDao<ThumbFile, String> implements MediaThumbnailDAO {

}
