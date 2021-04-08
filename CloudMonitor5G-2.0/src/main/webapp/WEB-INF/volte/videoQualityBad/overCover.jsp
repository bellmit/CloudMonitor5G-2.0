<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>重叠覆盖</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
	//获取及回显道路名称
	var mainTableId = 'overCoverTable';
	function getRoadNameCallBackUrl(){
		return "<%=basePath%>videoQuality/roadNameCallBack.action";
	}
	//存储日志id,按","分隔
	var testLogItemIds = '${testLogItemIds}';
	//存储质差路段id
	var volteqbrid;
	//小区图层名称
	var drawCellFileName;
	//gisCommon会调用获取测试日志ID
	function getCellTestLogItemIds(){
		return testLogItemIds;
	}
	//gisCommon会调用获取生成小区SQL的url
	function getCellActionUrl(){
		return "<%=basePath%>gisSql/queryCellInfo.action";
	}
	//gisCommon会调用获取质差路段ID
	function getGpsPointVQBId(){
		return volteqbrid;
	}
	//gisCommon会调用获取生成质差路段轨迹的url
	function getVQBGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryVQBIndexGpsPoint.action";
	}
	//gisCommon会调用获取生成质差路段轨迹行驶方向的url
	function getVQBDirectionActionUrl(){
		return "<%=basePath%>gisSql/queryVQBDirection.action";
	}
	//gisCommon会调用获取小区与邻区连线的url
	function VQBCellToCellActionUrl(){
		return "<%=basePath%>gisSql/queryVQBCellToCell.action";
	}
	//gisCommon会调用获取LTE切换事件图标轨迹的URL
	function getVQBLTEGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryVQBLTEGpsPoint.action";
	}
	/* 开始分析,分析按钮 */
	function doAnalysis(){
		var overCoverRow = $("#overCoverTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!overCoverRow){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个质差路段!",'warning');
			return ;
		}
		testLogItemIds = overCoverRow.recSeqNo;
		volteqbrid = overCoverRow.id;
		if(!volteqbrid){
			$.messager.alert("提示","请勾选某个质差路段!",'warning');
			return ;
		}
		// 以ajax方式，发送到服务器，完成操作
		$.post("${pageContext.request.contextPath}/videoQuality/doOverCoverAnalysis.action",{roadId:overCoverRow.id},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#overCoverAdviceTable").datagrid('loadData',result.overCoverAzimuthOrDowndipAngleAdjust);
				}
			}
		,"json");
		$.post("${pageContext.request.contextPath}/videoQuality/checkDrawCellFileName.action",{roadId:overCoverRow.id},
			function(result){
				//如果上次为null或者undefined,或者当前图层与上一次图层不一样
				if(!drawCellFileName||result!=drawCellFileName){
					mapIframe.window.drawCell();
				}
				drawCellFileName = result;
			}
		,"json");
		mapIframe.window.drawQBR();
	}
	/* 地图最大化 */
	function hiddenLayout(){
		$("#mainLayout").layout("collapse","east");
	}
	
	//质差路段字段排序开关
	var isRoadSortable = true ;
	<c:if test="${null == overCovers || empty overCovers}">
		isRoadSortable = false;
	</c:if>
	$(function(){
		<c:if test="${null == overCovers || empty overCovers}">
			$("#overCoverTable").datagrid('mergeCells',{
				index: 0,
				field: 'boxId',
				colspan:8
			});
		</c:if>
		$("#overCoverAdviceTable").datagrid('mergeCells',{
			index: 0,
			field: 'cellName',
			colspan:6
		});
	});
	
	</script>
