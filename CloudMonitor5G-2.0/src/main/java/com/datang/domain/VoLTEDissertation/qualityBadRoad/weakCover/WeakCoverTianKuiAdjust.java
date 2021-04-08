/**
 * 
 */
package com.datang.domain.VoLTEDissertation.qualityBadRoad.weakCover;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * 弱覆盖问题路段----天馈调整详情表
 * 
 * @author yinzhipeng
 * @date:2015年9月15日 上午9:48:20
 * @version
 */
@Entity
@Table(name = "IADS_QBR_WEAK_COVER_TK_ADJ")
public class WeakCoverTianKuiAdjust implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3981263108009669400L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题路段(弱覆盖问题路段)
	 */
	private VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * 调整优先级,天馈调整的优先级，从1开始（优先级最高）
	 */
	private Integer adjustRank;
	/**
	 * 和质差距离(m),该服务小区与各采样点问题路段的平均距离(采样点集合使用用户选择的当前问题路段)
	 */
	private Float distanceAvg;
	/**
	 * rsrp均值,问题路段中，该服务小区的RSRP均值
	 */
	private Float rsrpAvg;
	/**
	 * 工参下倾角
	 */
	private Float downdipAngle;
	/**
	 * 建议调整下倾角
	 */
	private Float adviceDowndipAngle;

	/**
	 * 该服务小区的采样点(RSRP):
	 * 采样点数量占整个语音质差路段采样点比例(%),该服务小区的采样点/总的服务小区采样点*100%,采样点集合使用用户选择的当前问题路段
	 */
	// private Float rsrpNum;
	/**
	 * 采样点数量占整个语音质差路段采样点比例(%)
	 */
	private Float sampleRatio;

	/**
	 * 工参方位角
	 */
	private Float azimuth;

	/**
	 * @return the idid
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the volteQualityBadRoadWeakCovervolteQualityBadRoadWeakCover
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public VolteQualityBadRoadWeakCover getVolteQualityBadRoadWeakCover() {
		return volteQualityBadRoadWeakCover;
	}

	/**
	 * @param volteQualityBadRoadWeakCover
	 *            the volteQualityBadRoadWeakCover to set
	 */
	public void setVolteQualityBadRoadWeakCover(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover) {
		this.volteQualityBadRoadWeakCover = volteQualityBadRoadWeakCover;
	}

	/**
	 * @return the cellNamecellName
	 */
	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	/**
	 * @param cellName
	 *            the cellName to set
	 */
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	/**
	 * @return the cellIdcellId
	 */
	@Column(name = "CELL_ID")
	public Long getCellId() {
		return cellId;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}

	/**
	 * @return the adjustRankadjustRank
	 */
	@Column(name = "ADJUST_RANK")
	public Integer getAdjustRank() {
		return adjustRank;
	}

	/**
	 * @param adjustRank
	 *            the adjustRank to set
	 */
	public void setAdjustRank(Integer adjustRank) {
		this.adjustRank = adjustRank;
	}

	/**
	 * @return the distanceAvgdistanceAvg
	 */
	@Column(name = "DISTANCE_AVG")
	public Float getDistanceAvg() {
		return distanceAvg;
	}

	/**
	 * @param distanceAvg
	 *            the distanceAvg to set
	 */
	public void setDistanceAvg(Float distanceAvg) {
		this.distanceAvg = distanceAvg;
	}

	/**
	 * @return the rsrpAvgrsrpAvg
	 */
	@Column(name = "RSRP_AVG")
	public Float getRsrpAvg() {
		return rsrpAvg;
	}

	/**
	 * @param rsrpAvg
	 *            the rsrpAvg to set
	 */
	public void setRsrpAvg(Float rsrpAvg) {
		this.rsrpAvg = rsrpAvg;
	}

	/**
	 * @return the downdipAngledowndipAngle
	 */
	@Column(name = "DOWNDIP_ANGLE")
	public Float getDowndipAngle() {
		return downdipAngle;
	}

	/**
	 * @param downdipAngle
	 *            the downdipAngle to set
	 */
	public void setDowndipAngle(Float downdipAngle) {
		this.downdipAngle = downdipAngle;
	}

	/**
	 * @return the adviceDowndipAngleadviceDowndipAngle
	 */
	// @Column(name = "ADVICE_DOWNDIP_ANGLE")
	@Transient
	public Float getAdviceDowndipAngle() {
		return adviceDowndipAngle;
	}

	/**
	 * @param adviceDowndipAngle
	 *            the adviceDowndipAngle to set
	 */
	public void setAdviceDowndipAngle(Float adviceDowndipAngle) {
		this.adviceDowndipAngle = adviceDowndipAngle;
	}

	/**
	 * @return the sampleRatiosampleRatio
	 */
	// @Column(name = "SAMPLE_RATIO")
	@Transient
	public Float getSampleRatio() {
		return sampleRatio;
	}

	/**
	 * @param sampleRatio
	 *            the sampleRatio to set
	 */
	public void setSampleRatio(Float sampleRatio) {
		this.sampleRatio = sampleRatio;
	}

	/**
	 * @return the azimuthazimuth
	 */
	@Column(name = "AZIMUTH")
	public Float getAzimuth() {
		return azimuth;
	}

	/**
	 * @param azimuth
	 *            the azimuth to set
	 */
	public void setAzimuth(Float azimuth) {
		this.azimuth = azimuth;
	}

	/**
	 * @return the rsrpNumrsrpNum
	 */
	// @Column(name = "RSRP_NUM")
	// public Float getRsrpNum() {
	// return rsrpNum;
	// }

	/**
	 * @param rsrpNum
	 *            the rsrpNum to set
	 */
	// public void setRsrpNum(Float rsrpNum) {
	// this.rsrpNum = rsrpNum;
	// }

}
