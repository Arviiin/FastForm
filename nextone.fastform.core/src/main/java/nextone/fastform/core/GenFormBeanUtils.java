package nextone.fastform.core;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;

/**
 * 自动生成FormBean相关文件的工具类
 * @author jlzhuang
 */
@SuppressWarnings({ "rawtypes" })
public class GenFormBeanUtils {

	private static Logger logger = LoggerFactory.getLogger(GenFormBeanUtils.class);
	protected static Configuration cfg;
	protected static String partFilePathOfMasterXml=GenerateUtils.rootDirPath+"/TilesXml/";
	protected static String partFilePathOfJsp=GenerateUtils.rootDirPath+"/WebFormSource/webapp/WEB-INF/jsp/"+GenerateUtils.ModulePath;
	protected static String partFilePathRelFormBean=GenerateUtils.rootDirPath+"/WebFormSource/java/"+GenerateUtils.subAndChangeBeanPath;

	/**
	 * 自动生成_U.jsp的静态方法
	 */
	public static void generU_Jsp(Class clazz) {
		// 获取数据源
		Map data = GenerateUtils.getDataForFormBean(clazz);
		String pgmCd = (String) data.get("pgmCd");
		String lowerCasePgmCd = pgmCd.toLowerCase();
		logger.info("pgmCd is :"+pgmCd);
		//根据传入的参数生成相应代码
		GenerateUtils.freeMarkerGenerFile(cfg, GenFormBeanUtils.class,
				"/fastFormGenTempl", "crud_UJsp.ftl", partFilePathOfJsp+lowerCasePgmCd+"/"+pgmCd+"_U.jsp", data);
	}

	/**
	 * 自动生成_R.jsp的静态方法
	 */
	public static void generR_Jsp(Class clazz) {
		// 获取数据源
		Map data = GenerateUtils.getDataForFormBean(clazz);
		String pgmCd = (String) data.get("pgmCd");
		String lowerCasePgmCd = pgmCd.toLowerCase();
		//根据传入的参数生成相应代码
		GenerateUtils.freeMarkerGenerFile(cfg, GenFormBeanUtils.class,
				"/fastFormGenTempl", "crud_RJsp.ftl", partFilePathOfJsp+lowerCasePgmCd+"/"+pgmCd+"_R.jsp", data);
	}

	/**
	 * 生成masterDefinition的静态方法
	 * 
	 * @throws Exception
	 */
	public static void generMasterDefinition(Class clazz) throws Exception {
		// 获取数据源
		Map data = GenerateUtils.getDataForFormBean(clazz);
		String pgmCd = (String) data.get("pgmCd");
		//根据传入的参数生成相应代码
		GenerateUtils.freeMarkerGenerFile(cfg, GenFormBeanUtils.class,
				"/fastFormGenTempl", "masterDefinition.ftl", partFilePathOfMasterXml+pgmCd+".tiles.xml", data);
	}
	
	/**
	 * 自动生成BaseAM60_01Controller的静态方法,同时根据传入的参数判断是否要生成其子类
	 * @throws Exception
	 */
	public static void generBaseController(Class clazz, boolean subControllerFlg)throws Exception {
		// 获取数据源
		Map data = GenerateUtils.getDataForFormBean(clazz);
		String pgmCd = (String) data.get("pgmCd");
		//根据传入的参数生成相应代码             D:/Demo/WebAppMstms/java/nextone/master/controller/
		GenerateUtils.freeMarkerGenerFile(cfg, GenFormBeanUtils.class,
						"/fastFormGenTempl", "BaseController.ftl",   partFilePathRelFormBean+"controller/Base"+pgmCd+"Controller.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subControllerFlg) {
			GenerateUtils.freeMarkerGenerFile(cfg, GenFormBeanUtils.class,
					"/fastFormGenTempl", "Controller.ftl", partFilePathRelFormBean+"controller/"+pgmCd+"Controller.java", data);
		}
	}

	/**
	 * 生成BaseAM60_01Service的静态方法,同时根据其传入的参数判断是否要生成其子类
	 */
	public static void generBaseService(Class clazz, boolean subServiceFlg) {
		// 获取数据源
		Map data = GenerateUtils.getDataForFormBean(clazz);
		String pgmCd = (String) data.get("pgmCd");
		//根据传入的参数生成相应代码     
		GenerateUtils.freeMarkerGenerFile(cfg, GenFormBeanUtils.class,
						"/fastFormGenTempl", "BaseService.ftl", partFilePathRelFormBean+"service/Base"+pgmCd+"Service.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subServiceFlg) {
			GenerateUtils.freeMarkerGenerFile(cfg, GenFormBeanUtils.class,
					"/fastFormGenTempl", "Service.ftl", partFilePathRelFormBean+"service/"+pgmCd+"Service.java", data);
		}
	}
	/**
	 * 生成BaseAM60_01ServiceImpl的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * 
	 * @throws Exception
	 */
	public static void generBaseServiceImpl(Class clazz,boolean subServiceImplFlg) throws Exception {
		// 获取数据源
		Map data = GenerateUtils.getDataForFormBean(clazz);
		String pgmCd = (String) data.get("pgmCd");
		//根据传入的参数生成相应代码     
		GenerateUtils.freeMarkerGenerFile(cfg, GenFormBeanUtils.class,
						"/fastFormGenTempl", "BaseServiceImpl.ftl", partFilePathRelFormBean+"service/impl/Base"+pgmCd+"ServiceImpl.java", data);
		if (subServiceImplFlg) {
			GenerateUtils.freeMarkerGenerFile(cfg, GenFormBeanUtils.class,
					"/fastFormGenTempl", "ServiceImpl.ftl", partFilePathRelFormBean+"service/impl/"+pgmCd+"ServiceImpl.java", data);
		}
	}

	/**
	 * 生成BaseFormBeanValidator的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * @throws Exception
	 */
	public static void generBaseFormBeanValidator(Class clazz,boolean subFormBeanValidatorFlg) throws Exception {
		// 获取数据源
		Map data = GenerateUtils.getDataForFormBean(clazz);
		String pgmCd = (String) data.get("pgmCd");
		//根据传入的参数生成相应代码       
		GenerateUtils.freeMarkerGenerFile(cfg, GenFormBeanUtils.class,
						"/fastFormGenTempl", "BaseFormBeanValidator.ftl", partFilePathRelFormBean+"validator/Base"+pgmCd+"FormBeanValidator.java", data);
		if (subFormBeanValidatorFlg) {
			GenerateUtils.freeMarkerGenerFile(cfg, GenFormBeanUtils.class,
					"/fastFormGenTempl", "FormBeanValidator.ftl", partFilePathRelFormBean+"validator/"+pgmCd+"FormBeanValidator.java", data);
		}
	}
}