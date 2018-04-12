package nextone.fastform.xmlcore;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;

/**
 * 自动生成一览画面(SearchForm)相关文件的工具类
 * @author jlzhuang
 */
@SuppressWarnings({ "rawtypes" })
public class TestGenSearchFormUtils {

	private static Logger logger = LoggerFactory.getLogger(TestGenSearchFormUtils.class);
	protected static Configuration cfg;
	protected static String partFilePathOfMasterXml=TestGenerateUtils.rootDirPath+"/TilesXml/";
	protected static String partFilePathOfJsp=TestGenerateUtils.rootDirPath+"/WebFormSource/webapp/WEB-INF/jsp/"+TestGenerateUtils.ModulePath;
	protected static String partFilePathOfDaoImpl=TestGenerateUtils.rootDirPath+"/WebFormSource/java/"+TestGenerateUtils.subAndChangeBeanPath;
	protected static String partFilePathOfJson=TestGenerateUtils.rootDirPath+"/WebFormSource/webapp/WEB-INF/queryview/def/";
	/**
	 * 自动生成一览画面jsp的静态方法
	 * @throws Exception 
	 */
	public static void genSearchFormJsp(String XmlPath) throws Exception  {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForSearchForm(XmlPath);
		String pgmCd = (String) data.get("searchFormPgmCd");
		String lowerCasePgmCd = pgmCd.toLowerCase();
		logger.info("数据获取成功");
		//根据传入的参数生成相应代码
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenSearchFormUtils.class,
				"/searchFormDBGenTempl", "SearchFormJsp.ftl", partFilePathOfJsp+lowerCasePgmCd+"/"+pgmCd+".jsp", data);
	}
	
	/**
	 * 自动生成一览画面BaseDaoImpl的静态方法
	 * @throws Exception 
	 */
	public static void genBaseSearchFormDaoImpl(String XmlPath,boolean subSearchFormDaoImplFlg) throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForSearchForm(XmlPath);
		String pgmCd = (String) data.get("searchFormPgmCd");
		//根据传入的参数生成相应代码      D:/Demo/WebAppMstms/java/nextone/master/searchformdao/
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenSearchFormUtils.class,
				"/searchFormDBGenTempl", "BaseSearchFormDaoImpl.ftl", partFilePathOfDaoImpl+"searchformdao/Base"+pgmCd+"DaoImpl.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subSearchFormDaoImplFlg) {
			TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenSearchFormUtils.class,
					"/searchFormDBGenTempl", "SearchFormDaoImpl.ftl", partFilePathOfDaoImpl+"searchformdao/"+pgmCd+"DaoImpl.java", data);
		}
	}
	
	/**
	 * 自动生成一览画面Def.SearchForm的JSON文件的静态方法
	 * @throws Exception 
	 */
	public static void genDefSearchFormJson(String XmlPath) throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForSearchForm(XmlPath);
		String pgmCd = (String) data.get("searchFormPgmCd");
		String lowerCasePgmCd = pgmCd.toLowerCase();
		Object moduleCd = data.get("searchFormModuleCd");
		//根据传入的参数生成相应代码      D:\Demo\WebAppMstms\webapp\WEB-INF\queryview\def\dm\dm21
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenSearchFormUtils.class,
				"/searchFormDBGenTempl", "Def_SearchFormJson.ftl", partFilePathOfJson+moduleCd+"/"+lowerCasePgmCd+"/"+pgmCd+".Def.SearchForm.json", data);
	}
	
	/**
	 * 生成SearchForm对应的masterDefinition的静态方法
	 * @throws Exception 
	 */
	public static void generMasterDefinition(String XmlPath) throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForSearchForm(XmlPath);
		String pgmCd = (String) data.get("searchFormPgmCd");
		//根据传入的参数生成相应代码
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenSearchFormUtils.class,
				"/searchFormDBGenTempl", "masterDefinition.ftl", partFilePathOfMasterXml+pgmCd+".tiles.xml", data);
	}
public static void generSearchFormBean(String XmlPath) throws Exception {
	    Map data = TestGenerateUtils.getDataForSearchForm(XmlPath);
	    String pgmCd = (String) data.get("searchFormPgmCd");
	    TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenSearchFormUtils.class,
				"/searchFormDBGenTempl", "SearchFormBean.ftl", partFilePathOfDaoImpl+"bean/"+pgmCd+"FormBean.java", data);
}
}