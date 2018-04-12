			if (formval.formMode == 'CREATE_MODE') {
				if (confirm('<spring:message code="confirm.create"/>')) {
					${pgmCd}Controller.create(formval, function(returnData) {
					
						//call onSubmited handle function
						if (returnData.resultCd == 'SUCCESS') {
							var ${formId} = returnData.dataMap['${formId}'];
							window.location = "${r"${ctx}"}/${moduleCd}/${"${pgmCd}"?lower_case}/initRead/"+ ${formId};
						} else {
							showMsg(returnData);
						}
					});
				}
			} 