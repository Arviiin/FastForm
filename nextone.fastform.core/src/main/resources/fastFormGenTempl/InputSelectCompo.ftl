			<label for="${compon.labelForComponentId}" <#include "/InputItem.ftl">><#if compon.must><n:must/></#if><spring:message
					code="label.${compon.labelMsgCd}" /></label>
			<div class="col-sm-2">
				<form:select path="${compon.formInOrOutputPath}"
					items="${r"$"}{formbean.list${"${compon.fieldName}"?cap_first}}"
					<#if compon.lblValBeanService == "">
					itemValue="detailCd"
					itemLabel="detailNm1"
					<#elseif compon.lblValBeanService != "">
					itemValue="value"
					itemLabel="label"
					</#if>
					id="${compon.labelForComponentId}"
					class="form-control" /> 
			</div>