			else if (formval.formMode == 'UPDATE_MODE') {
				console.info(formval);
				if (confirm('<spring:message code="confirm.update"/>')) {
					${pgmCd}Controller.update(formval, function(returnData) {
						//call onSubmited handle function
						if (returnData.resultCd == 'SUCCESS') {
							var ${formId} = returnData.dataMap['${formId}'];
							window.location = "${r"${ctx}"}/${moduleCd}/${"${pgmCd}"?lower_case}/initRead/"
									+ ${formId};
						} else {
							showMsg(returnData);
						}
					});
				}
			}