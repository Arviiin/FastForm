package nextone.fastform.core;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class GenFastFormRelFileUtils {
	private static Logger logger = LoggerFactory.getLogger(GenFastFormRelFileUtils.class);
	private static Class formBeanClass,entityBeanClass,searchFormBeanClass;
	private static String formBeanFullClassPath="",entityBeanFullClassPath="",searchFormBeanFullClassPath="";
	private static boolean formbeanExt=true,entitybeanExt=true,searchformbeanExt=true; 
	private static boolean subControllerFlg = false, subServiceFlg = false,
			subServiceImplFlg = false, subFormBeanValidatorFlg = false,
			subEntityBeanValidatorFlg = false, subEntityServiceFlg = false,
			subEntityServiceImplFlg = false, subEntityDaoFlg = false,
			subEntityDaoImplFlg = false, subSearchFormDaoImplFlg = false;
	
	/**
	 * main函数可从命令行最多输入6个参数(输入顺序不做要求)
	 * 分别为:<1>FormBean的全类名:nextone.master.bean.DM20FormBean
	 * 		<2>EntityBean的全类名:nextone.master.bean.StudentBean
	 * 		<3>SearchFornBean的全类名:nextone.master.bean.DM21FormBean
	 * 		<4>formbeanExt(即是是否生成与FormBean相关的相应扩展子类),默认为true
	 * 		<5>entitybeanExt(即是是否生成与EntityBean相关的相应扩展子类),默认为true
	 * 		<6>searchformbeanExt(即是是否生成与SearchFornBean相关的相应扩展子类),默认为true
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
		GenerateUtils.rootDirPath =(String) argsMap.get("-gendir");//拿到根目录
		formBeanFullClassPath = (String) argsMap.get("-formbean");
		entityBeanFullClassPath = (String) argsMap.get("-entitybean");
		searchFormBeanFullClassPath = (String) argsMap.get("-searchformbean");
		
		if(!"".equals(formBeanFullClassPath)){
			formBeanClass = Class.forName(formBeanFullClassPath);
			GenerateUtils.subAndChangeBeanPath=GenerateUtils.getSubAndChangeBeanPath(formBeanFullClassPath);
			GenerateUtils.ModulePath=GenerateUtils.getModulePath(formBeanFullClassPath);
		}
		if(!"".equals(entityBeanFullClassPath)){
			entityBeanClass = Class.forName(entityBeanFullClassPath);
			GenerateUtils.subAndChangeBeanPath=GenerateUtils.getSubAndChangeBeanPath(entityBeanFullClassPath);
		}
		if(!"".equals(searchFormBeanFullClassPath)){
			searchFormBeanClass = Class.forName(searchFormBeanFullClassPath);
			GenerateUtils.subAndChangeBeanPath=GenerateUtils.getSubAndChangeBeanPath(formBeanFullClassPath);
			GenerateUtils.ModulePath=GenerateUtils.getModulePath(formBeanFullClassPath);
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
			genFormbeanRelFile(formBeanClass, formbeanExt);
		}
		
		//当EntityBean的全类名不为空时,则生成相应文件
		if(!("".equals(entityBeanFullClassPath))){
			genEntitybeanRelFile(entityBeanClass, entitybeanExt);
		}
		
		//当SearchFormBean的全类名不为空时,则生成相应文件
		if(!("".equals(searchFormBeanFullClassPath))){
			genSearchFormAndDBRelFile(searchFormBeanClass, searchformbeanExt);
		}
	
	}
	
	/**
	 * 自动生成FastForm中的FormBean相关文件
	 * @param formBeanClass
	 * @param formbeanExt
	 * @throws Exception
	 */
	public static void genFormbeanRelFile(Class formBeanClass,boolean formbeanExt) throws Exception{
		
		if(formbeanExt) {
			subControllerFlg = true;
			subServiceFlg = true;
			subServiceImplFlg = true;
			subFormBeanValidatorFlg = true;
	    }
		//测试自动生成pgmCd_U.jsp
		GenFormBeanUtils.generU_Jsp(formBeanClass);
		//测试自动生成pgmCd_R.jsp
		GenFormBeanUtils.generR_Jsp(formBeanClass);
		
		//测试自动生成pgmCdMasterDefinition
		GenFormBeanUtils.generMasterDefinition(formBeanClass);
		
		//测试自动生成BasePgmCdController,同时根据传入的Flg参数来判断是否生成其子类
		GenFormBeanUtils.generBaseController(formBeanClass, subControllerFlg);
		//测试自动生成BasePgmCdService,同时根据传入的Flg参数来判断是否生成其子类
		GenFormBeanUtils.generBaseService(formBeanClass,subServiceFlg);
		//测试自动生成BasePgmCdServiceImpl,同时根据传入的Flg参数来判断是否生成其子类
		GenFormBeanUtils.generBaseServiceImpl(formBeanClass,subServiceImplFlg);
		
		//测试自动生成BaseFormBeanValidator,同时根据传入的Flg参数来判断是否生成其子类
		GenFormBeanUtils.generBaseFormBeanValidator(formBeanClass,subFormBeanValidatorFlg);
		logger.info("------------------------FormBean相关文件生成完成------------------------");
	}
	
	/**
	 * 自动生成FastForm中的EntityBean相关文件
	 * @param entityBeanClass
	 * @param entitybeanExt
	 * @throws Exception
	 */
	public static void genEntitybeanRelFile(Class entityBeanClass, boolean entitybeanExt) throws Exception{
		
		if(entitybeanExt) {
			subEntityBeanValidatorFlg = true;
			subEntityServiceFlg = true;
			subEntityServiceImplFlg = true;
			subEntityDaoFlg = true;
			subEntityDaoImplFlg = true;
	    }
		//测试自动生成BaseCompanyBeanValidator,同时根据传入的Flg参数来判断是否生成其子类
		GenEntityBeanUtils.generBaseEntityBeanValidator(entityBeanClass,subEntityBeanValidatorFlg);
		
		//测试自动生成BaseEntityService,同时根据传入的Flg参数来判断是否生成其子类
		GenEntityBeanUtils.generBaseEntityService(entityBeanClass,subEntityServiceFlg);
		//测试自动生成BaseEntityServiceImpl,同时根据传入的Flg参数来判断是否生成其子类
		GenEntityBeanUtils.generBaseEntityServiceImpl(entityBeanClass,subEntityServiceImplFlg);
		//测试自动生成BaseEntityDao,同时根据传入的Flg参数来判断是否生成其子类
		GenEntityBeanUtils.generBaseEntityDao(entityBeanClass,subEntityDaoFlg);
		//测试自动生成BaseEntityDaoImpl,同时根据传入的Flg参数来判断是否生成其子类
		GenEntityBeanUtils.generBaseEntityDaoImpl(entityBeanClass,subEntityDaoImplFlg);
		
		//测试自动生成Pojo
		GenEntityBeanUtils.generPojo(entityBeanClass);
		//测试自动生成创建Table的文件
		GenEntityBeanUtils.genSqlCreateTable(entityBeanClass);
		logger.info("------------------------EntityBeanAndDb相关文件生成完成------------------------");
	}
	
	/**
	 * 自动生成FastForm中的SearchFormBean相关文件
	 * @param searchFormBeanClass
	 * @param searchformbeanExt
	 * @throws Exception
	 */
	public static void genSearchFormAndDBRelFile(Class searchFormBeanClass,boolean searchformbeanExt) throws Exception{
		
		if(searchformbeanExt) {
			subSearchFormDaoImplFlg = true;
	    }
		//测试自动生成一览画面对应 的.jsp
		GenSearchFormUtils.genSearchFormJsp(searchFormBeanClass);
		//测试自动生成一览画面对应的BaseDaoImpl,同时根据传入的Flg参数来判断是否生成其子类
		GenSearchFormUtils.genBaseSearchFormDaoImpl(searchFormBeanClass,subSearchFormDaoImplFlg);
		//测试自动生成一览画面对应的Json文件
		GenSearchFormUtils.genDefSearchFormJson(searchFormBeanClass);
		
		//测试自动生成MasterDefinition
		GenSearchFormUtils.generMasterDefinition(searchFormBeanClass);
		logger.info("------------------------SearchFormBean相关文件生成完成------------------------");
	}
}