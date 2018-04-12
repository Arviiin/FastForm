	<#-- 生成master.mxl中关于JSP配置的模板  -->
	<#assign moduleCd=moduleCd pgmCd=pgmCd packageCd=packageCd lowerCasePgmCd="${pgmCd}"?lower_case>
	<definition name="${moduleCd}.${lowerCasePgmCd}_u" extends="comm.page.inputform">
		<put-attribute name="contentBody" value="/WEB-INF/jsp/master/${packageCd}/${pgmCd}_U.jsp" />
	</definition>
	
	<definition name="${moduleCd}.${lowerCasePgmCd}_r" extends="comm.page.inputform">
		<put-attribute name="contentBody" value="/WEB-INF/jsp/master/${packageCd}/${pgmCd}_R.jsp" />
	</definition>