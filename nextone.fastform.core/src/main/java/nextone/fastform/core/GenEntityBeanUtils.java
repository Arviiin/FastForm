package nextone.fastform.core;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;

/**
 * 自动生成EntityBean相关文件的工具类
 * @author jlzhuang
 */
@SuppressWarnings({ "rawtypes" })
public class GenEntityBeanUtils {

	private static Logger logger = LoggerFactory.getLogger(GenEntityBeanUtils.class);
	protected static Configuration cfg;
	protected static String partFile=GenerateUtils.rootDirPath+"/EntitySource/java/"+GenerateUtils.subAndChangeBeanPath;
	protected static String partFilePathOfTable=GenerateUtils.rootDirPath+"/DbSql/";//建表语句生成路径
	
	/**
	 * 生成BaseCompanyBeanValidator的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * @throws Exception
	 */
	public static void generBaseEntityBeanValidator(Class clazz,boolean subCompanyBeanValidatorFlg) throws Exception {
		// 获取数据源
		Map data = GenerateUtils.getDataForEntityBean(clazz);
		String entityBean = (String) data.get("entityBean");
		logger.info("EntityBean is :"+entityBean);
		//根据传入的参数生成相应代码        D:/Demo/WebAppMaster/java/nextone/master/validator
		GenerateUtils.freeMarkerGenerFile(cfg, GenEntityBeanUtils.class,
						"/fastFormGenTempl", "BaseEntityBeanValidator.ftl", partFile+"validator/Base"+entityBean+"Validator.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subCompanyBeanValidatorFlg) {
			GenerateUtils.freeMarkerGenerFile(cfg, GenEntityBeanUtils.class,
					"/fastFormGenTempl", "EntityBeanValidator.ftl", partFile+"validator/"+entityBean+"Validator.java", data);
		}
	}

	/**
	 * 生成BaseEntityService的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * @throws Exception
	 */
	public static void generBaseEntityService(Class clazz,boolean subEntityServiceFlg) throws Exception {
		// 获取数据源          
		Map data = GenerateUtils.getDataForEntityBean(clazz);
		String entityName = (String) data.get("entityName");
		logger.info("EntityName is:"+entityName);
		//根据传入的参数生成相应代码      D:/Demo/WebAppMaster/java/nextone/master/entityservice/
		GenerateUtils.freeMarkerGenerFile(cfg, GenEntityBeanUtils.class,
						"/fastFormGenTempl", "BaseEntityService.ftl", partFile+"entityservice/Base"+entityName+"Service.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subEntityServiceFlg) {
			GenerateUtils.freeMarkerGenerFile(cfg, GenEntityBeanUtils.class,
					"/fastFormGenTempl", "EntityService.ftl", partFile+"entityservice/"+entityName+"Service.java", data);
		}
	}

	/**
	 * 生成BaseEntityServiceImpl的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * 
	 * @throws Exception
	 */
	public static void generBaseEntityServiceImpl(Class clazz,boolean subEntityServiceImplFlg) throws Exception {
		// 获取数据源
		Map data = GenerateUtils.getDataForEntityBean(clazz);
		String entityName = (String) data.get("entityName");
		//根据传入的参数生成相应代码             D:/Demo/WebAppMaster/java/nextone/master/entityservice/impl/
		GenerateUtils.freeMarkerGenerFile(cfg, GenEntityBeanUtils.class,
						"/fastFormGenTempl", "BaseEntityServiceImpl.ftl", partFile+"entityservice/impl/Base"+entityName+"ServiceImpl.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subEntityServiceImplFlg) {
			GenerateUtils.freeMarkerGenerFile(cfg, GenEntityBeanUtils.class,
					"/fastFormGenTempl", "EntityServiceImpl.ftl", partFile+"entityservice/impl/"+entityName+"ServiceImpl.java", data);
		}
	}

	/**
	 * 生成BaseEntityDao的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * 
	 * @throws Exception
	 */
	public static void generBaseEntityDao(Class clazz, boolean subEntityDaoFlg)throws Exception {
		// 获取数据源
		Map data = GenerateUtils.getDataForEntityBean(clazz);
		String entityName = (String) data.get("entityName");
		//根据传入的参数生成相应代码       D:/Demo/WebAppMaster/java/nextone/master/dao/
		GenerateUtils.freeMarkerGenerFile(cfg, GenEntityBeanUtils.class,
						"/fastFormGenTempl", "BaseEntityDao.ftl", partFile+"dao/Base"+entityName+"Dao.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subEntityDaoFlg) {
			GenerateUtils.freeMarkerGenerFile(cfg, GenEntityBeanUtils.class,
					"/fastFormGenTempl", "EntityDao.ftl", partFile+"dao/"+entityName+"Dao.java", data);
		}
	}

	/**
	 * 生成BaseEntityDaoImpl的静态方法,同时根据其传入的参数判断是否要生成其子类
	 * 
	 * @throws Exception
	 */
	public static void generBaseEntityDaoImpl(Class clazz,boolean subEntityDaoImplFlg) throws Exception {
		// 获取数据源
		Map data = GenerateUtils.getDataForEntityBean(clazz);
		String entityName = (String) data.get("entityName");
		//根据传入的参数生成相应代码           D:/Demo/WebAppMaster/java/nextone/master/dao/impl/
		GenerateUtils.freeMarkerGenerFile(cfg, GenEntityBeanUtils.class,
						"/fastFormGenTempl", "BaseEntityDaoImpl.ftl", partFile+"dao/impl/Base"+entityName+"DaoImpl.java", data);
		//判断子类存在标识,决定是否要生成子类.
		if (subEntityDaoImplFlg) {
			GenerateUtils.freeMarkerGenerFile(cfg, GenEntityBeanUtils.class,
					"/fastFormGenTempl", "EntityDaoImpl.ftl", partFile+"dao/impl/"+entityName+"DaoImpl.java", data);
		}
	}

	/**
	 * 生成Pojo的静态方法
	 * @throws Exception 
	 */
	public static void generPojo(Class clazz) throws Exception {
		// 获取数据源
		Map data = GenerateUtils.getDataForEntityBean(clazz);
		String pojoNm = (String) data.get("pojoNm");
		//根据传入的参数生成相应代码        D:/Demo/WebAppMaster/java/nextone/master/pojo/
		GenerateUtils.freeMarkerGenerFile(cfg, GenEntityBeanUtils.class,
				"/searchFormDBGenTempl", "Pojo.ftl", partFile+"pojo/"+pojoNm+".java", data);
	}
	
	/**
	 * 生成创建数据库表文件的静态方法
	 * @throws Exception 
	 */
	public static void genSqlCreateTable(Class clazz) throws Exception {
		// 获取数据源
		Map data = GenerateUtils.getDataForEntityBean(clazz);
		String pojoNm = (String) data.get("pojoNm");
		//根据传入的参数生成相应代码
		GenerateUtils.freeMarkerGenerFile(cfg, GenEntityBeanUtils.class,
				"/searchFormDBGenTempl", "Table.ftl", partFilePathOfTable+pojoNm+"SqlCreateTable.sql", data);
	}
	
}