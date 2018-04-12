<#-- 生成FormBean校验的模板  -->
<#-- 定义一些变量  -->
<#assign prePackage=prePackage pgmCd=pgmCd entityBean=entityBean uncapFirstEntityBean="${entityBean}"?uncap_first>
package ${prePackage}.validator;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import nextone.utils.BaseLogger;

import ${prePackage}.bean.${pgmCd}FormBean;
import ${prePackage}.validator.${entityBean}Validator;

public class Base${pgmCd}FormBeanValidator extends BaseLogger implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ${pgmCd}FormBean.class.equals(clazz);
	}

	@Autowired
	@Setter
	private ${entityBean}Validator ${uncapFirstEntityBean}Validator;

	@Override
	public void validate(Object target, Errors errors) {
		${pgmCd}FormBean bean = (${pgmCd}FormBean) target;
		errors.pushNestedPath("${uncapFirstEntityBean}");
		ValidationUtils.invokeValidator(this.${uncapFirstEntityBean}Validator,
				bean.get${entityBean}(), errors);
		errors.popNestedPath();
	}
}