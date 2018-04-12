<#-- 生成画面ServiceImpl子类的模板  -->
<#assign pgmCd=pgmCd>
package ${prePackage}.service.impl;

import nextone.master.service.${pgmCd}Service;
import org.springframework.stereotype.Service;

@Service("${"${pgmCd}"?lower_case}Service")
public class ${pgmCd}ServiceImpl extends Base${pgmCd}ServiceImpl implements ${pgmCd}Service{
	//待扩展...
}