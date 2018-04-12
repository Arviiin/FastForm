package ${prePackage}.bean;

import java.util.List;
import lombok.Data;
import nextone.framework.bean.BaseEntityBean;
import org.directwebremoting.annotations.DataTransferObject;
import com.google.common.collect.Lists;

/**
 * 学生信息Bean.
 * @author jlzhuang
 */
@Data
@DataTransferObject
public class ${entityBean} extends BaseEntityBean {
	<#-- 遍历list中的数据 -->
	 <#list components as compon>
	 <#if compon.readOnly=="false">
	 private ${compon.fieldType} ${compon.fieldName};
	 </#if>
     </#list>
	 private String stuMotherLangNm;
	 private String stuDesc;
	 private String stuSexNm;
	 private List<MultiLangBean> stuDescBeans = Lists.newArrayList();
	 
}