			<label id="${compon.labelId}" <#include "/InputItem.ftl">><spring:message
					code="label.${compon.labelMsgCd}" /></label>
			<div class="col-sm-2 clearfix">
				<span class="form-label" id="${compon.spanId}"><c:out value="${r"$"}{${compon.formInOrOutputPath}}" /></span>
			</div>