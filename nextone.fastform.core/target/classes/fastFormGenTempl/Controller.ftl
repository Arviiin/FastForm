<#-- 生成画面Controller子类的模板  -->
<#assign pgmCd=pgmCd>
package ${prePackage}.controller;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Controller;

@Controller
@RemoteProxy(name = "${pgmCd}Controller")
public class ${pgmCd}Controller extends Base${pgmCd}Controller {
	//待扩展...
}