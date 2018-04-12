			<label id="${compon.labelId}" class="col-sm-2 control-label date"><spring:message
					code="label.${compon.labelMsgCd}" /></label>
			<div class="col-sm-2 clearfix">
				<span class="form-label" id="${compon.spanId}"><fmt:formatDate value="${r"$"}{${compon.formInOrOutputPath}}" pattern="yyyy/MM/dd HH:mm:ss" />
				</span>
			</div>