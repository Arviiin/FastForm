package ${prePackage}.pojo;
import java.util.Date;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *POJO:${pojoNm}
 */
@Entity
@Table(name="${"${pojoNm}"?upper_case}")
public class ${pojoNm}  implements java.io.Serializable {
	 <#-- 遍历list中的数据 -->
	 <#list components as compon>
	 <#-- 只要输入字段不是createUserNm和updateUserNm,则如下 -->
	 <#if compon.fieldName != "createUserNm" && compon.fieldName != "updateUserNm">
	 private ${compon.fieldClassSimpleName} ${compon.fieldName};
	 </#if>
	 </#list>
	
    public ${pojoNm}() {
    }

	<#-- 遍历list中的数据 -->
	<#list components as compon>
	<#-- 只要输入字段不是createUserNm和updateUserNm,则如下 -->
	<#if compon.fieldName != "createUserNm" && compon.fieldName != "updateUserNm">
	<#-- 若输入类型是HIDDEN,或者以Id结尾,则打印相应代码 -->
	<#if compon.inputType =="HIDDEN" || "${compon.fieldName}"?ends_with("Id")>
	<#--如果字段是主键,则如下-->
	<#if entityId==compon.fieldName>
	@Id
	</#if>
	<#include "/PojoItem01.ftl"> unique=${compon.unique?string("true","false")}, nullable=${compon.nullable?string("true","false")}, precision=${compon.precision}, scale=${compon.scale})
	<#--如果字段是DATETIME类型,则如下-->
	<#elseif compon.inputType == "DATETIME" || compon.inputType == "DATETIME">
	@Temporal(TemporalType.TIMESTAMP)
	<#include "/PojoItem01.ftl"> nullable=${compon.nullable?string("true","false")})
	<#--剩下的只要输入类型不为TEXT,则如下-->
	<#else >
	<#include "/PojoItem01.ftl"> nullable=${compon.nullable?string("true","false")}, length=${compon.maxlength})
	</#if>
	public ${compon.fieldClassSimpleName} get${"${compon.fieldName}"?cap_first}() {
        return this.${compon.fieldName};
    }
    public void set${"${compon.fieldName}"?cap_first}(${compon.fieldClassSimpleName} ${compon.fieldName}) {
        this.${compon.fieldName} = ${compon.fieldName};
    }
    </#if>
	</#list>
    
    <#-- 遍历list中的数据 -->
	<#list components as compon>
	<#--如果输入类型为MULTLANG,则如下-->
	<#if compon.inputType =="MULTLANG">
	<#assign fieldNameOfMultiLang = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Cd"))>
    private Mat005Mto ${fieldNameOfMultiLang}MultiLang;
    @ManyToOne(fetch = FetchType.LAZY,optional=true)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "${"${compon.fieldDBName}"?upper_case}", insertable = false, updatable = false)
	public Mat005Mto get${"${fieldNameOfMultiLang}"?cap_first}MultiLang() {
		return ${fieldNameOfMultiLang}MultiLang;
	}
	public void set${"${fieldNameOfMultiLang}"?cap_first}MultiLang(Mat005Mto ${fieldNameOfMultiLang}MultiLang) {
		this.${fieldNameOfMultiLang}MultiLang = ${fieldNameOfMultiLang}MultiLang;
	}
	</#if>
	</#list>

	<#-- 遍历list中的数据 -->
	<#list components as compon>
	<#-- 当字段是createUserId或者updateUserId时执行下面代码 -->
	<#if compon.fieldName == "createUserId" || compon.fieldName == "updateUserId" >
	<#assign subFieldNameOfId = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Id"))>
	private Mat002 ${subFieldNameOfId};
	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "${"${compon.fieldDBName}"?upper_case}", insertable = false, updatable = false)
	public Mat002 get${"${subFieldNameOfId}"?cap_first}() {
		return ${subFieldNameOfId};
	}
	public void set${"${subFieldNameOfId}"?cap_first}(Mat002 ${subFieldNameOfId}) {
		this.${subFieldNameOfId} = ${subFieldNameOfId};
	}
	</#if>
	</#list>
	
	
	<#-- 遍历list中的数据   -->
	<#list components as compon>
	<#if compon.inputType == "SELECT">
	<#if compon.manyToOnePojo?exists><#-- 当字段的manyToOnePojo存在时,执行下面代码 -->
	<#assign subFieldNameOfManyToOneId = "${compon.fieldName}"?substring(0,"${compon.fieldName}"?index_of("Id"))>
	private ${compon.manyToOnePojo} ${subFieldNameOfManyToOneId};
	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "${"${compon.fieldDBName}"?upper_case}", insertable = false, updatable = false)
	public ${compon.manyToOnePojo} get${"${subFieldNameOfManyToOneId}"?cap_first}() {
		return ${subFieldNameOfManyToOneId};
	}
	public void set${"${subFieldNameOfManyToOneId}"?cap_first}(${compon.manyToOnePojo} ${subFieldNameOfManyToOneId}) {
		this.${subFieldNameOfManyToOneId} = ${subFieldNameOfManyToOneId};
	}
	<#elseif compon.typeCd != ""><#--如果下拉框取的大分类,则如下-->
	private Mat004Mto ${compon.fieldName}TypeDtl;
	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "${"${compon.fieldDBName}"?upper_case}",insertable = false, updatable = false)
	public Mat004Mto get${"${compon.fieldName}"?cap_first}TypeDtl() {
		return ${compon.fieldName}TypeDtl;
	}
	public void set${"${compon.fieldName}"?cap_first}TypeDtl(Mat004Mto ${compon.fieldName}TypeDtl) {
		this.${compon.fieldName}TypeDtl = ${compon.fieldName}TypeDtl;
	}
	</#if>
	</#if>
	</#list>
	
	
	
}