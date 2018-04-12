<#-- 生成实体校验子类的模板  -->
<#assign entityBean=entityBean>
package ${prePackage}.validator;

import org.springframework.stereotype.Component;

@Component
public class ${entityBean}Validator extends Base${entityBean}Validator {
	//待扩展...
}
