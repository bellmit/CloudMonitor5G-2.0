<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>4/5G互操作的异常事件分析参数</title>

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
    <%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script> --%>
    <style type="text/css">
        .inputDivShow{
            display: inline-block; *
            display: inline;
            font-size: 12px;
            margin: 5px;
            padding-left: 0;
            padding-right: 0;
            text-align: right;
            width: 200px;
        }
        .inputDivShow1{
            display: inline-block; *
        display: inline;
            font-size: 12px;
            margin: 5px;
            padding-left: 0;
            padding-right: 0;
            text-align: center;
            width: 160px;
        }
        .inputDivShow2{
            display: inline-block; *
        display: inline;
            font-size: 12px;
            margin: 5px;
            padding-left: 0;
            padding-right: 0;
            text-align: center;
            width: 80px;
        }
        .titleDivShow{
            display: inline-block; *
        display: inline;
            font-size: 12px;
            margin: 5px;
            padding-left: 0;
            padding-right: 0;
            text-align: right;
            width: 180px;
        }
        .inputDivShow input,.inputDivShow1 input{
            width:60px;
        }
        .inputDivShow select.inputDivShow1 select{
            width:100px;
        }
    </style>
    <script type="text/javascript">
    	/* 重置初始值 */
		function resetForm(){
			$.messager.confirm('提示','你确认要重置为初始值?',function(flag){
				if(flag){
					$("#area_before").textbox('setValue',"30");
					$("#area_after").textbox('setValue',"30");
					$("#switch_cover4To5g").textbox('setValue',"30");
					$("#switch_quality_4To5g").textbox('setValue',"30");
					$("#relocate_cover4To5g").textbox('setValue',"30");
					$("#relocate_quality_4To5g").textbox('setValue',"30");
					$("#reselect_cover_4To5g").textbox('setValue',"30");
					$("#reselect_quality_4To5g").textbox('setValue',"30");
					$("#switch_cover_5To4g").textbox('setValue',"30");
					$("#switch_quality_5To4g").textbox('setValue',"30");
					$("#relocate_cover_5To4g").textbox('setValue',"30");
					$("#relocate_quality_5To4g").textbox('setValue',"30");
					$("#reselect_cover_5To4g").textbox('setValue',"30");
					$("#reselect_quality_5To4g").textbox('setValue',"30");
				}
			});
		}
    
        var submitForm = function () {
        	$('#infoform').form('submit', {
			    url:"${pageContext.request.contextPath}/updateexceptionparam45g/updateexceptionevent45gmethod.action",
			    onSubmit: function(param){
			    	param.description = "4/5G互操作的异常事件分析参数";
			    	param.gateType = 4;
			    },
			    success:function(data){
			    	var data = eval('(' + data + ')');
					if (data.success) {
						$.messager.alert('提示','数据更新成功','info');
					} else {
						$.messager.alert('提示','数据更新失败','info');
					}
			    }
			});
/*             var timeHigh = $("#area_before").val();
            var timeLow = $("#area_after").val();
            var switchCover4To5G = $("#switch_cover4To5g").val();
            var switchQuality4To5G = $("#switch_quality_4To5g").val();
            var relocateCover4To5G = $("#relocate_cover4To5g").val();
            var relocateQuality4To5G = $("#relocate_quality_4To5g").val();
            var reselectCover5To4G = $("#reselect_cover_4To5g").val();
            var reselectQuality4To5G = $("#reselect_quality_4To5g").val();
            var switchCover5To4G = $("#switch_cover_5To4g").val();
            var switchQuality5To4G = $("#switch_quality_5To4g").val();
            var relocateCover5To4G = $("#relocate_cover_5To4g").val();
            var relocateQuality5To4G = $("#relocate_quality_5To4g").val();
            var reselectCover5To4G = $("#reselect_cover_5To4g").val();
            var reselectQuality5To4G = $("#reselect_quality_5To4g").val();

            var datas = {
                "gateType":1,
                "description":"4/5G互操作的异常事件分析参数",
                "timeHigh45g":timeHigh,
                "timeLow45g":timeLow,
                "switchCover4To5G":switchCover4To5G,
                "switchQuality4To5G":switchQuality4To5G,
                "relocateCover4To5G":relocateCover4To5G,
                "relocateQuality4To5G":relocateQuality4To5G,
                "reselectCover5To4G":reselectCover5To4G,
                "reselectQuality4To5G":reselectQuality4To5G,
                "switchCover5To4G":switchCover5To4G,
                "switchQuality5To4G":switchQuality5To4G,
                "relocateCover5To4G":relocateCover5To4G,
                "relocateQuality5To4G":relocateQuality5To4G,
                "reselectCover5To4G":reselectCover5To4G,
                "reselectQuality5To4G":reselectQuality5To4G
            }

            $.ajax({
                url:"${pageContext.request.contextPath}/updateexceptioneventmove/updateexceptioneventmovemethod.action",
                type:"POST",
                dataType:"text",
                data:datas,
                success:function(){
                    alert("数据更新成功");
                },
                error:function (result) {
                    alert("数据更新失败");
                }
            }); */
        }
    </script>
