<#-- 生成实体DaoImpl基类的模板  -->
<#-- 定义一些变量  -->
<#assign prePackage=prePackage entityName=entityName entityBean=entityBean>
package ${prePackage}.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import nextone.common.SeqDefine;
import nextone.framework.bean.LoginInfoParam;
import nextone.framework.dao.impl.BaseDaoImpl;
import nextone.utils.CollectionsUtils;
import nextone.utils.ConvertUtils;

import ${entityBeanPackage};
import ${prePackage}.bean.MultiLangBean;
import ${prePackage}.dao.Base${entityName}Dao;
import ${prePackage}.dao.MultiLangDao;
import ${prePackage}.pojo.${pojoNm};

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Preconditions;

public class Base${entityName}DaoImpl extends BaseDaoImpl implements Base${entityName}Dao {
	
	@Setter
	@Autowired
	private MultiLangDao multiLangDao;

	public Base${entityName}DaoImpl() {
		super();
	}
	<#-- 定义一个变量uncapFirstEntityBean,并将"${entityBean}"?uncap_first赋值给它     定义一个变量capFirstEntityId,并将"${entityId}"?cap_first赋值给它-->
	<#assign uncapFirstEntityBean="${entityBean}"?uncap_first capFirstEntityId="${entityId}"?cap_first >
	@Override
	public int create(${entityBean} ${uncapFirstEntityBean})  {
		Preconditions.checkNotNull(${uncapFirstEntityBean});
		<#-- 遍历list中的数据 -->
		<#list components as compon>
		<#--如果输入类型为MULTLANG,则如下-->
		<#if compon.inputType =="MULTLANG">
		<#assign fieldNameOfMultiLang = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))>
		Preconditions.checkNotNull(${uncapFirstEntityBean}.get${"${fieldNameOfMultiLang}"?cap_first}Beans());
		</#if>
		</#list>	
		
		BigDecimal id = super.getNxSeqNextVal("${SequenceNm}");
		${uncapFirstEntityBean}.set${capFirstEntityId}(id.intValue());

		super.setEntityBeanCommFields(${uncapFirstEntityBean}, true);
		${uncapFirstEntityBean}.setRegCompCd(this.getLoginInfo().getCompCd());// 赋值登录公司编号  
		
		
		
		<#-- 遍历list中的数据 -->
		<#list components as compon>
		<#--如果输入类型为MULTLANG,则如下-->
		<#if compon.inputType =="MULTLANG">
		//存储多国语字段  
		<#-- 定义一个变量fieldNameOfMultiLang,并将"${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))赋值给它 ,注意list中的变量必须定义在循环体内,且每次循环即便是遍历同一lsit,在不同位置都需要重新定义,定义的结构可以相同-->
		<#assign fieldNameOfMultiLang = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))>
		String nmcd = multiLangDao.createOrUpdateNmBeans(
				 ${uncapFirstEntityBean}.get${"${fieldNameOfMultiLang}"?cap_first}(), ${uncapFirstEntityBean}.get${"${fieldNameOfMultiLang}"?cap_first}Beans(),
				 getLoginInfo().getLoginCompCd(),
				 "${compon.nameType}");
		${uncapFirstEntityBean}.set${"${compon.fieldName}"?cap_first}(nmcd);
		</#if>
		</#list>

		<#-- 定义一个变量lowerCasePojoNm,并将"${pojoNm}"?lower_case赋值给它 -->
		<#assign lowerCasePojoNm="${pojoNm}"?lower_case >
		// EntityBean存入DB
		${pojoNm} ${lowerCasePojoNm}Obj = new ${pojoNm}();
		ConvertUtils.map(${uncapFirstEntityBean}, ${lowerCasePojoNm}Obj);
		saveOrUpdate(${lowerCasePojoNm}Obj);
		${uncapFirstEntityBean}.set${capFirstEntityId}(${lowerCasePojoNm}Obj.get${capFirstEntityId}());

		// 返回实体Id
		return ${uncapFirstEntityBean}.get${capFirstEntityId}();
	}
	
