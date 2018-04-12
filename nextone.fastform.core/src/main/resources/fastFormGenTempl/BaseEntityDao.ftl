<#-- 生成实体Dao基类的模板  -->
<#-- 定义一些变量 -->
<#assign entityBean=entityBean uncapFirstEntityBean="${entityBean}"?uncap_first>
package ${prePackage}.dao;

import nextone.framework.dao.BaseDao;
import ${entityBeanPackage};

public interface Base${entityName}Dao extends BaseDao{
	
	int create(${entityBean} ${uncapFirstEntityBean});
	
	void update(${entityBean} ${uncapFirstEntityBean});
	
	boolean checkCdExist(Integer ${entityId}, String ${bizKey});

	${entityBean} get(int ${entityId}, String langCd);

	${entityBean} getByCd(String ${bizKey});
}