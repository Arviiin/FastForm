package ${prePackage}.searchformdao;

import java.util.Map;

import nextone.framework.bean.SearchFormCondiBean;
import nextone.framework.bean.SearchFormSqlBean;
import nextone.framework.bean.searchformdef.SearchFormDefiBean;
import nextone.framework.dao.impl.SearchFormDaoImpl;
import nextone.utils.MessageUtils;
import nextone.utils.StringUtils;
public class Base${searchFormPgmCd}DaoImpl extends SearchFormDaoImpl {
	@Override
	public String getQuerySql(SearchFormCondiBean condiBean,
			Map<String, Object> paramMap, SearchFormDefiBean searchFormDefiBean) {
			// 拼接sql语句
			StringBuffer sqlStr = new StringBuffer();
			//显示列bean
			SearchFormSqlBean sqlBean = new SearchFormSqlBean();
			
			<#-- 遍历List中的数据 -->
			<#list components as compon>
			<#if compon.inputType == "SWITCH">
			String ${compon.fieldName}OnText = MessageUtils.getMessageByKey("${compon.onText}", super.getLoginInfo().getUserLocale());
			</#if>
			</#list>
			<#-- 遍历List中的数据 -->
			<#list components as compon>
			<#if compon.inputType == "SWITCH">
			String ${compon.fieldName}OffText = MessageUtils.getMessageByKey("${compon.offText}", super.getLoginInfo().getUserLocale());
			</#if>
			</#list>
			
			sqlStr.append("SELECT ");
			
			<#assign tableJoinNum=0 >
			<#-- 遍历list中的数据 -->
			<#list components as compon>
			<#--如果输入类型为HIDDEN,则如下-->
			<#if compon.inputType =="HIDDEN">
			<#if entityId==compon.fieldName>
			sqlBean.addSqlSelField("m6.${compon.fieldName}","${compon.fieldName}");
			</#if>
			<#--如果输入类型为MULTLANG,则如下-->
			<#elseif compon.inputType == "MULTLANG">
			<#assign fieldNameOfMultiLang = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))>
			sqlBean.addSqlSelField("${"${fieldNameOfMultiLang}"?lower_case}.nameDesc","${fieldNameOfMultiLang}");
			<#--如果输入类型为SWITCH,则如下-->
			<#elseif compon.inputType == "SWITCH">
			sqlBean.addSqlSelField("case when m6.${compon.fieldName} =${compon.onValueTransl} then '"+${compon.fieldName}OnText+
					"' when m6.${compon.fieldName} =${compon.offValueTransl} then '"+${compon.fieldName}OffText+
					"' else null end","${compon.fieldName}");
			<#--如果输入类型为TEXT,则如下-->
			<#elseif compon.inputType == "TEXT">
			<#if compon.fieldName == "createUserNm" || compon.fieldName == "updateUserNm">
			<#assign tableJoinNum=tableJoinNum+1 >
			sqlBean.addSqlSelField("t_${tableJoinNum}.emplNm", "${compon.fieldName}");
			<#--如果输入类型为TEXT且不是createUserNm和updateUserNm剩余其他字段,则如下-->
			<#else>
			sqlBean.addSqlSelField("m6.${compon.fieldName}","${compon.fieldName}");
			</#if>
			<#--如果输入类型为SELECT类型,则如下-->
			<#elseif compon.inputType == "SELECT" >
			<#if compon.manyToOnePojo?exists><#--如果下拉框取的是实体,则如下-->
			<#assign subFieldNameOfManyToOneId = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Id"))>
			sqlBean.addSqlSelField("${subFieldNameOfManyToOneId}nm.nameDesc","${subFieldNameOfManyToOneId}Nm");
			<#elseif compon.typeCd != ""><#--如果下拉框取的大分类,则如下-->
			sqlBean.addSqlSelField("${"${compon.fieldName}"?lower_case}nm.detailNm1", "${compon.fieldName}");
			</#if>
			<#--如果输入类型为剩余其他类型,则如下-->
			<#else>
			sqlBean.addSqlSelField("m6.${compon.fieldName}","${compon.fieldName}");
			</#if>
			</#list>
					
			sqlStr.append(sqlBean.getSqlByCondiCols(condiBean.getListColModel()));
			// 关联查询表
			sqlStr.append(" FROM ${pojoNm} m6 ");
			
			<#-- 遍历List中的数据 -->
			<#list components as compon>
			<#if compon.inputType == "MULTLANG">
			<#assign fieldNameOfMultiLang = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))>
			// 多国语
			sqlStr.append("  LEFT JOIN m6.${fieldNameOfMultiLang}MultiLang as ${"${fieldNameOfMultiLang}"?lower_case} ");
			sqlStr.append("   with ${"${fieldNameOfMultiLang}"?lower_case}.langCd = :langCd ");
			</#if>
			</#list>

			<#assign tableJoinNum=0 >
			//创建人,更新人
			<#-- 遍历list中的数据 -->
			<#list components as compon>
			<#--如果输入类型为TEXT,则如下-->
			<#if compon.inputType =="TEXT">
			<#if compon.fieldName == "createUserNm" || compon.fieldName == "updateUserNm">
			<#assign tableJoinNum=tableJoinNum+1 >
			sqlStr.append(" LEFT JOIN m6.${compon.joinFiled} AS t_${tableJoinNum} ");
			</#if>
			</#if>
			</#list>
			
			<#-- 遍历list中的数据   -->
			<#list components as compon>
			<#if compon.inputType == "SELECT">
			<#if compon.manyToOnePojo?exists ><#-- 当字段的manyToOnePojo存在时,执行下面代码 -->
			<#assign subFieldNameOfManyToOneId = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Id"))>
			sqlStr.append("  LEFT JOIN m6.${subFieldNameOfManyToOneId} as ${subFieldNameOfManyToOneId} ");
			sqlStr.append("  LEFT JOIN ${subFieldNameOfManyToOneId}.${subFieldNameOfManyToOneId}NmMultiLang as ${subFieldNameOfManyToOneId}nm ");
			sqlStr.append("  with ${subFieldNameOfManyToOneId}nm.langCd = :langCd ");
			<#elseif compon.typeCd != ""><#--如果下拉框取的大分类,则如下-->
			//下拉框大分类
			sqlStr.append("   LEFT JOIN m6.${compon.fieldName}TypeDtl as ${"${compon.fieldName}"?lower_case}nm ");
			sqlStr.append("   with ${"${compon.fieldName}"?lower_case}nm.langCd = :langCd ");
			sqlStr.append("   and ${"${compon.fieldName}"?lower_case}nm.typeCd = '${compon.typeCd}' ");
			</#if>
			</#if>
			</#list>
			
			// 参数
		    paramMap.put("langCd", super.getLoginInfo().getUserLangCd());
			
			// 查询条件
			sqlStr.append(" where 1 = 1 ");
		   
		    <#assign tableJoinNum=0 >
			<#-- 遍历list中的数据 -->
			<#list components as compon>
			<#--如果输入类型不为HIDDEN,则拿出list的数据打印-->
			<#if compon.inputType !="HIDDEN">
			<#--如果输入类型为MULTLANG,则如下-->
			<#if compon.inputType == "MULTLANG">
			<#assign fieldNameOfMultiLang = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))>
			sqlStr.append(getWhereSql(searchFormDefiBean,condiBean,paramMap," ${"${fieldNameOfMultiLang}"?lower_case}.nameDesc", "${fieldNameOfMultiLang}"));
			<#--如果输入类型为TEXT,则如下-->
			<#elseif compon.inputType == "TEXT">
			<#if compon.fieldName == "createUserNm" || compon.fieldName == "updateUserNm">
			<#assign tableJoinNum=tableJoinNum+1 >
			sqlStr.append(getWhereSql(searchFormDefiBean,condiBean,paramMap," t_${tableJoinNum}.emplNm","${compon.fieldName}"));
			<#--如果输入类型为TEXT且不是createUserNm和updateUserNm剩余其他字段,则如下-->
			<#else>
			sqlStr.append(getWhereSql(searchFormDefiBean,condiBean,paramMap," m6.${compon.fieldName}","${compon.fieldName}"));
			</#if>
			<#--如果输入类型为剩余其他类型,则如下-->
			<#else>
			sqlStr.append(getWhereSql(searchFormDefiBean,condiBean,paramMap," m6.${compon.fieldName}","${compon.fieldName}"));
			</#if>
			</#if>
			</#list>
			
			if (StringUtils.isEmpty(condiBean.getSidx())) {// HQL时需要判断是否有sidx，SQL时不需要判断
				sqlStr.append(getOrderBySql(condiBean,paramMap,searchFormDefiBean));
			}
			return sqlStr.toString();
	}
	
	protected String getOrderBySql(SearchFormCondiBean condiBean,
			Map<String, Object> paramMap, SearchFormDefiBean searchFormDefiBean) {
		return " ORDER BY m6.${bizKey} ";
	}
}