	@Override
	public void update(${entityBean} ${uncapFirstEntityBean}) {
		Preconditions.checkNotNull(${uncapFirstEntityBean});
		Preconditions.checkNotNull(${uncapFirstEntityBean}.get${capFirstEntityId}());
		<#-- 遍历list中的数据 -->
		<#list components as compon>
		<#--如果输入类型为MULTLANG,则如下-->
		<#if compon.inputType =="MULTLANG">
		<#assign fieldNameOfMultiLang = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))>
		Preconditions.checkNotNull(${uncapFirstEntityBean}.get${"${fieldNameOfMultiLang}"?cap_first}Beans());
		</#if>
		</#list>
		
		// EntityBean存入DB
		${pojoNm} ${lowerCasePojoNm}Obj = (${pojoNm}) super.getObject(${pojoNm}.class,
				${uncapFirstEntityBean}.get${capFirstEntityId}());
		
		// 共通字段赋值
		setEntityBeanCommFields(${uncapFirstEntityBean}, false);
		${uncapFirstEntityBean}.setRegCompCd(this.getLoginInfo().getCompCd());// 赋值登录公司编号

		${uncapFirstEntityBean}.setCreateDt(${lowerCasePojoNm}Obj.getCreateDt());
		${uncapFirstEntityBean}.setCreateUserCd(${lowerCasePojoNm}Obj.getCreateUserCd());
		${uncapFirstEntityBean}.setCreateUserId(${lowerCasePojoNm}Obj.getCreateUserId());


		<#-- 遍历list中的数据 -->
		<#list components as compon>
		<#--如果输入类型为MULTLANG,则如下-->
		<#if compon.inputType =="MULTLANG">
		//存储多国语字段  
		<#-- 再一次定义变量fieldNameOfMultiLang,并将"${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))赋值给它 ,注意list中的变量必须定义在循环体内,且每次循环即便是遍历同一lsit,在不同位置都需要重新定义,定义的结构可以相同-->
		<#assign fieldNameOfMultiLang = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))>
		${uncapFirstEntityBean}.set${"${compon.fieldName}"?cap_first}(multiLangDao.createOrUpdateNmBeans(
				 ${uncapFirstEntityBean}.get${"${fieldNameOfMultiLang}"?cap_first}(), ${uncapFirstEntityBean}.get${"${fieldNameOfMultiLang}"?cap_first}Beans(),
				 getLoginInfo().getLoginCompCd(),
				 "${compon.nameType}"));
		</#if>
		</#list>
		ConvertUtils.map(${uncapFirstEntityBean}, ${lowerCasePojoNm}Obj);
		saveOrUpdate(${lowerCasePojoNm}Obj);
	}
	
	@Override
	public boolean checkCdExist(Integer ${entityId}, String ${bizKey}) {
		Preconditions.checkNotNull(${bizKey});
		String hql = " From ${pojoNm} m6 Where m6.${bizKey} = :${bizKey} ";
		Map<String, Object> map = createParamMap();
		map.put("${bizKey}", ${bizKey});
		// 更新
		if (${entityId} != null) {
			hql += " And m6.${entityId} <> :${entityId} ";
			map.put("${entityId}", ${entityId});
		}
		List<${pojoNm}> listObj = super.findByHQL(hql, map);
		if (listObj != null && listObj.size() > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public ${entityBean} get(int ${entityId}, String langCd) {
		Preconditions.checkNotNull(${entityId});
		Preconditions.checkNotNull(langCd);
		// 获取EntityBean信息
		${pojoNm} ${lowerCasePojoNm}Obj = (${pojoNm}) super.findById(${pojoNm}.class, ${entityId});
		${entityBean} bean = null;
		if (${lowerCasePojoNm}Obj != null) {
			bean = getBeanByPojo(${lowerCasePojoNm}Obj, super.getLoginInfo());
		}
		return bean;
	}
	
	protected ${entityBean} getBeanByPojo(${pojoNm} ${lowerCasePojoNm}, LoginInfoParam loginPara){
		${entityBean} bean = new ${entityBean}();
		// 获取EntityBean信息
		ConvertUtils.map(${lowerCasePojoNm}, bean);
		
		<#-- 遍历list中的数据 -->
		<#list components as compon>
		<#--如果输入类型为MULTLANG,则如下-->
		<#if compon.inputType =="MULTLANG">
		<#assign fieldNameOfMultiLang = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))>
		List<MultiLangBean> nmBeans = multiLangDao.getList(${lowerCasePojoNm}.get${"${compon.fieldName}"?cap_first}());
		bean.set${"${fieldNameOfMultiLang}"?cap_first}Beans(nmBeans);
		bean.set${"${fieldNameOfMultiLang}"?cap_first}(multiLangDao.getNmByNmBeans(bean.get${"${fieldNameOfMultiLang}"?cap_first}Beans(), loginPara.getUserLangCd()));
		</#if>
		</#list>
		
		return bean;
	}
	
	@Override
	public ${entityBean} getByCd(String ${bizKey}) {
		Preconditions.checkNotNull(${bizKey});
		StringBuffer hql = new StringBuffer();
		hql.append(" From ${pojoNm} m6 Where m6.${bizKey} = :${bizKey} ");
		Map<String, Object> map = createParamMap();
		map.put("${bizKey}", ${bizKey});
		List<${pojoNm}> listObj = super.findByHQL(hql.toString(), map);
		if (listObj != null) {
			${pojoNm} ${lowerCasePojoNm}Obj = CollectionsUtils.getFirst(listObj);
			${entityBean} bean = null;
			if (${lowerCasePojoNm}Obj != null) {
				bean = getBeanByPojo(${lowerCasePojoNm}Obj, super.getLoginInfo());
			}
			return bean;
		}
		return null;
	}
	
}