<#-- 生成实体Bean校验的模板  -->
<#-- 定义一些模板变量  -->
<#assign prePackage=prePackage entityBean=entityBean entityName=entityName>
package ${prePackage}.validator;

import lombok.Setter;
import nextone.framework.validator.CommonValidate;
import ${entityBeanPackage};
import ${prePackage}.bean.MultiLangValidCondiBean;
import ${prePackage}.entityservice.${entityName}Service;
import ${prePackage}.validator.ListMultiLangBeansValidator;
import nextone.utils.BaseLogger;
import nextone.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.google.common.base.Strings;

/**
 * @author jlzhuang
 */

public class Base${entityBean}Validator extends BaseLogger implements Validator {

	@Setter
	@Autowired
	<#-- 定义一个变量uncapFirstEntityName并将"${entityName}"?uncap_first 赋值给它-->
	<#assign uncapFirstEntityName="${entityName}"?uncap_first>
	private ${entityName}Service ${uncapFirstEntityName}Service;

	@Setter
	@Autowired
	private ListMultiLangBeansValidator listMultiLangBeansValidator;

	@Override
	public boolean supports(Class<?> clazz) {
		return ${entityBean}.class.isAssignableFrom(clazz);
}
	@Override
	public void validate(Object target, Errors errors) {
		${entityBean} bean = (${entityBean}) target;
		Integer maxLen = 20;
		String[] len = new String[1];
		len[0] = maxLen.toString();
		
		<#if (mustComponents?size>0)>
		// 字段不可为空
		</#if>
		<#-- 遍历字段list中的数据      -->
		<#list mustComponents as mustCompon>
		<#if mustCompon.inputType == "NUM">
		if (bean.get${"${mustCompon.fieldName}"?cap_first}()==null) //大类型,可以为null,故如Integer都可以这样判断
		<#elseif mustCompon.inputType == "MULTLANG">
		<#assign fieldNameOfMultiLang = "${mustCompon.fieldName}"?substring(0,"${mustCompon.fieldName}"?index_of("Cd"))>
		if (StringUtils.isEmpty(bean.get${"${fieldNameOfMultiLang}"?cap_first}())) 
		<#else>	
		if (StringUtils.isEmpty(bean.get${"${mustCompon.fieldName}"?cap_first}())) 
		</#if>
			<#if mustCompon.inputType == "MULTLANG">
			<#assign fieldNameOfMultiLang = "${mustCompon.fieldName}"?substring(0,"${mustCompon.fieldName}"?index_of("Cd"))>
			errors.rejectValue("${fieldNameOfMultiLang}", "fielderror.require", null, null);
			<#else>
			errors.rejectValue("${mustCompon.fieldName}", "fielderror.require", null, null);
			</#if>
		</#list>
		
		<#-- 遍历字段list中的数据      -->
		<#if (codeComponents?size>0)>
		// 编号英文或数字    code类型的都要校验
		</#if>
		<#list codeComponents as codeCompon>
		<#if codeCompon.must>
		<#-- 定义一个变量capFirstCodeListFieldNm并将"${codeCompon.fieldName}"?cap_first 赋值给它-->
		<#assign capFirstCodeListFieldNm="${codeCompon.fieldName}"?cap_first>
		if (!StringUtils.isBlank(bean.get${capFirstCodeListFieldNm}()))
			CommonValidate.checkInputIsCode(errors, "${codeCompon.fieldName}", bean.get${capFirstCodeListFieldNm}());
		</#if>
		</#list>
		
		// xxxCd即xxx编号不可重复     即是check bizKey
		<#-- 定义一个变量capFirstBizKey并将"${bizKey}"?cap_first 赋值给它          定义一个变量capFirstEntityId并将"${entityId}"?cap_first 赋值给它-->
		<#assign capFirstBizKey="${bizKey}"?cap_first capFirstEntityId="${entityId}"?cap_first>
		if ( !StringUtils.isBlank(bean.get${capFirstBizKey}())) {
			if (${uncapFirstEntityName}Service.checkCdExist(bean.get${capFirstEntityId}(), 
					bean.get${capFirstBizKey}())) {
				errors.rejectValue("${bizKey}", "fielderror.exist", null, null);
			}
		}
		<#-- 遍历list中的数据 -->
		<#list components as compon>
		<#--如果输入类型为MULTLANG,则如下-->
		<#if compon.inputType =="MULTLANG">
		<#if compon.unique>
		<#assign fieldNameOfMultiLang = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))>
		if (bean.get${capFirstEntityId}() != null && !Strings.isNullOrEmpty(bean.get${"${fieldNameOfMultiLang}"?cap_first}())) {
			errors.pushNestedPath("${"${fieldNameOfMultiLang}"?cap_first}Beans");
			this.listMultiLangBeansValidator
					.setCondiBean(new MultiLangValidCondiBean(true, "${"${entityIdDBName}"?lower_case}",
							bean.get${capFirstEntityId}(), "${"${pojoNm}"?lower_case}", "${"${compon.fieldDBName}"?lower_case}",
							"${compon.nameType}"));
			ValidationUtils.invokeValidator(this.listMultiLangBeansValidator,
					bean.get${"${fieldNameOfMultiLang}"?cap_first}Beans(), errors);
			errors.popNestedPath();
		</#if>
		</#if>
		</#list>
		}
		<#if (maxlengthComponents?size>0)>
		// 以下字段长度不超过20
		</#if>
		<#-- 遍历字段list中的数据      -->
		<#list maxlengthComponents as maxlengthCompon>
		if (bean.get${"${maxlengthCompon.fieldName}"?cap_first}().length() > maxLen)
			errors.rejectValue("${maxlengthCompon.fieldName}", "fielderror.wordmaxlength", len, null);
		</#list>
	}
}