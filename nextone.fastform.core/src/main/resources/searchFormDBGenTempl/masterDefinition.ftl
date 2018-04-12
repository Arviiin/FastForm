	<definition name="${searchFormModuleCd}.${"${searchFormPgmCd}"?lower_case}" extends="comm.page.inputform">
        <put-attribute name="contentBody">
            <definition template="/WEB-INF/jsp/common/searchForm.jsp">
              <put-attribute name="searchFormImpl" value="/WEB-INF/jsp/master/${searchFormPackageCd}/${searchFormPgmCd}.jsp" />
            </definition>
        </put-attribute>
    </definition>