<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLte专题-视频质差整体分析</title>

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
	<%@ include file="../../../taglibs/echarts.jsp" %>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
	$.ajaxSetup({async:false});
	//存储日志id,按","分隔
	var testLogItemIds = '${testLogItemIds}';
	//gisCommon会调用获取测试日志ID
	function getTestLogItemIds2VQB(){
		return testLogItemIds;
	}
	//gisCommon会调用获取生成测试日志下所有类型质差路段轨迹的url
	function getTLI2VQBGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryVQBGpsPoint.action";
	}
	var everyCWBRNumPieChart;
	var rtpAndRsrpChart;
	$(function(){
		/* if (navigator.onLine) {
			  alert('online');
			} else {
			  alert('offline');
			}
		 */
		if(testLogItemIds){
			initTable();
		}
		window.setTimeout(function(){
			initChart();
		},2000);
		
	});
	//重新加载
	
	window.onresize=reload;
	function reload(){
		self.window.setTimeout(function(){
			everyCWBRNumPieChart.resize();
			rtpAndRsrpChart.resize();
		}, 100);
		
	}
	function initTable(){
		$.post("${pageContext.request.contextPath}/videoQuality/doWholeAnalysis.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#wholeIndexTable0").datagrid('loadData',result.wholeRoadIndex0);
					$("#wholeIndexTable1").datagrid('loadData',result.wholeRoadIndex5);
					$("#wholeIndexTable2").datagrid('loadData',result.wholeRoadIndex2);
					$("#wholeIndexTable3").datagrid('loadData',result.wholeRoadIndex3);
					var everyCWBRNum = result.wholeRoadIndex1.rows[0];
					if(everyCWBRNum){
						for(var everyCWBRNumIndex = 0 ; everyCWBRNumIndex < everyCWBRNumPieItem[0].length ; everyCWBRNumIndex++){
							if(everyCWBRNum[everyCWBRNumPieItem[1][everyCWBRNumIndex]]){
								everyCWBRNumPieOption.series[0].data.push({name:everyCWBRNumPieItem[0][everyCWBRNumIndex],value:everyCWBRNum[everyCWBRNumPieItem[1][everyCWBRNumIndex]]});
							}else{
								everyCWBRNumPieOption.series[0].data.push({name:everyCWBRNumPieItem[0][everyCWBRNumIndex],value:undefined});
							}
						}
					}
					var everyCellNum = result.wholeRoadIndex4.rows;
					if(everyCellNum){
						for(var i = 0 ; i < everyCellNum.length ; i++){
								rtpAndRsrpChartOption.series.push(everyCellNum[i]);
							}
						//}
					}
					
				}
			}
		,"json");
	}

	
		
		var everyCWBRNumPieItem=[['[1,2.5)','[2.5,3)','[3,3.5)','[3.5,5)'],['value1to25','value25to3','value3to35','value35to5']];
		/* VMOS值分布占比饼图option */
		var	everyCWBRNumPieOption = {
			tooltip : {trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"}, 
		    legend: {orient : 'vertical',x : '30',data:everyCWBRNumPieItem[0]},
		    toolbox: {show : true,
		    	feature : {
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : false,
		    series : [{name:'VMOS占比',type:'pie',radius:'80%',center: ['60%', '50%'],data:['']}]
		};
		
		
		
		
		/* Chart路径配置 */
        require.config({
            paths: {echarts: '${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist'}
        });
		//各质差原因堆积图
	rtpAndRsrpChartOption = {
		   	 tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		       data:['乒乓切换','邻区缺失','弱覆盖','干扰','重叠覆盖','模式转换','下行调度数小','其他'],
		       x:'center',
		       y:'top'
		       
		    },
		    color:[
		           '#00e0f2', '#ff00ff', '#ff3000', '#0080ff', '#04af65',
		           '#0000ff','#00ffff', '#2eff09' 
		       ],
		    toolbox: {
		        show : true,
		        orient: 'vertical',
		        x: 'right',
		        y: '30',
		        feature : {
		            //mark : {show: true},
		            //dataView : {show: true, readOnly: false},
		            magicType : {show: true, type: ['stack', 'tiled']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		  
		    grid:{
		    	height:'60%',
		    	y2:'10%',
		    	y:'20%'
		    	//width:'10%'
		    },
		    calculable : true,
		    xAxis : [
		        {
		            type : 'category',
		            data : ['丢包率','视频码率','视频帧率','音频码率','其它原因']
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		           
		        }
		    ],
		     series : []
	};
		/* 初始化图表 */
        function initChart(){
			require(
				['echarts','echarts/chart/pie'],
				function (ec) {
					everyCWBRNumPieChart = ec.init(document.getElementById("everyCWBRNumPieChart")); 
					everyCWBRNumPieChart.setOption(everyCWBRNumPieOption);
				
				}
			);
			require(
					['echarts','echarts/chart/bar'],
					function (ec) {
						rtpAndRsrpChart = ec.init(document.getElementById("wode")); 
						rtpAndRsrpChart.setOption(rtpAndRsrpChartOption);
						
					}
			);
	    }
		function hiddenLayout(){
			$("#mainLayout").layout("collapse","east");
		}
	</script>
	
  </head>
  
  <body id="mainLayout" class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" data-options="fit:true">
    			<iframe id="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=31" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
    			<!-- 指标列表 -->
    			<div data-options="region:'north',border:false" style="height:11%;padding:0px;">
	    			<table id="wholeIndexTable0"  class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:true,title:'视频质差路段指标概览',tools:'#tt'" >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'videoBitRate',width:90,align:'center'">视频码率</th>
	    						<th data-options="field:'videoFrameRate',width:120,align:'center'">视频帧率</th>
	    						<!-- <th data-options="field:'packLossRateForVoice',width:120,align:'center',formatter:numToFixed2Formatter">语音丢包率</th> -->
	    						<th data-options="field:'packLossRateForVideo',width:120,align:'center',formatter:numToFixed2Formatter">丢包率</th>
	    						<th data-options="field:'irtt',width:80,align:'center',formatter:numToFixed2Formatter">i_RTT</th>
	    						<th data-options="field:'audioBitRate',width:80,align:'center'">音频码率</th>
	    						<th data-options="field:'vmos',width:80,align:'center',formatter:numToFixed2Formatter">VMOS</th>
	    						<th data-options="field:'rsrpValueAvg',width:80,align:'center',formatter:numToFixed2Formatter">RSRP</th>
	    						<th data-options="field:'sinrValueAvg',width:80,align:'center',formatter:numToFixed2Formatter">SINR</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<tr><td></td><td></td><td></td><td></td><td></td><td></td></tr>
	    				</tbody>
	    			</table>
	    			<div id="tt" >
						<a href="#" style="width:75px;text-decoration:underline;" onclick="goToPage('${pageContext.request.contextPath}/videoQuality/downloadVideoQualityBadExcel.action');" title="详细报表导出">详细报表导出</a>
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	<div data-options="region:'center',border:false" >
		    		<div class="easyui-layout" style="width:100%;height: 100%;">
				    	<!-- 质差问题VMOS分布统计 -->
				    	<div data-options="region:'north',border:false" style="height:34%;padding:2px 0px 0px 0px;">
				    		<div class="easyui-panel" data-options="fit:true,title:'VMOS分布'">
				    			<div class="easyui-layout" style="width: 100%;height: 100%;overflow:hidden;" >
			    					<div data-options="region:'west',border:false" style="width:35%;">
			    						<table id="wholeIndexTable1" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false"  >
						    				<thead>
						    					<tr>
						    						<th data-options="field:'name',width:60,align:'center'">VMOS范围</th>
						    						<th data-options="field:'value',width:50,align:'center',formatter:numToFixed2Formatter">占比</th>
						    						
						    					</tr>
						    				</thead>
						    				<tbody>
						    					<tr><td></td><td></td><td></td><td></td></tr>
						    					
						    				</tbody>
						    			</table>
			    					</div>
			    					<div id="everyCWBRNumPieChart" data-options="region:'center',border:false" >
			    						
			    					</div>
			    				</div>
				    		</div>
				    	</div>
				    	<!-- 视频质差问题汇总 -->
				    	<div data-options="region:'center',border:false" ">
				    		<div class="easyui-panel" data-options="fit:true,title:'视频质差问题路段汇总'">
			    						<table id="wholeIndexTable2"style="overflow:hidden;"class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false"  >
						    				<thead>
						    					<tr>
						    						<th data-options="field:'roadNum',width:60,align:'center'">视频质差<br>数量</th>
						    						<th data-options="field:'vmosPointNumRatio',width:180,align:'center',formatter:numToFixed2Formatter">视频质差VMOS采样点<br>占总VMOS采样点数比例(%)</th>
						    						<th data-options="field:'cellNumRatio',width:130,align:'center',formatter:numToFixed2Formatter">涉及小区数占<br>总小区数比例(%)</th>
						    						<th data-options="field:'terminalNumRatio',width:150,align:'center',formatter:numToFixed2Formatter">涉及终端数占总<br>测试终端数比例(%)</th>
						    					</tr>
						    				</thead>
						    				<tbody>
						    					<tr><td></td><td></td><td></td><td></td></tr>
						    				</tbody>
						    			</table>
				    		</div>
				    	</div>
				    	<div data-options="region:'south',border:false" style="height:47%;padding:2px 0px 0px 0px;overflow: hidden;">
				    		<div class="easyui-panel" data-options="fit:true,title:'视频质差关键参数'">
			    			<table id="wholeIndexTable3" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false"  >
						    	<thead>
						    		<tr>
							    		<th rowspan="2" data-options="field:'cause',width:30,align:'center',formatter:showTooltip" >原因</th>
	   									<th colspan="2" >乒乓切换</th>
	   									<th colspan="2" >邻区缺失</th>
	   									<th colspan="2" >弱覆盖</th>
	   									<th colspan="2" >干扰</th>
	   									<th colspan="2" >重叠覆盖</th>
	   									<th colspan="2" >模式转换</th>
	   									<th colspan="2" >下行调度数小</th>
	   									<th colspan="2" >其他</th>
	   									<th colspan="2" >汇总</th>
   									</tr>
						    		<tr>
						    			<th data-options="field:'pingPongNum',width:15,align:'center',formatter:showTooltip">个数</th>
						    			<th data-options="field:'pingPongRatio',width:15,align:'center',formatter:showTooltip">占比</th>
						    			<th data-options="field:'adjacentNum',width:15,align:'center',formatter:showTooltip">个数</th>
						    			<th data-options="field:'adjacentRatio',width:15,align:'center',formatter:showTooltip">占比</th>
						    			<th data-options="field:'weakCoverNum',width:15,align:'center',formatter:showTooltip">个数</th>
						    			<th data-options="field:'weakCoverRatio',width:15,align:'center',formatter:showTooltip">占比</th>
						    			<th data-options="field:'disturbNum',width:15,align:'center',formatter:showTooltip">个数</th>
						    			<th data-options="field:'disturbRatio',width:15,align:'center',formatter:showTooltip">占比</th>
						    			<th data-options="field:'overCoverNum',width:15,align:'center',formatter:showTooltip">个数</th>
						    			<th data-options="field:'overCoverRatio',width:15,align:'center',formatter:showTooltip">占比</th>
						    			<th data-options="field:'patternSwitchNum',width:15,align:'center',formatter:showTooltip">个数</th>
						    			<th data-options="field:'patternSwitchRatio',width:15,align:'center',formatter:showTooltip">占比</th>
						    			<th data-options="field:'downDispatchSmallNum',width:15,align:'center',formatter:showTooltip">个数</th>
						    			<th data-options="field:'downDispatchSmallRatio',width:15,align:'center',formatter:showTooltip">占比</th>
						    			<th data-options="field:'otherNum',width:15,align:'center',formatter:showTooltip">个数</th>
						    			<th data-options="field:'otherRatio',width:15,align:'center',formatter:showTooltip">占比</th>
						    			<th data-options="field:'collectNum',width:15,align:'center',formatter:showTooltip">个数</th>
						    			<th data-options="field:'collectRatio',width:15,align:'center',formatter:showTooltip">占比</th>
						    			
						    		</tr>
						    	</thead>
						    	<tbody>
						    		<tr><td></td><td></td><td></td><td></td><td></td></tr>
						    	</tbody>
						   </table>
						   </div>
			    		</div>
				    </div>
			    </div>
			    
			    <div id="wode" data-options="region:'south',border:false,title:'视频质差原因分布',collapsible:false" style="height:24%;padding:2px 0px 0px 0px;">
			    
			    	
			    </div>	
			   
		    </div>
    	</div>
  </body>
</html>