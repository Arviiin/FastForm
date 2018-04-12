package nextone.fastform.xmlcore;

import java.util.Map;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;

/**
 * 自动生成EntityBean相关文件的工具类
 * @author jlzhuang
 */
@SuppressWarnings({ "rawtypes" })
public class TestGenEntityBeanUtils {

	private static Logger logger = LoggerFactory.getLogger(TestGenEntityBeanUtils.class);
	protected static Configuration cfg;
	protected static String partFile=TestGenerateUtils.rootDirPath+"/EntitySource/java/"+TestGenerateUtils.subAndChangeBeanPath;
	protected static String partFilePathOfTable=TestGenerateUtils.rootDirPath+"/DbSql/";//建表语句生成路径
	/**
	 * 生成BaseCompanyBeanValidator的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * @throws Exception
	 */
	public static void generBaseEntityBeanValidator(String XmlPath,boolean subCompanyBeanValidatorFlg) throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForEntityBean(XmlPath);
		String entityBean = (String) data.get("entityBean");
		logger.info("EntityBean is :"+entityBean);
		//根据传入的参数生成相应代码        D:/Demo/WebAppMaster/java/nextone/master/validator
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenEntityBeanUtils.class,
						"/fastFormGenTempl", "BaseEntityBeanValidator.ftl", partFile+"validator/Base"+entityBean+"Validator.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subCompanyBeanValidatorFlg) {
			TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenEntityBeanUtils.class,
					"/fastFormGenTempl", "EntityBeanValidator.ftl", partFile+"validator/"+entityBean+"Validator.java", data);
		}
	}

	/**
	 * 生成BaseEntityService的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * @throws Exception
	 */
	public static void generBaseEntityService(String XmlPath,boolean subEntityServiceFlg) throws Exception {
		// 获取数据源          
		Map data = TestGenerateUtils.getDataForEntityBean(XmlPath);
		String entityName = (String) data.get("entityName");
		logger.info("EntityName is:"+entityName);
		//根据传入的参数生成相应代码      D:/Demo/WebAppMaster/java/nextone/master/entityservice/
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenEntityBeanUtils.class,
						"/fastFormGenTempl", "BaseEntityService.ftl", partFile+"entityservice/Base"+entityName+"Service.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subEntityServiceFlg) {
			TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenEntityBeanUtils.class,
					"/fastFormGenTempl", "EntityService.ftl", partFile+"entityservice/"+entityName+"Service.java", data);
		}
	}

	/**
	 * 生成BaseEntityServiceImpl的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * 
	 * @throws Exception
	 */
	public static void generBaseEntityServiceImpl(String XmlPath,boolean subEntityServiceImplFlg) throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForEntityBean(XmlPath);
		String entityName = (String) data.get("entityName");
		//根据传入的参数生成相应代码             D:/Demo/WebAppMaster/java/nextone/master/entityservice/impl/
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenEntityBeanUtils.class,
						"/fastFormGenTempl", "BaseEntityServiceImpl.ftl", partFile+"entityservice/impl/Base"+entityName+"ServiceImpl.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subEntityServiceImplFlg) {
			TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenEntityBeanUtils.class,
					"/fastFormGenTempl", "EntityServiceImpl.ftl", partFile+"entityservice/impl/"+entityName+"ServiceImpl.java", data);
		}
	}

	/**
	 * 生成BaseEntityDao的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * 
	 * @throws Exception
	 */
	public static void generBaseEntityDao(String XmlPath, boolean subEntityDaoFlg)throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForEntityBean(XmlPath);
		String entityName = (String) data.get("entityName");
		//根据传入的参数生成相应代码       D:/Demo/WebAppMaster/java/nextone/master/dao/
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenEntityBeanUtils.class,
						"/fastFormGenTempl", "BaseEntityDao.ftl", partFile+"dao/Base"+entityName+"Dao.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subEntityDaoFlg) {
			TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenEntityBeanUtils.class,
					"/fastFormGenTempl", "EntityDao.ftl", partFile+"dao/"+entityName+"Dao.java", data);
		}
	}

	/**
	 * 生成BaseEntityDaoImpl的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * 
	 * @throws Exception
	 */
	public static void generBaseEntityDaoImpl(String XmlPath,boolean subEntityDaoImplFlg) throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForEntityBean(XmlPath);
		String entityName = (String) data.get("entityName");
		//根据传入的参数生成相应代码           D:/Demo/WebAppMaster/java/nextone/master/dao/impl/
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenEntityBeanUtils.class,
						"/fastFormGenTempl", "BaseEntityDaoImpl.ftl", partFile+"dao/impl/Base"+entityName+"DaoImpl.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subEntityDaoImplFlg) {
			TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenEntityBeanUtils.class,
					"/fastFormGenTempl", "EntityDaoImpl.ftl", partFile+"dao/impl/"+entityName+"DaoImpl.java", data);
		}
	}

	/**
	 * 生成Pojo的静态方法
	 * @throws Exception 
	 */
	public static void generPojo(String XmlPath) throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForEntityBean(XmlPath);
		String pojoNm = (String) data.get("pojoNm");
		//根据传入的参数生成相应代码        D:/Demo/WebAppMaster/java/nextone/master/pojo/
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenEntityBeanUtils.class,
				"/searchFormDBGenTempl", "Pojo.ftl", partFile+"pojo/"+pojoNm+".java", data);
	}
	
	/**
	 * 生成创建数据库表文件的静态方法
	 * @throws Exception 
	 */
	public static void genSqlCreateTable(String XmlPath) throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForEntityBean(XmlPath);
		String pojoNm = (String) data.get("pojoNm");
		//根据传入的参数生成相应代码
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenEntityBeanUtils.class,
				"/searchFormDBGenTempl", "Table.ftl", partFilePathOfTable+pojoNm+"SqlCreateTable.sql", data);
	}
	public static void generEntityBean(String XmlPath)throws Exception {
		// 获取数据源
		Map data = TestGenerateUtils.getDataForEntityBean(XmlPath);
		String entityName = (String) data.get("entityName");
		//根据传入的参数生成相应代码       D:/Demo/WebAppMaster/java/nextone/master/bean/
		TestGenerateUtils.freeMarkerGenerFile(cfg, TestGenEntityBeanUtils.class,
						"/fastFormGenTempl", "EntityBean.ftl", partFile+"Bean/"+entityName+"Bean.java", data);
	}
	
}