<!-- 	<script type="text/javascript" src="http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json&ak=liKpDfLP41rNnZmM1D33WljN&callback=abcdefg"></script> -->
  </head>
  <body class="easyui-layout" id="mainLayout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" style="width: 100%;height: 100%;" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=36" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 重叠覆盖统计 -->
		    	<div data-options="region:'north',border:false" style="height:55%;">
	    			<table id="overCoverTable" class="easyui-datagrid" data-options="title:'重叠覆盖问题列表',scrollbarSize:0,fit:true,striped:true,onLoadSuccess:mainTableLoadSuccess,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt3',remoteSort:false,multiSort:true"  >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'id',checkbox:true"></th>
	    						<th data-options="field:'boxId',width:120,align:'center',formatter:showTooltip">BOXID</th>
	    						<th data-options="field:'callType',width:60,align:'center',
	    							formatter: function(value,row,index){
										if (value){
											if(0==value){
												return '主叫';
											}else if(1==value){
												return '被叫';
											}
										}
										return value;
									}
	    						">主/被叫</th>
	    						<th data-options="field:'time',width:90,align:'center',formatter:showTooltip">发生时间</th>
	    						<th data-options="field:'vmos',width:80,align:'center',formatter:showTooltip">VMOS</th>
	    						<th data-options="field:'keyParameterCause',width:60,align:'center',
	    							formatter: function(value,row,index){
										if (value){
											if(0==value){
												return '<div title=丢包率原因>丢包率原因</div>';
											}else if(1==value){
												return '<div title=视频码率>视频码率</div>';
											}else if(2==value){
												return '<div title=视频帧率>视频帧率</div>';
											}else if(3==value){
												return '<div title=音频码率>音频码率</div>';
											}else if(-1==value){
												return '<div title=其它原因>其它原因</div>';
											}
										}
										return value;
									}
	    						">关键参<br>数原因</th>
	    						<th data-options="field:'cellName',width:60,align:'center',formatter:showTooltip">服务小区</th>
	    						<th data-options="field:'optimization',width:60,align:'center',formatter:showTooltip">优化建议</th>
	    						<th data-options="field:'fileName',width:120,align:'center',formatter:showTooltip">日志名称</th>
	    						<th data-options="field:'recSeqNo',width:120,align:'center',hidden:true">日志ID</th>
	    						<th data-options="field:'beginLatitude',width:120,align:'center',hidden:true">开始纬度</th>
	    						<th data-options="field:'courseLatitude',width:120,align:'center',hidden:true">中间纬度</th>
	    						<th data-options="field:'endLatitude',width:120,align:'center',hidden:true">结束纬度</th>
	    						<th data-options="field:'beginLongitude',width:120,align:'center',hidden:true">开始经度</th>
	    						<th data-options="field:'courseLongitude',width:120,align:'center',hidden:true">中间经度</th>
	    						<th data-options="field:'endLongitude',width:120,align:'center',hidden:true">结束经度</th>
	    						<th data-options="field:'m_stRoadName',width:100,align:'center',hidden:true">路段名称</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<c:forEach items="${overCovers}" var="overCover">
		    					<tr>
		    						<td>${overCover.id}</td>
		    						<td>${overCover.testLogItem.boxId}</td>
		    						<td>${overCover.testLogItem.callType}</td>
		    						<td>${overCover.timeValue}</td>
		    						<td>${overCover.vmos}</td>
	    							<td>${overCover.keyParameterCause}</td>
		    						<td>${overCover.cellName}</td>
		    						<td>${overCover.optimization}</td>
		    						<td>${overCover.testLogItem.fileName}</td>
		    						<td>${overCover.testLogItem.recSeqNo}</td>
		    						<td>${overCover.latitude}</td>
		    						<td>${overCover.latitude}</td>
		    						<td>${overCover.latitude}</td>
		    						<td>${overCover.longitude}</td>
		    						<td>${overCover.longitude}</td>
		    						<td>${overCover.longitude}</td>
		    						<td>${overCover.m_stRoadName}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == overCovers}">
	    						<tr>
		    						<td></td>
		    						<td>未选择测试日志</td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    					</tr>
	    					</c:if>
	    					<c:if test="${null != overCovers && empty overCovers}">
	    						<tr>
	    							<td></td>
		    						<td>该测试日志下没有重叠覆盖问题</td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    					</tr>
	    					</c:if>
	    				</tbody>
	    			</table>
	    			<div id="tt3" >
						<a href="#" style="width:50px;text-decoration:underline;" onclick="doAnalysis();" title="分析">分析</a>
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	<!-- 重叠覆盖优化建议汇总 -->
		    	<div data-options="region:'center',border:false" >
									<table id="overCoverAdviceTable" class="easyui-datagrid" data-options="title:'优化建议:方位角或下倾角调整',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
					    				<thead>
					    					<tr>
					    						<th data-options="field:'cellName',width:120,align:'center',formatter:showTooltip">小区名</th>
					    						<th data-options="field:'pci',width:50,align:'center'">PCI</th>
					    						<th data-options="field:'earfcn',width:65,align:'center'">EARFCN</th>
					    						<th data-options="field:'rsrp',width:70,align:'center',formatter:numToFixed2Formatter">RSRP</th>
					    						<th data-options="field:'toProblemDotDistance',width:60,align:'center',formatter:numToFixed2Formatter">和问题采样<br>点距离(m)</th>
					    						<th data-options="field:'projectAzimuth',width:55,align:'center'">工参方位角</th>
					    						<th data-options="field:'projectDowndipAngle',width:55,align:'center'">工参下倾角</th>
					    					</tr>
					    				</thead>
					    				<tbody>
				    						<tr>
					    						<td>未选择重叠覆盖问题</td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    					</tr>
					    				</tbody>
					    			</table>
								</div>
							</div>
						</div>
					
	</body>
</html>
