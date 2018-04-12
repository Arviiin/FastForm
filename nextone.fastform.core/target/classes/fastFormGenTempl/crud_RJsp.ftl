<#-- 生成查看画面JSP的模板  -->
<!-- ${pgmCd} ${formDesc}  -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="n" uri="http://www.nextone.com/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="am" %> 
<script src='${r"${ctx}"}/dwr/interface/${pgmCd}Controller.js'
	type='text/javascript'></script>
<script src='${r"${ctx}"}/resources/js/nextone.common.multilang.js'
	type='text/javascript'></script>
<!--input form-->
<div class="container-fluid">
	<form:form class="form-horizontal readmode" id="inputform"
		commandName="formbean">
		<form:hidden path="formMode" id="hidFormMode" />
		<c:set var="${"${entityBean}"?uncap_first}" value="${r"$"}{formbean.${"${entityBean}"?uncap_first}}" />
		<#-- 遍历list中隐藏字段的数据 -->
		<#list components as compon >
		<#if compon.inputType == "HIDDEN">
		<#if  formId== compon.fieldName>
		<form:hidden path="${compon.hidAttributePath}" id="${compon.hidAttributeId}" />
		</#if>
		</#if>
		</#list>
		
		<div class="panel panel-default inputformbk">
			<div class="panel-body topbuttonbar">
				<button type="button" class="btn btn-primary btn-sm" id="btnEdit">
					<span class="glyphicon glyphicon-edit"></span>
					<spring:message code="btn.edit" />
				</button>
				<button type="button" class="btn btn-primary btn-sm" id="btnBack">
					<spring:message code="btn.back" />
				</button>
			</div><!-- top button bar end -->
		</div>
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
		<#if compon.inputType == "CODE" || compon.inputType == "TEXT" || compon.inputType == "NUM">
		<#include "/InputCodeCompoR.ftl">
		<#--如果为时间日期控件则引入相应控件-->
		<#elseif compon.inputType == "DATE"  || compon.inputType == "DATETIME">
		<#include "/InputDateCompoR.ftl">
		<#--如果为下拉框控件则引入相应控件-->
		<#elseif compon.inputType == "SELECT">
		<#include "/InputSelectCompoR.ftl">
		<#--如果为滑块控件则引入相应控件-->
		<#elseif compon.inputType == "SWITCH">
		<#include "/InputSwitchCompoR.ftl">
		<#--如果为多国语控件则引入相应控件-->
		<#elseif compon.inputType == "MULTLANG">
		<#include "/InputMultiLangCompoR.ftl">
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
	</form:form>
</div>
<!--input form end-->
<script type="text/javascript">
<n:outJsonObj obj="${r"$"}{formbean}" outProperty="window.formbean"/> 
	$(function() {
		$("#btnBack").click(function() {
			btnBackHandle();
		});
		$("#btnEdit").click(function() {
			var ${formId} = $("#hid${"${formId}"?cap_first}").val();
			window.location = "${r"$"}{ctx}/${moduleCd}/${"${pgmCd}"?lower_case}/initUpdate/" + ${formId};
		});
	});
</script>