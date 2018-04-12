<#-- 生成初始化,修改等操作画面JSP的模板  -->
<!-- ${pgmCd} ${formDesc}  -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="n" uri="http://www.nextone.com/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="am" %> 
<script src='${r"${ctx}"}/dwr/interface/${pgmCd}Controller.js'
	type='text/javascript'></script>
<script src="${r"${ctx}"}/resources/js/fastForm/${pgmCd}.js?ver=${versionNum}" type="text/javascript"></script>
	
<!--input form-->
<div class="container-fluid">
	<form:form class="form-horizontal" id="inputform" commandName="formbean">
		<form:hidden path="formMode" id="hidFormMode" />
		<#-- 遍历list中隐藏字段的数据 -->
		<#list components as compon >
		<#if compon.inputType == "HIDDEN">
		<#if  formId== compon.fieldName>
		<form:hidden path="${compon.hidAttributePath}" id="${compon.hidAttributeId}" />
		</#if>
		</#if>
		</#list>
		<#-- 定义一个变量来记录索引,不使用其本身提供的索引值(compon_index),如果不是隐藏类型才+1 -->
		<#assign componIndex=-1>
		<#-- 遍历list中的数据 -->
		<#list components as compon> 
		<#-- 当输入类型不是HIDDEN类型时才进行下面的操作,不然直接去List中的下一个元素 -->
		<#if compon.inputType!="HIDDEN">
		<#assign componIndex=componIndex+1>
		<#if componIndex % 2 == 0>     
		
		<div class="form-group form-group-sm">
		</#if>
		<#--如果为普通控件则引入相应控件-->
		<#if compon.inputType == "CODE" || compon.inputType == "TEXT"  || compon.inputType == "NUM">
		<#include "/InputCodeCompo.ftl">
		<#--如果为时间日期控件则引入相应控件-->
		<#elseif compon.inputType == "DATE" || compon.inputType == "DATETIME">
		<#include "/InputDateCompo.ftl">
		<#--如果为下拉框控件则引入相应控件-->
		<#elseif compon.inputType == "SELECT">
		<#include "/InputSelectCompo.ftl">
		<#--如果为滑块控件则引入相应控件-->
		<#elseif compon.inputType == "SWITCH">
		<#include "/InputSwitchCompo.ftl">
		<#--如果为多国语控件则引入相应控件-->
		<#elseif compon.inputType == "MULTLANG">
		<#include "/InputMultiLangCompo.ftl">
		</#if>
		<#if compon_has_next>   <#-- 如果后面还有元素则判断当前控件是第奇数个还是偶数个,第偶数个需要加上</div> -->
		<#if componIndex % 2 == 1>
		</div>
		</#if>
		<#else>
		
		</div> <#-- 如果后面没有元素则不管奇数还是偶数直接加上</div> -->
		</#if>
		</#if><#-- 与判断输入类型是否为HIDDEN的if相匹配 -->
		</#list>
		
		<#include "/saveAndReturnButtonCompo.ftl">
		
	</form:form>
</div>
<!--input form end-->

<script type="text/javascript">
<n:outJsonObj obj="${r"$"}{formbean}" outProperty="window.formbean"/>  
	$(function() {
			// 初始化
			jQuery(document).ready(function() {
				<#if upperCaseFlg?exists>
				//输入小写转大写
				$("input.upperCase").textInputLower2Upper();
				</#if>
				<#if (switchComponents?size>0)>
				//设置滑块显示,这里在serviceimpl中赋值了初始值
			    $(":checkbox.switches").bootstrapSwitch();
				</#if>
			  	<#-- 遍历滑块字段list中的数据    labelForComponentId即为控件的Id  formInputPath即是实体Bean+字段名-->
				<#list switchComponents as switchCompon>
				$("#${switchCompon.labelForComponentId}").bootstrapSwitch('state', formbean.${switchCompon.formInOrOutputPath}==${switchCompon.onValueTransl});
				</#list>
			  	
				if(window['initForm']){
					initForm();
				}
			}); 
			
		//按钮点击事件
		$("#btnOk").click(function() {
			//清除message
			clearMsg();
			var formval = getFormBeanObj({
				formId : "inputform"
			});
			
			<#if (multiComponents?size>0)>
			// 多国语名Bean list
			</#if>
			<#-- 遍历多国语字段list中的数据      formInputPath即是实体Bean+字段名-->
			<#list multiComponents as multiCompon>
			formval.${multiCompon.formInOrOutputPath}Beans = formbean.${multiCompon.formInOrOutputPath}Beans;
			</#list>
			
			<#if (switchComponents?size>0)>
			//把每个滑块的值赋给formval
			</#if>
		  	<#-- 遍历滑块字段list中的数据    labelForComponentId即为控件的Id  formInputPath即是 实体Bean+字段名-->
			<#list switchComponents as switchCompon>
			formval.${switchCompon.formInOrOutputPath} = $("#${switchCompon.labelForComponentId}").bootstrapSwitch('state')?${switchCompon.onValueTransl}:${switchCompon.offValueTransl};
			</#list>
			console.info(formval);
			
			<#include "/createModeJs.ftl">
			
			<#include "/updateModeJs.ftl">
			
	    });//btnOk click end
		$("#btnBack").click(function() {
			btnBackHandle();
		});
	});//页面加载end
</script>