<#-- 生成画面ServiceImpl(即是画面Service的实现类)基类的模板  -->
<#-- 定义一些变量  -->
<#assign prePackage=prePackage pgmCd=pgmCd entityBean=entityBean 
		 uncapFirstEntityService="${entityService}"?uncap_first uncapFirstEntityBean="${entityBean}"?uncap_first>
package ${prePackage}.service.impl;

import java.util.List;

import lombok.Setter;
import nextone.framework.bean.BaseTypeCd;
import nextone.framework.bean.LabelValueBean;
import nextone.framework.service.impl.UIServiceImpl;
import nextone.utils.MessageUtils;

import org.springframework.beans.factory.annotation.Autowired;

import ${prePackage}.bean.${pgmFormBean};
import ${entityBeanPackage};
import nextone.master.bean.TypeDetailBean;
import ${prePackage}.service.${pgmService};
<#list components as compon >
<#-- 如果lblValBeanService不为"",则  -->
<#if compon.lblValBeanService !="">
<#assign lblValBean = "${compon.lblValBeanService}"?substring(0,"${compon.lblValBeanService}"?index_of("."))>
import ${prePackage}.entityservice.${"${lblValBean}"?cap_first};
</#if>
</#list>
import ${prePackage}.entityservice.${entityService};
import nextone.master.entityservice.TypeDetailService;

public class Base${pgmCd}ServiceImpl extends UIServiceImpl implements ${pgmCd}Service {


	<#list components as compon >
	<#-- 如果lblValBeanService不为"",则  -->
	<#if compon.lblValBeanService != "">
	@Autowired
	@Setter
	private ${"${lblValBean}"?cap_first} ${lblValBean};
	</#if>
	</#list>
	
	@Autowired
	@Setter
	private ${entityService} ${uncapFirstEntityService};
	
	@Autowired
	private TypeDetailService typeDetailService;
	
	@Override
	public void initCreate(${pgmCd}FormBean bean) {
		//设置滑块的初始化值用于显示,待扩展......
		initList(bean);
	}	

	@Override
	public void create(${pgmCd}FormBean bean){
		${entityBean} ${uncapFirstEntityBean} = bean.get${entityBean}();
		${uncapFirstEntityService}.create(${uncapFirstEntityBean});
	}

	@Override
	public ${pgmCd}FormBean initRead(${pgmCd}FormBean bean) {
		${entityBean} ${uncapFirstEntityBean} = ${uncapFirstEntityService}.get(bean.get${"${formId}"?cap_first}());
		<#list components as compon >
		<#-- 如果是下拉框则    注意如果是下拉框类型且字段以Cd结尾,则去掉Cd再加Nm(待做) -->
		<#if compon.inputType == "SELECT" && compon.typeCd!="">
  <#--	<#assign fieldNameOfSelect = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))>   -->
		${uncapFirstEntityBean}.set${"${compon.fieldName}"?cap_first}Nm(typeDetailService.getName("${compon.typeCd}", ${uncapFirstEntityBean}.get${"${compon.fieldName}"?cap_first}()));
		</#if>
		</#list>
		if (${uncapFirstEntityBean} != null) {
			<#if (switchComponents?size>0)>
		  	<#-- 遍历滑块字段list中的数据    -->
			<#list switchComponents as switchCompon>
			${uncapFirstEntityBean}.set${"${switchCompon.fieldName}"?cap_first}Nm(MessageUtils.getSwitchFlgNm(${uncapFirstEntityBean}.get${"${switchCompon.fieldName}"?cap_first}(), "${switchCompon.onText}", "${switchCompon.offText}", ${switchCompon.onValueTranslJava}, super.getLoginInfo().getUserLocale()));
			</#list>
			</#if>
			bean.set${entityBean}(${uncapFirstEntityBean});
			
			<#list components as compon >
			<#-- 如果lblValBeanService不为"",则  -->
			<#if compon.lblValBeanService != "">
			<#if "${compon.formInOrOutputPath}"?ends_with("Id")>
			<#assign subFieldName = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Id"))>
			<#else>
			<#assign subFieldName = compon.fieldName>
			</#if>
			if (${lblValBean}.get(${uncapFirstEntityBean}.get${"${compon.fieldName}"?cap_first}()) != null) {
				${uncapFirstEntityBean}.set${"${compon.fieldName}"?cap_first}Nm(${lblValBean}.get(${uncapFirstEntityBean}.get${"${compon.fieldName}"?cap_first}()).get${"${subFieldName}"?cap_first}Nm());
			}
			</#if>
			</#list>
			
		}
		return bean;
	}
	
	@Override
	public void initUpdate(${pgmCd}FormBean bean) {
		${entityBean} ${uncapFirstEntityBean} = ${uncapFirstEntityService}.get(bean.get${"${formId}"?cap_first}());
		bean.set${entityBean}(${uncapFirstEntityBean});
		initList(bean);
		<#--	此部分注释掉了,因为觉得可能是废话
				<#if (switchComponents?size>0)>
				<#list switchComponents as switchCompon>
				bean.get${entityBean}().set${"${switchCompon.fieldName}"?cap_first}(${switchCompon.onValueTransl}.equals(${"${entityBean}"?uncap_first}.get${"${switchCompon.fieldName}"?cap_first}()) ? YN_FLG.YES : YN_FLG.NO);
				</#list>
				</#if> 	-->
	}

	@Override
	public void update(${pgmCd}FormBean bean){
		${entityBean} ${uncapFirstEntityBean} = bean.get${entityBean}();
		${uncapFirstEntityService}.update(${uncapFirstEntityBean});
	}
	
	/**
	 * 初始化大分类下拉框
	 * @param bean
	 */
	private void initList(${pgmCd}FormBean bean){
		<#list components as compon >
		<#-- 如果是下拉框则  -->
		<#if compon.inputType == "SELECT">
		<#-- 如果是大分类类型的下拉框则如下  -->
		<#if compon.typeCd!="">
		List<TypeDetailBean> ${compon.fieldName}List = typeDetailService.getList("${compon.typeCd}");
		bean.setList${"${compon.fieldName}"?cap_first}(${compon.fieldName}List);
		super.addEmptyItem(TypeDetailBean.class, bean.getList${"${compon.fieldName}"?cap_first}());
		<#-- 如果是参照别的实体Id类型的下拉框则如下  -->
		<#elseif compon.lblValBeanService!="">
		List<LabelValueBean> ${compon.fieldName}List = ${lblValBean}.getLblValList();
		bean.setList${"${compon.fieldName}"?cap_first}(${compon.fieldName}List);
		super.addEmptyItem(LabelValueBean.class, bean.getList${"${compon.fieldName}"?cap_first}());
		</#if>
		</#if>
		</#list>
	}
	
}