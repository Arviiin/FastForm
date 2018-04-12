CREATE TABLE ${"${pojoNm}"?upper_case}
    (
    
     	<#-- 遍历list中的数据  -->
	 	<#list components as compon>
		<#-- 只要输入字段不是createUserNm和updateUserNm,则如下 -->
		<#if compon.fieldName != "createUserNm" && compon.fieldName != "updateUserNm">
		<#--如果输入类型为HIDDEN,或者NUM的时候,则如下-->
		<#if compon.inputType == "HIDDEN" || compon.inputType == "NUM" || (compon.inputType == "SELECT" && compon.lblValBeanService !="") || "${compon.fieldName}"?ends_with("Id")>
	 	${"${compon.fieldDBName}"?upper_case}  NUMBER(${compon.precision},${compon.scale}) <#include "/TableItem01.ftl">
		<#--如果输入类型为DATE,DATETIME则如下-->
		<#elseif compon.inputType == "DATE" || compon.inputType == "DATETIME">
	 	${"${compon.fieldDBName}"?upper_case}  DATE <#include "/TableItem01.ftl">
	 	<#--如果输入类型为TEXT且enableFullWidthChar为true则如下-->
	 	<#elseif compon.inputType == "TEXT" &&  compon.enableFullWidthChar>
	 	${"${compon.fieldDBName}"?upper_case}  NVARCHAR2(${compon.maxlength}) <#include "/TableItem01.ftl">
	 	<#else>
	 	${"${compon.fieldDBName}"?upper_case}  VARCHAR2(${compon.maxlength}) <#include "/TableItem01.ftl">
	 	</#if>
	 	</#if>
	 	</#list>
        CONSTRAINT ${"${pojoNm}"?upper_case}PK01 PRIMARY KEY (${"${entityIdDBName}"?upper_case})
    );