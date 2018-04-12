			<label for="${compon.labelForComponentId}" class="col-sm-2 control-label date"><spring:message
					code="label.${compon.labelMsgCd}" /></label>
			<div class="col-sm-2"> 
				<form:input path="${compon.formInOrOutputPath}" class="form-control input_md input-sm date " 
					id="${compon.labelForComponentId}" readonly="${compon.readOnly}"/>
			</div>	