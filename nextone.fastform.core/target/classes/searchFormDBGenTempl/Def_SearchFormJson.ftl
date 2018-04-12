{
	"pgmPath":"${searchFormModuleCd}/${"${searchFormPgmCd}"?lower_case}",
	"daoImplBeanNm":"${searchFormPgmCd}DaoImpl",
	"exportXls":true,
	"sqlHqlFlg":false,
	"condiModel":	[
		<#-- 遍历list中的数据 -->
		<#list components as compon>
		<#--如果输入类型是CODE,则如下-->
		<#if compon.inputType =="CODE" >
		{"name":"${compon.fieldName}",
		"labelMsgCode":"label.${compon.fieldName}",
		"inputType":"code",
		"condiType":null,
		"subForm":null,
		"value":null,
		"multiInput":true,
		"transpose":true,
		"listSelClsCd":null,
		"listSelLblValBeanService":null,
		"autoCompleteCode":null
		}<#if compon_has_next>,</#if>
		<#--如果输入类型是SELECT,则如下-->
		<#elseif compon.inputType =="SELECT">
		<#if compon.typeCd != "">
		{"name":"${compon.fieldName}",
		"labelMsgCode":"label.${compon.fieldName}",
		"inputType":"select",
		"condiType":null,
		"subForm":null,
		"value":null,
		"multiInput":null,
		"transpose":null,
		"listSelClsCd":"${compon.typeCd}",
		"listSelLblValBeanService":null,
		"autoCompleteCode":null
		}<#if compon_has_next>,</#if>
		<#elseif compon.lblValBeanService != "">
		{"name":"${compon.fieldName}",
		"labelMsgCode":"label.${compon.fieldName}",
		"inputType":"select",
		"condiType":null,
		"subForm":null,
		"value":null,
		"multiInput":null,
		"transpose":null,
		"listSelClsCd":null,
		"listSelLblValBeanService":"${compon.lblValBeanService}",
		"autoCompleteCode":null
		}<#if compon_has_next>,</#if>
		</#if>
		<#elseif compon.inputType =="NUM">
		{"name":"${compon.fieldName}",
		"labelMsgCode":"label.${compon.fieldName}",
		"inputType":"num",
		"condiType":null,
		"subForm":null,
		"value":null,
		"multiInput":null,
		"transpose":null,
		"listSelClsCd":null,
		"listSelLblValBeanService":null,
		"autoCompleteCode":null
		}<#if compon_has_next>,</#if>
		<#elseif compon.inputType =="MULTLANG" >
		<#assign fieldNameOfMultiLang = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))>
		{"name":"${fieldNameOfMultiLang}",
		"labelMsgCode":"label.${fieldNameOfMultiLang}",
		"inputType":"desctext",
		"condiType":null,
		"subForm":null,
		"value":null,
		"multiInput":null,
		"transpose":null,
		"listSelClsCd":null,
		"listSelLblValBeanService":null,
		"autoCompleteCode":null
		}<#if compon_has_next>,</#if>
		<#elseif  compon.inputType == "TEXT">
		{"name":"${compon.fieldName}",
		"labelMsgCode":"label.${compon.fieldName}",
		"inputType":"desctext",
		"condiType":null,
		"subForm":null,
		"value":null,
		"multiInput":null,
		"transpose":null,
		"listSelClsCd":null,
		"listSelLblValBeanService":null,
		"autoCompleteCode":null
		}<#if compon_has_next>,</#if>
		<#elseif compon.inputType == "SWITCH">
		{"name":"${compon.fieldName}",
		"labelMsgCode":"label.${compon.fieldName}",
		"inputType":"select",
		"condiType":null,
		"subForm":null,
		"value":null,
		"multiInput":null,
		"transpose":null,
		"listSelClsCd":"${compon.typeCd}",
		"listSelLblValBeanService":null,
		"autoCompleteCode":null
		}<#if compon_has_next>,</#if>
		<#elseif compon.inputType == "DATE" || compon.inputType == "DATETIME">
		{"name":"${compon.fieldName}",
		"labelMsgCode":"label.${compon.fieldName}",
		"inputType":"date",
		"condiType":null,
		"subForm":null,
		"value":null,
		"multiInput":null,
		"transpose":null,
		"listSelClsCd":null,
		"listSelLblValBeanService":null,
		"autoCompleteCode":null
		}<#if compon_has_next>,</#if>
		</#if>
		</#list>
	],
	
	"colModel" :[
	 {"name":"${entityId}", "customizable":false}, 
	 <#-- 遍历list中的数据 -->
	 <#list components as compon>
	 <#--如果输入类型不是HIDDEN,同时也不是DATETIME类型 -->
	 <#if compon.inputType !="HIDDEN" && compon.inputType !="DATETIME" >
	 <#if compon.inputType =="MULTLANG" >
	 <#assign fieldNameOfMultiLang = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))>
	 {"thMsgCode": "label.${fieldNameOfMultiLang}",		"name":"${fieldNameOfMultiLang}",		"index":"${fieldNameOfMultiLang}",		"width":70}<#if compon_has_next>,</#if>
	 <#elseif compon.manyToOnePojo?exists>
	 <#assign subFieldNameOfManyToOneId = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Id"))>
	 {"thMsgCode": "label.${compon.fieldName}",		"name":"${subFieldNameOfManyToOneId}Nm",		"index":"${subFieldNameOfManyToOneId}Nm",		"width":70}<#if compon_has_next>,</#if>
	 <#else>
	 {"thMsgCode": "label.${compon.fieldName}",		"name":"${compon.fieldName}",		"index":"${compon.fieldName}",		"width":70}<#if compon_has_next>,</#if>
	 </#if>
	 <#elseif compon.inputType =="DATETIME">
	 {"thMsgCode": "label.${compon.fieldName}",		"name":"${compon.fieldName}",       "index":"${compon.fieldName}",      "width":160,"formatter":"dateTime"}<#if compon_has_next>,</#if>   
	 </#if>
	 </#list>
    ],
    "sort" : [
    ]
}