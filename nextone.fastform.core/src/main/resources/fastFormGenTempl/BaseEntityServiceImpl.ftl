<#-- 生成实体ServiceImpl(即是Service的 实现类)基类的模板  -->
<#-- 定义一些变量  -->
<#assign prePackage=prePackage entityName=entityName uncapFirstEntityName="${entityName}"?uncap_first>
package ${prePackage}.entityservice.impl;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Setter;
import nextone.framework.service.impl.BaseServiceImpl;
import ${entityBeanPackage};
import ${prePackage}.dao.${entityName}Dao;
import ${prePackage}.entityservice.Base${entityName}Service;

public class Base${entityName}ServiceImpl extends BaseServiceImpl implements Base${entityName}Service {

	@Autowired
	@Setter
	private ${entityName}Dao ${uncapFirstEntityName}Dao;
	
	@Override
	public int create(${entityBean} ${"${entityBean}"?uncap_first}) {
		return ${uncapFirstEntityName}Dao.create(${"${entityBean}"?uncap_first});
	}

	@Override
	public void update(${entityBean} ${"${entityBean}"?uncap_first}) {
		${uncapFirstEntityName}Dao.update(${"${entityBean}"?uncap_first});		
	}
	@Override
	public boolean checkCdExist(Integer ${entityId}, String ${bizKey}) {
		return ${uncapFirstEntityName}Dao.checkCdExist(${entityId}, ${bizKey});
	} 
	
	@Override
	public ${entityBean} get(int ${entityId}) {
		return ${uncapFirstEntityName}Dao.get(${entityId}, getLoginInfo().getUserLangCd());
	}

	@Override
	public ${entityBean} getByCd(String ${bizKey}) {
		return ${uncapFirstEntityName}Dao.getByCd(${bizKey} );
	}
}