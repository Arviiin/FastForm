<#-- 生成实体ServiceImpl子类的模板  -->
package ${prePackage}.entityservice.impl;

import ${prePackage}.entityservice.${entityName}Service;

import org.springframework.stereotype.Service;
<#-- 定义一个变量entityName,并将entityName赋值给它 -->
<#assign entityName = entityName>
@Service("${"${entityName}"?uncap_first}Service")
public class ${entityName}ServiceImpl extends Base${entityName}ServiceImpl implements ${entityName}Service{
	//待扩展
}