package ${prePackage}.bean;
import java.util.List;
import lombok.Data;
import nextone.framework.bean.CRUDFormBean;
import nextone.framework.bean.LabelValueBean;
import nextone.framework.bean.UploadFileBean;
import org.directwebremoting.annotations.DataTransferObject;
import com.google.common.collect.Lists;
/**
 * 学生设定 Bean 测试自动生成
 * @author jlzhuang
 */
@Data
@DataTransferObject
public class ${pgmCd}FormBean extends CRUDFormBean{
private Integer ${formId};
private ${entityBean} ${beanFieldOfFormBeanName}=new ${entityBean}();
<#list components as compon>
<#if compon.inputType=="SELECT">
private List<TypeDetailBean> list${"${compon.fieldName}"?cap_first}=Lists.newArrayList();
</#if>
</#list>
}