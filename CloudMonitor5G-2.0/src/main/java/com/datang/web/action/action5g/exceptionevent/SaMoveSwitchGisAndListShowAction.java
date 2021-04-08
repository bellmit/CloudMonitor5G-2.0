package com.datang.web.action.action5g.exceptionevent;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.exceptionevent.Iads5gExceptionEventTable;
import com.datang.domain.platform.projectParam.Cell5G;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.exceptionevent.ExceptionEventSAMoveService;
import com.datang.service.exceptionevent.GisAndListShowServie;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAMoveDto;
import com.datang.web.action.action5g.exceptionevent.dto.GisAndListShowDto;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SA模式移动-系统内切换失败事件分析
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-25 13:55
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
@Data
@Slf4j
public class SaMoveSwitchGisAndListShowAction extends PageAction implements ModelDriven<GisAndListShowDto> {
	
	@Value("${dzTestLogTtemUrl}")
	private String dzTestLogTtemUrl;
	
    private GisAndListShowDto gisAndListShowDto = new GisAndListShowDto();
    @Autowired
    private GisAndListShowServie gisAndListShowServie;
    
	/**
	 * 测试日志服务
	 */
	@Autowired
	private ITestLogItemService testlogItemService;
	
    @Autowired
    private ExceptionEventSAMoveService exceptionEventSAMoveService;
    
