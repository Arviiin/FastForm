<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script src='${r"$"}{ctx}/dwr/interface/${crudPgmCd}Controller.js'
	type='text/javascript'></script>
	
<script type="text/javascript">
	//返回检索结果列中的id key字段名
	function getIdColNm(){
		return "${entityId}";
	}
	
	//返回检索结果固定列定义
	function getListFixedCols(){
		return {
			colNames : ['${entityId}'],
			colModel : [ {
					name : '${entityId}',
					width : 0,
					hidden : true
			} ]
		};
	}
	
	function initGrid() {
		setColModelOptions('${bizKey}', {
			formatter : 'textparamlink',
			formatoptions : {
				//从一栏画面跳转到查看画面的地址
				baseLinkUrl : ctx + '/${crudModuleCd}/${"${crudPgmCd}"?lower_case}/initRead/{${entityId}}?'
						+ strNavPushed
			}
		});
		baseInitGrid();
		//初始化明细表格按钮条
		if (formbean.formMode == 'normal') {
			//明细表格导航栏按钮设置
			$("#list").navGrid('#list_toppager', {
				view : false,
				del : false,
				add : true,
				edit : true,
				search : false,
				addtext : '<spring:message code= "btn.new"/>',
				addfunc : addFunction,
				edittext : '<spring:message code= "btn.edit"/>',
				editfunc : editFunction,
				deltext : '<spring:message code= "btn.delete"/>',
				delfunc : removeFunction
			}, {}, // use default settings for edit
			{}, // use default settings for add
			{}, // delete instead that del:false we need this
			{}, // enable the advanced searching
			{} /* allow the view dialog to be closed when user press ESC key*/
			)
		}
	}//function initGrid end
	
	//新建
	function addFunction(e) {
		var trigUrl = "/${crudModuleCd}/${"${crudPgmCd}"?lower_case}/initCreate";
		window.location = ctx + trigUrl + "?" + strNavPushed;
	}
	
	//删除
	function removeFunction() {
		if (confirm('<spring:message code="confirm.remove"/>')) {
			var selrows = jQuery("#list").jqGrid('getGridParam', 'selarrrow');
			AM49Controller.remove(selrows, function(returnData) {
				if (returnData.resultCd == 'SUCCESS') {
					$("#list").trigger("reloadGrid");
				}
				showMsg(returnData);
			});
		}
	}
	
	//修改
	function editFunction() {
		var selrows = jQuery("#list").jqGrid('getGridParam', 'selarrrow');
		if (selrows.length == 0) {
			$nextOne.alert($.jgrid.errors.norecords);
			return false;
		} else if (selrows.length > 1) {
			$nextOne.alert('<spring:message code="warn.maxselectcount1" />');
			return false;
		} else {
			var menuItemId = selrows[0];
			window.location = ctx + "/${crudModuleCd}/${"${crudPgmCd}"?lower_case}/initUpdate/" + menuItemId + "?"
				+ strNavPushed;
		}
	}
</script> 