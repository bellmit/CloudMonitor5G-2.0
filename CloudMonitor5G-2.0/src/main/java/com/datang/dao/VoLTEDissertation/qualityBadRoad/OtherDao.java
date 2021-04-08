/**
 * 
 */
package com.datang.dao.VoLTEDissertation.qualityBadRoad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.otherProblem.VolteQualityBadRoadOther;

/**
 * 
 * volte质量专题---volte语音质差---其他问题路段Dao
 * 
 * @author yinzhipeng
 * @date:2015年12月4日 上午10:58:40
 * @version
 */
@Repository
@SuppressWarnings("all")
public class OtherDao extends
		GenericHibernateDao<VolteQualityBadRoadOther, Long> {
	/**
	 * 根据测试日志的id集合获取其他问题路段
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteQualityBadRoadOther> queryOtherRoadsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteQualityBadRoadOther.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		criteria.add(Restrictions.eq("serviceType", "1,"));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取其他问题路段数量
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Integer queryOtherRoadNumByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return null;
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteQualityBadRoadOther.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		criteria.add(Restrictions.eq("serviceType", "1,"));
		Long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return null == total || 0 == total ? null : total.intValue();
	}

	/**
	 * 根据测试日志的id集合获取其他问题路段id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryOtherRoadIdsByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteQualityBadRoadOther.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		criteria.add(Restrictions.eq("serviceType", "1,"));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}
}
