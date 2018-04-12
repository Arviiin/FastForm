			<label id="${compon.labelId}" <#include "/InputItem.ftl">><spring:message
					code="label.${compon.labelMsgCd}" /></label>
			<div class="col-sm-2 clearfix">
				<span class="form-label" id="${compon.spanId}"><c:out value="${r"$"}{${compon.formInOrOutputPath}}" /></span>
				<!-- 多国语按钮 -->
				<am:AM23 listMultiLangJson="${"${compon.entityBean}"?uncap_first}.${compon.fieldName}Beans"
					 nameType="${compon.nameType}" readOnly="true"></am:AM23> 
			</div>