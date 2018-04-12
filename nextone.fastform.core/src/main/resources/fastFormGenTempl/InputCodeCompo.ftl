			<label for="${compon.labelForComponentId}" <#include "/InputItem.ftl">><#if compon.must><n:must/></#if><spring:message
					code="label.${compon.labelMsgCd}" /></label>
			<div class="col-sm-2"> 
				<form:input path="${compon.formInOrOutputPath}" class="form-control <#if compon.upperCase>upperCase</#if>" 
					id="${compon.labelForComponentId}" maxlength="${compon.maxlength}" readonly="${compon.readOnly}"/>
			</div>