</head>
<body class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
<div data-options="region:'center',border:false" style="overflow-x: hidden;overflow-y: auto;">
	<form id="infoform" method="post">
        <div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">4/5G互操作异常分析无线指标评估范围</div></div>
        <div style="margin-left: 100px">
            <p>
                <label><strong>互操作之前</strong>
                    <input id="area_before" name="timeHigh45g" value="${timeHigh45g }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:1000" /> s
                </label>
                &nbsp;&nbsp;
                <label><strong>互操作之后</strong>
                    <input id="area_after" name="timeLow45g" value="${timeLow45g }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:10000" /> s
                </label>
            </p>
        </div>
        <div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">4/5G互操作异常分析无线原因分析门限设置</div></div>

        <div style="width:100%;">
            <div style="float: left;margin-left: 50px;margin-top: 20px">
                <div style="width: 150px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;">
                    <strong>LTE to NR切换失败</strong>
                </div>
            </div>
            <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
                <div><strong>弱覆盖采样点占比 >= </strong>
                    <input id="switch_cover4To5g" name="switchCover4To5G"  style="display: block" value="${switchCover4To5G }"   class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
                </div>
                <br>
                <div><strong>质差采样点占比 >= </strong>
                    <input id="switch_quality_4To5g" name="switchQuality4To5G" value="${switchQuality4To5G }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是质差原因</strong>
                </div>
            </div>
        </div>

        <div style="width:100%;display: inline-block">
            <div style="float: left;margin-left: 50px;margin-top: 20px">
                <div style="width: 150px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;">
                    <strong>LTE to NR重定位失败</strong>
                </div>
            </div>
            <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
                <div><strong>弱覆盖采样点占比 >= </strong>
                    <input id="relocate_cover4To5g" name="relocateCover4To5G"  style="display: block" value="${relocateCover4To5G }"   class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
                </div>
                <br>
                <div><strong>质差采样点占比 >= </strong>
                    <input id="relocate_quality_4To5g" name="relocateQuality4To5G" value="${relocateQuality4To5G }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是质差原因</strong>
                </div>
            </div>
        </div>

	    <div style="width:100%;">
	        <div style="float: left;margin-left: 50px;margin-top: 20px">
	            <div style="width: 150px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;word-break: break-all;">
	                <strong>LTE to NR重选失败</strong>
	            </div>
	        </div>
	        <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
	            <div><strong>弱覆盖采样点占比 >= </strong>
	                <input id="reselect_cover_4To5g" name="reselectCover4To5G"  style="display: block" value="${reselectCover4To5G }"  class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
	            </div>
	            <br>
	            <div><strong>质差采样点占比 >= </strong>
	                <input id="reselect_quality_4To5g" name="reselectQuality4To5G" value="${reselectQuality4To5G }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /><strong>% 认为是质差原因</strong>
	            </div>
	        </div>
	    </div>
   
	    <div style="width:100%;display: inline-block">
	        <div style="float: left;margin-left: 50px;margin-top: 20px">
	            <div style="width: 150px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;word-break: break-all;">
	                <strong>NR to LTE切换失败</strong>
	            </div>
	        </div>
	        <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
	            <div><strong>弱覆盖采样点占比 >= </strong>
	                <input id="switch_cover_5To4g" name="switchCover5To4G"  style="display: block" value="${switchCover5To4G }"  class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
	            </div>
	            <br>
	            <div><strong>质差采样点占比 >= </strong>
	                <input id="switch_quality_5To4g" name="switchQuality5To4G" value="${switchQuality5To4G }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /><strong>% 认为是质差原因</strong>
	            </div>
	        </div>
	    </div>
	    
	    <div style="width:100%;">
            <div style="float: left;margin-left: 50px;margin-top: 20px">
                <div style="width: 150px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;">
                    <strong>NR to LTE重定位失败</strong>
                </div>
            </div>
            <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
                <div><strong>弱覆盖采样点占比 >= </strong>
                    <input id="relocate_cover_5To4g" name="relocateCover5To4G"  style="display: block" value="${relocateCover5To4G }"   class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
                </div>
                <br>
                <div><strong>质差采样点占比 >= </strong>
                    <input id="relocate_quality_5To4g" name="relocateQuality5To4G" value="${relocateQuality5To4G }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是质差原因</strong>
                </div>
            </div>
        </div>

	    <div style="width:100%;display: inline-block">
	        <div style="float: left;margin-left: 50px;margin-top: 20px">
	            <div style="width: 150px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;word-break: break-all;">
	                <strong>NR to LTE重选失败</strong>
	            </div>
	        </div>
	        <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
	            <div><strong>弱覆盖采样点占比 >= </strong>
	                <input id="reselect_cover_5To4g" name="reselectCover5To4G"  style="display: block" value="${reselectCover5To4G }"  class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
	            </div>
	            <br>
	            <div><strong>质差采样点占比 >= </strong>
	                <input id="reselect_quality_5To4g" name="reselectQuality5To4G" value="${reselectQuality5To4G }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /><strong>% 认为是质差原因</strong>
	            </div>
	        </div>
	    </div>
    
    </form>



</div>
<div data-options="region:'south',border:false" style="height:40px;">
    <table width="100%" style="border-top:1px solid #95b8e7;">
        <tr height="35px">
            <td width="50%;" align="right">
                <shiroextend:hasAnyPermissions name="volteanalysisthreshold:save">
                    <a id="saveButton" iconCls="icon-ok" class="easyui-linkbutton" style="width:100px;" onclick="submitForm();"  >确认</a>
                </shiroextend:hasAnyPermissions>
            </td>
            <td width="50%;">
                <shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">
                    <a class="easyui-linkbutton" iconCls="icon-reload" style="width:100px;" onclick="resetForm();" >重置</a>
                </shiroextend:hasAnyPermissions>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
