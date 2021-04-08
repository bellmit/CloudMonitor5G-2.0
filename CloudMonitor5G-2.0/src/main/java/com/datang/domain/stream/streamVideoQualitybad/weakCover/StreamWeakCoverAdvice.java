package com.datang.domain.stream.streamVideoQualitybad.weakCover;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

/**
 * 流媒体视频质差弱覆盖问题 ----优化建议调整天馈、功率
 * 
 * @explain
 * @name WeakCoverAdjustAntennaFeeder
 * @author shenyanwei
 * @date 2017年10月20日下午4:43:03
 */
@Entity
@Table(name = "STREAM_BQ_WC_ADVICE")
public class StreamWeakCoverAdvice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7706411237033491439L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题(弱覆盖)
	 */
	private StreamQualityBadWeakCover streamQualityBadWeakCover;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * PCI
	 */
	private Long pci;
	/**
	 * EARFCN
	 */
	private Long earfcn;
	/**
	 * RSRP
	 */
	private Float rsrp;
	/**
	 * 和问题采样点距离(m)
	 */
	private Float toProblemDotDistance;
	/**
	 * 工参站高（m）
	 */
	private Float projectHeight;
	/**
	 * 工参下倾角
	 */
	private Float projectDowndipAngle;
	/**
	 * 工参方向角
	 */
	private Float projectAzimuth;
	/**
	 * 持续时间
	 */
	private Long duration;

	/**
	 * @return the duration
	 */
	@Column(name = "DURATION")
	public Long getDuration() {
		return duration;
	}

	/**
	 * @param the
	 *            duration to set
	 */

	public void setDuration(Long duration) {
		this.duration = duration;
	}

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
	 * @return the streamQualityBadWeakCover
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public StreamQualityBadWeakCover getStreamQualityBadWeakCover() {
		return streamQualityBadWeakCover;
	}

	/**
	 * @param the
	 *            streamQualityBadWeakCover to set
	 */

	public void setStreamQualityBadWeakCover(
			StreamQualityBadWeakCover streamQualityBadWeakCover) {
		this.streamQualityBadWeakCover = streamQualityBadWeakCover;
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
	 * @return the pci
	 */
	@Column(name = "PCI")
	public Long getPci() {
		return pci;
	}

	/**
	 * @param the
	 *            pci to set
	 */

	public void setPci(Long pci) {
		this.pci = pci;
	}

	/**
	 * @return the earfcn
	 */
	@Column(name = "EARFCN")
	public Long getEarfcn() {
		return earfcn;
	}

	/**
	 * @param the
	 *            earfcn to set
	 */

	public void setEarfcn(Long earfcn) {
		this.earfcn = earfcn;
	}

	/**
	 * @return the rsrp
	 */
	@Column(name = "RSRP")
	public Float getRsrp() {
		return rsrp;
	}

	/**
	 * @param the
	 *            rsrp to set
	 */

	public void setRsrp(Float rsrp) {
		this.rsrp = rsrp;
	}

	/**
	 * @return the toProblemDotDistance
	 */
	@Column(name = "DISTANCE")
	public Float getToProblemDotDistance() {
		return toProblemDotDistance;
	}

	/**
	 * @param the
	 *            toProblemDotDistance to set
	 */

	public void setToProblemDotDistance(Float toProblemDotDistance) {
		this.toProblemDotDistance = toProblemDotDistance;
	}

	/**
	 * @return the projectHeight
	 */
	@Column(name = "HEIGHT")
	public Float getProjectHeight() {
		return projectHeight;
	}

	/**
	 * @param the
	 *            projectHeight to set
	 */

	public void setProjectHeight(Float projectHeight) {
		this.projectHeight = projectHeight;
	}

	/**
	 * @return the projectDowndipAngle
	 */
	@Column(name = "DOWNDIP_ANGLE")
	public Float getProjectDowndipAngle() {
		return projectDowndipAngle;
	}

	/**
	 * @param the
	 *            projectDowndipAngle to set
	 */

	public void setProjectDowndipAngle(Float projectDowndipAngle) {
		this.projectDowndipAngle = projectDowndipAngle;
	}

	/**
	 * @return the projectDirectionAngle
	 */
	@Column(name = "AZIMUTH")
	public Float getProjectAzimuth() {
		return projectAzimuth;
	}

	/**
	 * @param the
	 *            projectDirectionAngle to set
	 */

	public void setProjectAzimuth(Float projectAzimuth) {
		this.projectAzimuth = projectAzimuth;
	}

}
