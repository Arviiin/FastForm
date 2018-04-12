package nextone.fastform.xmlcore;

import java.util.Map;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;

/**
 * 自动生成FormBean相关文件的工具类
 * @author jlzhuang
 */
@SuppressWarnings({ "rawtypes" })
public class TestGenFormBeanUtils {

	private static Logger logger = LoggerFactory.getLogger(TestGenFormBeanUtils.class);
	protected static Configuration cfg;
	protected static String partFilePathOfMasterXml=TestGenerateUtils.rootDirPath+"/TilesXml/";
	protected static String partFilePathOfJsp=TestGenerateUtils.rootDirPath+"/WebFormSource/webapp/WEB-INF/jsp/"+TestGenerateUtils.ModulePath;
	protected static String partFilePathRelFormBean=TestGenerateUtils.rootDirPath+"/WebFormSource/java/"+TestGenerateUtils.subAndChangeBeanPath;

	/**
	 * 自动生成_U.jsp的静态方法
	 * @throws DocumentException 
	 */
	public static void generU_Jsp(String XmlPath) throws DocumentException {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForFormBean(XmlPath);
		String pgmCd = (String) data.get("pgmCd");
		String lowerCasePgmCd = pgmCd.toLowerCase();
		logger.info("pgmCd is :"+pgmCd);
		//根据传入的参数生成相应代码
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenFormBeanUtils.class,
				"/fastFormGenTempl", "crud_UJsp.ftl", partFilePathOfJsp+lowerCasePgmCd+"/"+pgmCd+"_U.jsp", data);
	}

	/**
	 * 自动生成_R.jsp的静态方法
	 * @throws DocumentException 
	 */
	public static void generR_Jsp(String XmlPath) throws DocumentException {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForFormBean(XmlPath);
		String pgmCd = (String) data.get("pgmCd");
		String lowerCasePgmCd = pgmCd.toLowerCase();
		//根据传入的参数生成相应代码
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenFormBeanUtils.class,
				"/fastFormGenTempl", "crud_RJsp.ftl", partFilePathOfJsp+lowerCasePgmCd+"/"+pgmCd+"_R.jsp", data);
	}

	/**
	 * 生成masterDefinition的静态方法
	 * 
	 * @throws Exception
	 */
	public static void generMasterDefinition(String XmlPath) throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForFormBean(XmlPath);
		String pgmCd = (String) data.get("pgmCd");
		//根据传入的参数生成相应代码
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenFormBeanUtils.class,
				"/fastFormGenTempl", "masterDefinition.ftl", partFilePathOfMasterXml+pgmCd+".tiles.xml", data);
	}
	
	/**
	 * 自动生成BaseAM60_01Controller的静态方法,同时根据传入的参数判断是否要生成其子类
	 * @throws Exception
	 */
	public static void generBaseController(String XmlPath, boolean subControllerFlg)throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForFormBean(XmlPath);
		String pgmCd = (String) data.get("pgmCd");
		//根据传入的参数生成相应代码             D:/Demo/WebAppMstms/java/nextone/master/controller/
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenFormBeanUtils.class,
						"/fastFormGenTempl", "BaseController.ftl",   partFilePathRelFormBean+"controller/Base"+pgmCd+"Controller.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subControllerFlg) {
			TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenFormBeanUtils.class,
					"/fastFormGenTempl", "Controller.ftl", partFilePathRelFormBean+"controller/"+pgmCd+"Controller.java", data);
		}
	}

	/**
	 * 生成BaseAM60_01Service的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * @throws DocumentException 
	 */
	public static void generBaseService(String XmlPath, boolean subServiceFlg) throws DocumentException {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForFormBean(XmlPath);
		String pgmCd = (String) data.get("pgmCd");
		//根据传入的参数生成相应代码     
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenFormBeanUtils.class,
						"/fastFormGenTempl", "BaseService.ftl", partFilePathRelFormBean+"service/Base"+pgmCd+"Service.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subServiceFlg) {
			TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenFormBeanUtils.class,
					"/fastFormGenTempl", "Service.ftl", partFilePathRelFormBean+"service/"+pgmCd+"Service.java", data);
		}
	}
	/**
	 * 生成BaseAM60_01ServiceImpl的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * 
	 * @throws Exception
	 */
	public static void generBaseServiceImpl(String XmlPath,boolean subServiceImplFlg) throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForFormBean(XmlPath);
		String pgmCd = (String) data.get("pgmCd");
		//根据传入的参数生成相应代码     
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenFormBeanUtils.class,
						"/fastFormGenTempl", "BaseServiceImpl.ftl", partFilePathRelFormBean+"service/impl/Base"+pgmCd+"ServiceImpl.java", data);
		if (subServiceImplFlg) {
			TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenFormBeanUtils.class,
					"/fastFormGenTempl", "ServiceImpl.ftl", partFilePathRelFormBean+"service/impl/"+pgmCd+"ServiceImpl.java", data);
		}
	}

	/**
	 * 生成BaseFormBeanValidator的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * @throws Exception
	 */
	public static void generBaseFormBeanValidator(String XmlPath,boolean subFormBeanValidatorFlg) throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForFormBean(XmlPath);
		String pgmCd = (String) data.get("pgmCd");
		//根据传入的参数生成相应代码       
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenFormBeanUtils.class,
						"/fastFormGenTempl", "BaseFormBeanValidator.ftl", partFilePathRelFormBean+"validator/Base"+pgmCd+"FormBeanValidator.java", data);
		if (subFormBeanValidatorFlg) {
			TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenFormBeanUtils.class,
					"/fastFormGenTempl", "FormBeanValidator.ftl", partFilePathRelFormBean+"validator/"+pgmCd+"FormBeanValidator.java", data);
		}
	}
	public static void generFormBean(String XmlPath) throws Exception {
		Map data = TestGenerateUtils.getDataForFormBean(XmlPath);	
		String pgmCd = (String) data.get("pgmCd");
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenFormBeanUtils.class,
				"/fastFormGenTempl", "FormBean.ftl", partFilePathRelFormBean+"bean/"+pgmCd+"FormBean.java", data);
	}
}