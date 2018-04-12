package nextone.fastform.xmlcore;

import java.util.HashMap;
import java.util.Map;

import nextone.fastform.core.GenerateUtils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestGenFastFormRelFileUtils {
	private static Logger logger = LoggerFactory.getLogger(TestGenFastFormRelFileUtils.class);
	private static String formBeanXml,entityBeanXml,searchFormBeanXml;
	private static String formBeanFullClassPath="",entityBeanFullClassPath="",searchFormBeanFullClassPath="";
	private static boolean formbeanExt=true,entitybeanExt=true,searchformbeanExt=true; 
	private static boolean subControllerFlg = false, subServiceFlg = false,
			subServiceImplFlg = false, subFormBeanValidatorFlg = false,
			subEntityBeanValidatorFlg = false, subEntityServiceFlg = false,
			subEntityServiceImplFlg = false, subEntityDaoFlg = false,
			subEntityDaoImplFlg = false, subSearchFormDaoImplFlg = false;
	/**
	 * main函数可从命令行最多输入6个参数(输入顺序不做要求)
	 * 分别为:<1>FormBean的全类名:src/main/java/nextone/fastform/xml/DM20FormBean.xml
	 * 		<2>EntityBean的全类名:src/main/java/nextone/fastform/xml/StudentBean.xml
	 * 		<3>SearchFornBean的全类名:src/main/java/nextone/fastform/xml/DM21FormBean.xml
	 * 
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
			if(args.length<=0){
				throw new RuntimeException("please input at least a argument");
			}
		Map argsMap = new HashMap();
		argsMap.put("-formbean", "");
		argsMap.put("-entitybean", "");
		argsMap.put("-searchformbean", "");
		argsMap.put("-gendir", "");
		argsMap.put("-formbeanext", true);
		argsMap.put("-entitybeanext", true);
		argsMap.put("-searchformbeanext", true);
		
		for (String arg : args) {
			String[] str = arg.split("=");
			if(str.length!=2){
				throw new RuntimeException("Input Format Error! Please Check!");
			}
			//判断前先将用户输入的Key部分全部转为小写(即忽略Key部分的大小写)
			String str0 = str[0].toLowerCase();
			if(argsMap.containsKey(str0)){
				argsMap.put(str0, str[1]);
			}
		}
		//如果生成的 根目录未输入,则抛出运行时异常
		if("".equals(argsMap.get("-genDir"))){
			throw new RuntimeException("-genDir must be input!");
		}
		TestGenerateUtils.rootDirPath =(String) argsMap.get("-gendir");//拿到根目录
		formBeanFullClassPath = (String) argsMap.get("-formbean");
		entityBeanFullClassPath = (String) argsMap.get("-entitybean");
		searchFormBeanFullClassPath = (String) argsMap.get("-searchformbean");
		
		if(!"".equals(formBeanFullClassPath)){
			formBeanXml = formBeanFullClassPath;
			Map dataForFormBean = TestGenerateUtils.getDataForFormBean(formBeanXml);
			formBeanFullClassPath=dataForFormBean.get("formbeanxmlpath").toString();
			TestGenerateUtils.subAndChangeBeanPath=TestGenerateUtils.getSubAndChangeBeanPath(formBeanFullClassPath);
			TestGenerateUtils.ModulePath=TestGenerateUtils.getModulePath(formBeanFullClassPath);
		}
		if(!"".equals(entityBeanFullClassPath)){
			entityBeanXml = entityBeanFullClassPath;
			Map dataForFormBean = TestGenerateUtils.getDataForEntityBean(entityBeanXml);
			entityBeanFullClassPath=dataForFormBean.get("EntityBeanxmlpath").toString();
			TestGenerateUtils.subAndChangeBeanPath=TestGenerateUtils.getSubAndChangeBeanPath(entityBeanFullClassPath);
		}
		if(!"".equals(searchFormBeanFullClassPath)){
			searchFormBeanXml = searchFormBeanFullClassPath;
			Map dataForFormBean = TestGenerateUtils.getDataForSearchForm(searchFormBeanXml);
			searchFormBeanFullClassPath=dataForFormBean.get("searchformbeanxmlpath").toString();
			TestGenerateUtils.subAndChangeBeanPath=TestGenerateUtils.getSubAndChangeBeanPath(searchFormBeanFullClassPath);
			TestGenerateUtils.ModulePath=TestGenerateUtils.getModulePath(searchFormBeanFullClassPath);
		}
		
//		formbeanExt = argsMap.get("-formbeanext").equals("true")?true:false;
//		formbeanExt = Boolean.valueOf(argsMap.get("-formbeanext").toString()).booleanValue();
		if(argsMap.get("-formbeanext").equals("true"))  formbeanExt = true;
		if(argsMap.get("-entitybeanext").equals("true"))  entitybeanExt = true;
		if(argsMap.get("-searchformbeanext").equals("true"))  searchformbeanExt = true;
		logger.info("formBeanFullClassPath:"+formBeanFullClassPath);
		logger.info("entityBeanFullClassPath:"+entityBeanFullClassPath);
		logger.info("searchFormBeanFullClassPath:"+searchFormBeanFullClassPath);			
		
		//当FormBean的全类名不为空时,则生成相应文件
		if(!("".equals(formBeanFullClassPath))){
			genFormbeanRelFile(formBeanXml, formbeanExt);
		}
		
		//当EntityBean的全类名不为空时,则生成相应文件
		if(!("".equals(entityBeanFullClassPath))){
			genEntitybeanRelFile(entityBeanXml, entitybeanExt);
		}
		
		//当SearchFormBean的全类名不为空时,则生成相应文件
		if(!("".equals(searchFormBeanFullClassPath))){
			genSearchFormAndDBRelFile(searchFormBeanXml, searchformbeanExt);
		}
	
	}
	
	/**
	 * 自动生成FastForm中的FormBean相关文件
	 * @param formBeanClass
	 * @param formbeanExt
	 * @throws Exception
	 */
	public static void genFormbeanRelFile(String formBeanXml,boolean formbeanExt) throws Exception{
		
		if(formbeanExt) {
			subControllerFlg = true;
			subServiceFlg = true;
			subServiceImplFlg = true;
			subFormBeanValidatorFlg = true;
	    }
		//测试自动生成pgmCd_U.jsp
		TestGenFormBeanUtils.generU_Jsp(formBeanXml);
		//测试自动生成pgmCd_R.jsp
		TestGenFormBeanUtils.generR_Jsp(formBeanXml);
		
		//测试自动生成pgmCdMasterDefinition
		TestGenFormBeanUtils.generMasterDefinition(formBeanXml);
		
		//测试自动生成BasePgmCdController,同时根据传入的Flg参数来判断是否生成其子类
		TestGenFormBeanUtils.generBaseController(formBeanXml, subControllerFlg);
		//测试自动生成BasePgmCdService,同时根据传入的Flg参数来判断是否生成其子类
		TestGenFormBeanUtils.generBaseService(formBeanXml,subServiceFlg);
		//测试自动生成BasePgmCdServiceImpl,同时根据传入的Flg参数来判断是否生成其子类
		TestGenFormBeanUtils.generBaseServiceImpl(formBeanXml,subServiceImplFlg);
		
		//测试自动生成BaseFormBeanValidator,同时根据传入的Flg参数来判断是否生成其子类
		TestGenFormBeanUtils.generBaseFormBeanValidator(formBeanXml,subFormBeanValidatorFlg);
		TestGenFormBeanUtils.generFormBean(formBeanXml);
		logger.info("------------------FormBean相关文件生成完成------------------");
	}
	
	/**
	 * 自动生成FastForm中的EntityBean相关文件
	 * @param entityBeanClass
	 * @param entitybeanExt
	 * @throws Exception
	 */
	public static void genEntitybeanRelFile(String entityBeanXml, boolean entitybeanExt) throws Exception{
		
		if(entitybeanExt) {
			subEntityBeanValidatorFlg = true;
			subEntityServiceFlg = true;
			subEntityServiceImplFlg = true;
			subEntityDaoFlg = true;
			subEntityDaoImplFlg = true;
	    }
		//测试自动生成BaseCompanyBeanValidator,同时根据传入的Flg参数来判断是否生成其子类
		TestGenEntityBeanUtils.generBaseEntityBeanValidator(entityBeanXml,subEntityBeanValidatorFlg);
		
		//测试自动生成BaseEntityService,同时根据传入的Flg参数来判断是否生成其子类
		TestGenEntityBeanUtils.generBaseEntityService(entityBeanXml,subEntityServiceFlg);
		//测试自动生成BaseEntityServiceImpl,同时根据传入的Flg参数来判断是否生成其子类
		TestGenEntityBeanUtils.generBaseEntityServiceImpl(entityBeanXml,subEntityServiceImplFlg);
		//测试自动生成BaseEntityDao,同时根据传入的Flg参数来判断是否生成其子类
		TestGenEntityBeanUtils.generBaseEntityDao(entityBeanXml,subEntityDaoFlg);
		//测试自动生成BaseEntityDaoImpl,同时根据传入的Flg参数来判断是否生成其子类
		TestGenEntityBeanUtils.generBaseEntityDaoImpl(entityBeanXml,subEntityDaoImplFlg);
		
		//测试自动生成Pojo
		TestGenEntityBeanUtils.generPojo(entityBeanXml);
		//测试自动生成创建Table的文件
		TestGenEntityBeanUtils.genSqlCreateTable(entityBeanXml);
		TestGenEntityBeanUtils.generEntityBean(entityBeanXml);
		logger.info("------------------EntityBeanAndDb相关文件生成完成------------------");
	}
	
	/**
	 * 自动生成FastForm中的SearchFormBean相关文件
	 * @param searchFormBeanClass
	 * @param searchformbeanExt
	 * @throws Exception
	 */
	public static void genSearchFormAndDBRelFile(String searchFormBeanXml,boolean searchformbeanExt) throws Exception{
		
		if(searchformbeanExt) {
			subSearchFormDaoImplFlg = true;
	    }
		//测试自动生成一览画面对应 的.jsp
		TestGenSearchFormUtils.genSearchFormJsp(searchFormBeanXml);
		//测试自动生成一览画面对应的BaseDaoImpl,同时根据传入的Flg参数来判断是否生成其子类
		TestGenSearchFormUtils.genBaseSearchFormDaoImpl(searchFormBeanXml,subSearchFormDaoImplFlg);
		//测试自动生成一览画面对应的Json文件
		TestGenSearchFormUtils.genDefSearchFormJson(searchFormBeanXml);
		
		//测试自动生成MasterDefinition
		TestGenSearchFormUtils.generMasterDefinition(searchFormBeanXml);
		TestGenSearchFormUtils.generSearchFormBean(searchFormBeanXml);
		logger.info("------------------SearchFormBean相关文件生成完成------------------");
	}
}

