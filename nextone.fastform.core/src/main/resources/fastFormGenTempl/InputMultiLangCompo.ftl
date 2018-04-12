			<label for="${compon.labelForComponentId}" <#include "/InputItem.ftl">><#if compon.must><n:must/></#if><spring:message
					code="label.${compon.labelMsgCd}" /></label>
			<div class="col-sm-2">
				<div class="input-group"  id="test">
					<form:input path="${compon.formInOrOutputPath}" class="form-control <#if compon.upperCase>upperCase</#if>" 
					id="${compon.labelForComponentId}" maxlength="${compon.maxlength}" readonly="${compon.readOnly}"/>
					<!-- 多国语按钮 -->
			 		<am:AM23 listMultiLangJson="${"${compon.entityBean}"?uncap_first}.${compon.fieldName}Beans" 
			 			nameType="${compon.nameType}"></am:AM23> 
				</div> 	
			</div>