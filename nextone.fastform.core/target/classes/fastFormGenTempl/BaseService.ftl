<#-- 生成画面Service(接口)基类的模板  -->
<#-- 定义一些变量  -->
<#assign prePackage=prePackage pgmFormBean=pgmFormBean>
package ${prePackage}.service;

import nextone.framework.service.UIService;
import ${prePackage}.bean.${pgmFormBean};

public interface Base${pgmService} extends UIService {

    public void initCreate(${pgmFormBean} bean);
    
    public void create(${pgmFormBean} bean);
    
    public void initUpdate(${pgmFormBean} bean);
    
    public void update(${pgmFormBean} bean);
   
    public ${pgmFormBean} initRead(${pgmFormBean} bean);
}