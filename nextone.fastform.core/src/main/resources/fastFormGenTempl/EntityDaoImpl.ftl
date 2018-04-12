<#-- 生成实体DaoImpl子类的模板  -->
<#assign entityName=entityName>
package ${prePackage}.dao.impl;

import ${prePackage}.dao.${entityName}Dao;

import org.springframework.stereotype.Repository;

@Repository("${"${entityName}"?uncap_first}Dao")
public class ${entityName}DaoImpl extends Base${entityName}DaoImpl implements ${entityName}Dao{
	//待扩展...
}