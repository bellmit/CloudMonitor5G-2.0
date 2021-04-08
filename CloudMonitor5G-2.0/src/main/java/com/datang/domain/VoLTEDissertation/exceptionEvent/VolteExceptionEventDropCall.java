/**
 * 
 */
package com.datang.domain.VoLTEDissertation.exceptionEvent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * VoLTE质量专题----异常事件分析,掉话异常事件指标bean
 * 
 * @author yinzhipeng
 * @date:2016年4月16日 上午9:20:45
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_EE_DROP_CALL")
public class VolteExceptionEventDropCall extends VolteExceptionEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5691213968937214987L;

	/**
	 * 掉话时RSRP
	 */
	private Float dropCallRsrp;
	/**
	 * 掉话时SINR
	 */
	private Float dropCallSinr;
	/**
	 * 重叠覆盖度
	 */
	private Float overlappingCoverageRate;

	/**
	 * @return the dropCallRsrpdropCallRsrp
	 */
	@Column(name = "DROP_CALL_RSRP")
	public Float getDropCallRsrp() {
		return dropCallRsrp;
	}

	/**
	 * @param dropCallRsrp
	 *            the dropCallRsrp to set
	 */
	public void setDropCallRsrp(Float dropCallRsrp) {
		this.dropCallRsrp = dropCallRsrp;
	}

	/**
	 * @return the dropCallSinrdropCallSinr
	 */
	@Column(name = "DROP_CALL_SINR")
	public Float getDropCallSinr() {
		return dropCallSinr;
	}

	/**
	 * @param dropCallSinr
	 *            the dropCallSinr to set
	 */
	public void setDropCallSinr(Float dropCallSinr) {
		this.dropCallSinr = dropCallSinr;
	}

	/**
	 * @return the overlappingCoverageRateoverlappingCoverageRate
	 */
	@Column(name = "OVER_COVER_RATE")
	public Float getOverlappingCoverageRate() {
		return overlappingCoverageRate;
	}

	/**
	 * @param overlappingCoverageRate
	 *            the overlappingCoverageRate to set
	 */
	public void setOverlappingCoverageRate(Float overlappingCoverageRate) {
		this.overlappingCoverageRate = overlappingCoverageRate;
	}

}
