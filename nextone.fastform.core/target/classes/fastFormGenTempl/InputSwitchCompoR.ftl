			<label id="${compon.labelId}" <#include "/InputItem.ftl">><spring:message
					code="label.${compon.labelMsgCd}" /></label>
			<div class="col-sm-2 clearfix">
				<!-- 大分类读取有一点不同 c:out value="$中的值一般以Nm结尾(在companyBean中定义)与code中的名字不一致-->
				<span class="form-label" id="${compon.spanId}"><c:out value="${r"$"}{${compon.formInOrOutputPath}Nm}" /></span>
			</div>