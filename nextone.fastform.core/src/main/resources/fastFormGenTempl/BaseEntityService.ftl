<#-- 生成实体Service(接口)基类的模板  -->
<#-- 定义一些变量  -->
<#assign entityBean=entityBean uncapFirstEntityBean="${entityBean}"?uncap_first>
package ${prePackage}.entityservice;

import ${entityBeanPackage};
import nextone.framework.service.BaseService;

public interface Base${entityName}Service extends BaseService {
	
	public int create(${entityBean} ${uncapFirstEntityBean});

	public void update(${entityBean} ${uncapFirstEntityBean});

	boolean checkCdExist(Integer ${entityId}, String ${bizKey});
		
	public ${entityBean} get(int ${entityId});

	public ${entityBean} getByCd(String ${bizKey});
	
}