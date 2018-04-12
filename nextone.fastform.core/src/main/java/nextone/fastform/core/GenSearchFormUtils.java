package nextone.fastform.core;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;

/**
 * 自动生成一览画面(SearchForm)相关文件的工具类
 * @author jlzhuang
 */
@SuppressWarnings({ "rawtypes" })
public class GenSearchFormUtils {

	private static Logger logger = LoggerFactory.getLogger(GenSearchFormUtils.class);
	protected static Configuration cfg;
	protected static String partFilePathOfMasterXml=GenerateUtils.rootDirPath+"/TilesXml/";
	protected static String partFilePathOfJsp=GenerateUtils.rootDirPath+"/WebFormSource/webapp/WEB-INF/jsp/"+GenerateUtils.ModulePath;
	protected static String partFilePathOfDaoImpl=GenerateUtils.rootDirPath+"/WebFormSource/java/"+GenerateUtils.subAndChangeBeanPath;
	protected static String partFilePathOfJson=GenerateUtils.rootDirPath+"/WebFormSource/webapp/WEB-INF/queryview/def/";
	
	/**
	 * 自动生成一览画面jsp的静态方法
	 * @throws Exception 
	 */
	public static void genSearchFormJsp(Class clazz) throws Exception  {
		// 获取数据源
		Map data = GenerateUtils.getDataForSearchForm(clazz);
		String pgmCd = (String) data.get("searchFormPgmCd");
		String lowerCasePgmCd = pgmCd.toLowerCase();
		logger.info("数据获取成功");
		//根据传入的参数生成相应代码
		GenerateUtils.freeMarkerGenerFile(cfg, GenSearchFormUtils.class,
				"/searchFormDBGenTempl", "SearchFormJsp.ftl", partFilePathOfJsp+lowerCasePgmCd+"/"+pgmCd+".jsp", data);
	}
	
	/**
	 * 自动生成一览画面BaseDaoImpl的静态方法
	 * @throws Exception 
	 */
	public static void genBaseSearchFormDaoImpl(Class clazz,boolean subSearchFormDaoImplFlg) throws Exception {
		// 获取数据源
		Map data = GenerateUtils.getDataForSearchForm(clazz);
		String pgmCd = (String) data.get("searchFormPgmCd");
		//根据传入的参数生成相应代码      D:/Demo/WebAppMstms/java/nextone/master/searchformdao/
		GenerateUtils.freeMarkerGenerFile(cfg, GenSearchFormUtils.class,
				"/searchFormDBGenTempl", "BaseSearchFormDaoImpl.ftl", partFilePathOfDaoImpl+"searchformdao/Base"+pgmCd+"DaoImpl.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subSearchFormDaoImplFlg) {
			GenerateUtils.freeMarkerGenerFile(cfg, GenSearchFormUtils.class,
					"/searchFormDBGenTempl", "SearchFormDaoImpl.ftl", partFilePathOfDaoImpl+"searchformdao/"+pgmCd+"DaoImpl.java", data);
		}
	}
	
	/**
	 * 自动生成一览画面Def.SearchForm的JSON文件的静态方法
	 * @throws Exception 
	 */
	public static void genDefSearchFormJson(Class clazz) throws Exception {
		// 获取数据源
		Map data = GenerateUtils.getDataForSearchForm(clazz);
		String pgmCd = (String) data.get("searchFormPgmCd");
		String lowerCasePgmCd = pgmCd.toLowerCase();
		Object moduleCd = data.get("searchFormModuleCd");
		//根据传入的参数生成相应代码      D:\Demo\WebAppMstms\webapp\WEB-INF\queryview\def\dm\dm21
		GenerateUtils.freeMarkerGenerFile(cfg, GenSearchFormUtils.class,
				"/searchFormDBGenTempl", "Def_SearchFormJson.ftl", partFilePathOfJson+moduleCd+"/"+lowerCasePgmCd+"/"+pgmCd+".Def.SearchForm.json", data);
	}
	
	/**
	 * 生成SearchForm对应的masterDefinition的静态方法
	 * @throws Exception 
	 */
	public static void generMasterDefinition(Class clazz) throws Exception {
		// 获取数据源
		Map data = GenerateUtils.getDataForSearchForm(clazz);
		String pgmCd = (String) data.get("searchFormPgmCd");
		//根据传入的参数生成相应代码
		GenerateUtils.freeMarkerGenerFile(cfg, GenSearchFormUtils.class,
				"/searchFormDBGenTempl", "masterDefinition.ftl", partFilePathOfMasterXml+pgmCd+".tiles.xml", data);
	}

}