    private String nrFriendlyName;
    
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public AbstractPageList doPageQuery(PageList pageList) {
    	HttpSession session = ServletActionContext.getRequest()
				.getSession();
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		List<String> logNameList = new ArrayList<String>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				Set<String> logNameSet = new HashSet<String>();
				for (TestLogItem testLogItem : queryTestLogItems) {
					logNameSet.add(testLogItem.getFilelink());
				}
				logNameList = new ArrayList(logNameSet);
			}
		}else{
			EasyuiPageList easyuiPageList = new EasyuiPageList();
			easyuiPageList.setRows(null);
			easyuiPageList.setTotal("0");
			return easyuiPageList;
		}
		pageList.putParam("logNameList",logNameList);
        pageList.putParam("nrFriendlyName",nrFriendlyName);
        AbstractPageList list = gisAndListShowServie.pageListMoveSwitch(pageList);
        List<Iads5gExceptionEventTable> row = list.getRows();
        try {
			if(row.size() > 0){
				for (int i = 0;i < row.size();i++) {
					Iads5gExceptionEventTable iads5gExceptionEventTable = row.get(i);
					Date failTime = simpleDateFormat.parse(iads5gExceptionEventTable.getScellChangeFailTime());
					long timeLong = failTime.getTime();
					iads5gExceptionEventTable.setScellChangeFailTimeLong(timeLong);
					
					String nrFriendlyName = iads5gExceptionEventTable.getNrFriendlyName();
					if(nrFriendlyName!=null && StringUtils.hasText(nrFriendlyName)){
						List<Cell5G> cell5GList = gisAndListShowServie.getNrCell(nrFriendlyName);
						if(cell5GList.size()>0){
							Cell5G cell5G = cell5GList.get(0);
							iads5gExceptionEventTable.setNrTac(cell5G.getTac());
							iads5gExceptionEventTable.setNrCellid(cell5G.getCellId()+"");
			    			iads5gExceptionEventTable.setNrFrequencyPoint(cell5G.getFrequency1()+"");
			    			iads5gExceptionEventTable.setNrPci(cell5G.getPci()+"");
						}
					}
					String nrTargetFriendlyName = iads5gExceptionEventTable.getNrTargetCellCellName();
					if(nrTargetFriendlyName!=null && StringUtils.hasText(nrTargetFriendlyName)){
						List<Cell5G> cell5GList = gisAndListShowServie.getNrCell(nrTargetFriendlyName);
						if(cell5GList.size()>0){
							Cell5G cell5G = cell5GList.get(0);
							iads5gExceptionEventTable.setNrTargetCellTac(cell5G.getTac());
							iads5gExceptionEventTable.setNrTargetCellCellid(cell5G.getCellId()+"");
			    			iads5gExceptionEventTable.setNrTargetCellFrequency(cell5G.getFrequency1()+"");
			    			iads5gExceptionEventTable.setNrTargetCellPci(cell5G.getPci()+"");

						}
					}
//        		//NR辅小区信息
//            	String point = iads5gExceptionEventTable.getNrFrequencyPoint();
//            	String nrPci = iads5gExceptionEventTable.getNrPci();
//            	if(point != null && nrPci != null){
//            		List<Cell5G> cell5G = gisAndListShowServie.getCellIdAndCellName(nrPci,point);
//            		if(cell5G.size() > 0){
//            			Cell5G cell = cell5G.get(0);
//            			iads5gExceptionEventTable.setNrCellid(cell.getCellId()+"");
//            			iads5gExceptionEventTable.setNrFriendlyName(cell.getCellName());
//            		}
//            	}
			    	row.set(i, iads5gExceptionEventTable);
				}
			}
			list.setRows(row);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return list;
    }

    @Override
    public GisAndListShowDto getModel() {
        return gisAndListShowDto;
    }
    
    /**
     * 获取采样点的经纬度
     * @author maxuancheng
     * date:2019年12月11日 下午3:39:23
     * @return
     */
    private Long recSeqNo;
    private Long timeLong;
    public String getGpsPointData(){
    	if(recSeqNo == null || timeLong == null){
    		return null;
    	}
    	TestLogItem queryCQTByFloorName = testlogItemService.queryTestLogById(recSeqNo);
    	ExceptionEventASAMoveDto eea = exceptionEventSAMoveService.queryData();
    	List<Object> rs = gisAndListShowServie.getGpsPointData(recSeqNo,timeLong,eea.getTimeHighSaMove(),eea.getTimeLowSaMove(),queryCQTByFloorName.getFileName());
    	ActionContext.getContext().getValueStack().push(rs);
    	return ReturnType.JSON;
    }


    /**
     * 主页面跳转
     * @return
     */
    public String samoveswitchviewui(){
        return ReturnType.LISTUI;

    }
    
	/**
	 * 下载测试日志
	 * 
	 * @return
	 */
	public String downloadFile() {
		Long reqno = Long.valueOf(gisAndListShowDto.getReqSeqNo());
		String respString = "<!DOCTYPE html><html><head><title></title></head><body><h2>";
		if (null != reqno) {
			TestLogItem queryTestLogById = testlogItemService
					.queryTestLogById(reqno);
			if (null != queryTestLogById
					&& StringUtils.hasText(queryTestLogById.getFilelink())) {
				if(queryTestLogById.getFilelink().contains("./")){
					String filePath = dzTestLogTtemUrl + queryTestLogById.getFilelink().replace("./", "/");
					if(filePath.indexOf(queryTestLogById.getFileName()) == -1){
						filePath = filePath + queryTestLogById.getFileName();
					}
					File log = new File(filePath);
					System.out.println(filePath);
					if (!log.exists() || !log.isFile()) {
						respString = respString+queryTestLogById.getFileName()+" 日志不存在!路径:"+ filePath+"</br>";
					}else{
						return "downloadLog";
					}
				}else{
					respString = respString+queryTestLogById.getFileName()+":日志名称的路径应该为相对路径!</br>";
				}
			}
		}
		ServletOutputStream outputStream = null;
		try {
			respString = respString + "</h2><input type='button' value='返回' onclick='self.window.history.go(-1);' /></body></html>";
			outputStream = ServletActionContext.getResponse().getOutputStream();
			outputStream.write(respString.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != outputStream) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// 文件不存在
		return null;
	}

	/**
	 * 下载测试日志
	 * 
	 * @return
	 */
	public InputStream getDownloadTestLog() {
		Long reqno = Long.valueOf(gisAndListShowDto.getReqSeqNo());
		if (null != reqno) {
			TestLogItem queryTestLogById = testlogItemService.queryTestLogById(Long.valueOf(reqno));
			if (null != queryTestLogById
					&& StringUtils.hasText(queryTestLogById.getFilelink())) {
				String filePath = dzTestLogTtemUrl + queryTestLogById.getFilelink().replace("./", "/");
				if(filePath.indexOf(queryTestLogById.getFileName()) == -1){
					filePath = filePath + queryTestLogById.getFileName();
				}
				File log = new File(filePath);
				if (log.exists() && log.isFile()) {
					try {
						String filelink = queryTestLogById.getFilelink();
						String substring = filelink.substring(filelink
								.lastIndexOf("."));
						ActionContext.getContext().put(
								"fileName",
								new String((queryTestLogById.getFileName())
										.getBytes(), "ISO8859-1"));
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
					try {
						return new FileInputStream(log);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}



    /**
     * 下载文件
     */

//    public void downloadFile(){
//    	BufferedOutputStream bos = null;
//        try {
//
//            HttpServletResponse response = ServletActionContext.getResponse();
//            String fileId = gisAndListShowDto.getFileId();
//            String fileName = gisAndListShowDto.getFileName();
//            Iads5gExceptionEventTable iads5gExceptionEventTable = gisAndListShowServie.queryRecordById(fileId);
//            //NR辅小区信息
//        	String point = iads5gExceptionEventTable.getNrFrequencyPoint();
//        	String nrPci = iads5gExceptionEventTable.getNrPci();
//        	if(point != null && nrPci != null){
//        		List<Cell5G> cell5G = gisAndListShowServie.getCellIdAndCellName(nrPci,point);
//        		if(cell5G.size() > 0){
//        			Cell5G cell = cell5G.get(0);
//        			iads5gExceptionEventTable.setNrCellid(cell.getCellId()+"");
//        			iads5gExceptionEventTable.setNrFriendlyName(cell.getCellName());
//        		}
//        	}
//            
//            
//            byte[] fileNameByte = fileName.getBytes("UTF-8");
//            String filename = new String(fileNameByte, "ISO8859-1");
//            response.setHeader("Content-Disposition", "attachment;filename=\"" + filename+".csv\"");
//            response.setContentType("application/csv");
//            response.setCharacterEncoding("UTF-8");
//            ServletOutputStream outputStream = response.getOutputStream();
//            bos = new BufferedOutputStream(outputStream);
//            StringBuffer stringBuffer = new StringBuffer();
//            stringBuffer.append("序号,地市,切换请求发起时间,切换失败发生时间,文件名,经度,纬度,切换失败类型,无线原因分析,源小区信息DL_ibler,源小区TAC,源小区CELLID,源小区小区友好名,源小区频点,源小区PCI,源小区ss-rsrp,目标小区TAC,目标小区CELLID,目标小区小区友好名,目标小区频点,目标小区PCI"+"\n");
//            stringBuffer.append(iads5gExceptionEventTable.getId().longValue()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getCity()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getScellChangeReqStartTime()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getScellChangeFailTime()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getFileName()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getLongitude()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getDimension()+",");
//            
//            stringBuffer.append(iads5gExceptionEventTable.getScellChangeFailType()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getWirelessFailReason()+",");
//            
//            stringBuffer.append(iads5gExceptionEventTable.getNrDlIbler()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getNrTac()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getNrCellid()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getNrFriendlyName()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getNrFrequencyPoint()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getNrPci()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getNrPccRsrp()+",");
//
//            stringBuffer.append(iads5gExceptionEventTable.getNrTargetCellTac()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getNrTargetCellCellid()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getNrTargetCellCellName()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getNrTargetCellFrequency()+",");
//            stringBuffer.append(iads5gExceptionEventTable.getNrTargetCellPci()+",");
//            bos.write((byte)0XEF);
//            bos.write((byte)0XBB);
//            bos.write((byte)0XBF);
//            bos.write(stringBuffer.toString().getBytes("UTF-8"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//        	if(bos!=null){
//        		try {
//					bos.flush();
//					bos.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//        	}
//		}
//    }

}
