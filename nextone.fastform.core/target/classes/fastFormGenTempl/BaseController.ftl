<#-- 生成画面Controller的模板  -->
<#-- 定义一些变量-->
<#assign prePackage=prePackage pgmCd=pgmCd moduleCd=moduleCd pgmFormBean=pgmFormBean formId=formId
		 lowerCasePgmCd="${pgmCd}"?lower_case>
package ${prePackage}.controller;

import lombok.Setter;
import nextone.common.MstConstParam;
import nextone.framework.bean.FormMode;
import nextone.framework.bean.ReturnData;
import nextone.framework.bean.ReturnData.ResultCd;
import nextone.framework.controller.BizController;
import org.directwebremoting.annotations.RemoteMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ${prePackage}.bean.${pgmFormBean};
import ${prePackage}.service.${pgmService};
import ${prePackage}.validator.${pgmFormBeanValidator};

public class Base${pgmController} extends BizController {
	
	public static final String URL_${pgmCd} = "/${moduleCd}/${lowerCasePgmCd}";
	public static final String ${pgmCd}_U = "${moduleCd}.${lowerCasePgmCd}_u";
	public static final String ${pgmCd}_R = "${moduleCd}.${lowerCasePgmCd}_r";

	public Base${pgmController}() { }
	
	@Autowired
	@Setter
	private ${pgmService} service;
	
	@Autowired
	@Setter
	private ${pgmFormBeanValidator} formBeanValidator;

	@RequestMapping(value = URL_${pgmCd} + MstConstParam.INIT_CREATE, method = RequestMethod.GET)
	public ModelAndView initCreate(${pgmFormBean} formbean) {
		try {
			formbean.setFormMode(FormMode.CREATE_MODE);
			service.initCreate(formbean);
			ModelAndView mv = new ModelAndView();
			mv.addObject(MstConstParam.FORMBEAN, formbean);
			mv.setViewName(${pgmCd}_U);
			return mv;
		} catch (Exception e) {
			return super.errorProcModelAndView(e);
		}
	}
	
	@RemoteMethod
	public ReturnData create(${pgmFormBean} formbean) throws Exception {
		try {
			ReturnData returnData = new ReturnData();
			DataBinder binder = new DataBinder(formbean);
			binder.setValidator(formBeanValidator);
			// validate the target object
			binder.validate();
			// get BindingResult that includes any validation errors
			BindingResult bindRslt = binder.getBindingResult();
			returnData.setBindingResult(bindRslt, super.getLoginInfo()
					.getUserLocale());
			if(returnData.getResultCd().equals(ResultCd.SUCCESS)){
				/** 保存. */
				service.create(formbean);
				returnData.setOkMsgCd("ok.create", getLoginInfo().getUserLocale());
				returnData.addDataMap("${formId}", formbean.get${entityBean}().get${'${formId}'?cap_first}());
				// 将成功消息放入session，在查看画面中读取并显示
				setFlashReturnDataParam(returnData);
			}
			return returnData;
		} catch (Exception e) {
			return super.errorProcReturnData(e);
		}
	}
	
	@RequestMapping(value = URL_${pgmCd} + MstConstParam.INIT_UPDATE
			+ "/{${formId}}", method = RequestMethod.GET)
	public ModelAndView initUpdate(${pgmFormBean} formbean) {
		try {
			formbean.setFormMode(FormMode.UPDATE_MODE);
			service.initUpdate(formbean);
			ModelAndView mv = new ModelAndView();
			mv.addObject(MstConstParam.FORMBEAN, formbean);
			mv.setViewName(${pgmCd}_U);
			return mv;
		} catch (Exception e) {
			return super.errorProcModelAndView(e);
		}
	}
	
	@RequestMapping(value = URL_${pgmCd} + MstConstParam.INIT_READ
			+ "/{${formId}}", method = RequestMethod.GET)
	public ModelAndView initRead(${pgmFormBean} formbean) {
		try {
			ModelAndView mv = new ModelAndView();
			ReturnData returnData = null;

			formbean.setFormMode(FormMode.READ_MODE);
			service.initRead(formbean);
			if (formbean.get${entityBean}() == null) {
				return super.errorProcModelAndView("error.datanotfound");
			}
			mv.addObject(MstConstParam.FORMBEAN, formbean);
			// 编辑画面传来的提示消息
			returnData = getFlashReturnDataParam();
			mv.addObject(MstConstParam.RETURN_DATA, returnData);
			mv.setViewName(${pgmCd}_R);
			return mv;

		} catch (Exception e) {
			return super.errorProcModelAndView(e);
		}
	}
	
	@RemoteMethod
	public ReturnData update(${pgmFormBean} formbean) throws Exception {
		try {
			ReturnData returnData = new ReturnData();
			DataBinder binder = new DataBinder(formbean);
			binder.setValidator(formBeanValidator);
			// validate the target object
			binder.validate();
			// get BindingResult that includes any validation errors
			BindingResult bindRslt = binder.getBindingResult();
			returnData.setBindingResult(bindRslt, super.getLoginInfo()
					.getUserLocale());
			if(returnData.getResultCd().equals(ResultCd.SUCCESS)){
				/** 保存. */
				service.update(formbean);
				returnData.setOkMsgCd("ok.update", getLoginInfo().getUserLocale());
				returnData.addDataMap("${formId}", formbean.get${entityBean}().get${'${formId}'?cap_first}());
				// 将成功消息放入session，在查看画面中读取并显示
				setFlashReturnDataParam(returnData);
			}
			return returnData;
		} catch (Exception e) {
			return super.errorProcReturnData(e);
		}
	}
}