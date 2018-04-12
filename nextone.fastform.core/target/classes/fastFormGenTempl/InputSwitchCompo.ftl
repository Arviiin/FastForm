			<label for="${compon.labelForComponentId}" <#include "/InputItem.ftl">><#if compon.must><n:must/></#if><spring:message
					code="label.${compon.labelMsgCd}" /></label>
			<div class="col-sm-2">
				<form:checkbox class="switches" path="${compon.formInOrOutputPath}"  
					id="${compon.labelForComponentId}" 
					data-on-text="${r"$"}{n:getText('${compon.onText}') }"
					data-off-text="${r"$"}{n:getText('${compon.offText}')}" data-size="small" value="${r"$"}{formbean.${compon.entityBean}.${compon.labelMsgCd}==\"1\"}"/>
			</div>