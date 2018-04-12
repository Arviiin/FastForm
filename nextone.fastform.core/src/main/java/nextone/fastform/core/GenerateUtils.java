package nextone.fastform.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import nextone.fastform.annotation.FastEntity;
import nextone.fastform.annotation.FastForm;
import nextone.fastform.annotation.FastSearchForm;
import nextone.fastform.annotation.Input;
import nextone.fastform.annotation.Input.TYPE;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 自动生成文件共通方法的工具类
 * @author jlzhuang
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenerateUtils {
	public static String rootDirPath = null;//定义静态根路径变量
	public static String subAndChangeBeanPath = null;//定义静态部分路径变量
	public static String ModulePath = null;//定义静态模块路径变量
	private static Logger logger = LoggerFactory.getLogger(GenerateUtils.class);
	private static Class entityClazz;
	private static final String DataTransferObjectAnnotationNm = "org.directwebremoting.annotations.DataTransferObject";
	
	/**
	 * 截取输入参数类的全路径的部分,并将其转换,eg: nextone.master.bean.StudentBean      
	 * 截取nextone.master并将其转换为nextone/master/
	 * @param beanFullClassPath
	 * @return
	 */
	public static String getSubAndChangeBeanPath(String beanFullClassPath){
		//字符串按小圆点进行分割,split(".") 直接这样写是不行的, 正确的写法是:对小圆点进行转义
		String[] beanSplitPath = beanFullClassPath.split("\\.");
		String subFromBeanPath = beanSplitPath[0]+"/"+beanSplitPath[1]+"/";
		return subFromBeanPath;
	}
	
	/**
	 * 截取输入参数类的全路径的部分,并将其转换,eg: nextone.master.bean.StudentBean
	 * 截取master并将其转换为master/
	 * @param formBeanFullClassPath
	 * @return
	 */
	public static String getModulePath(String formBeanFullClassPath){
		//字符串按小圆点进行分割,split(".") 直接这样写是不行的, 正确的写法是:对小圆点进行转义
		String[] splitStr = formBeanFullClassPath.split("\\.");
		return splitStr[splitStr.length-3]+"/";
	}
	
	/**
	 * 根据传入参数在硬盘指定位置上生成相应代码
	 * @param data:封装的数据用于填充模板
	 * @param cfg:配置对象
	 * @param clazz:使用此方法的类
	 * @param classpath:指定模板所在的classpath目录  
	 * @param ftl:指定模板
	 * @param filePath:指定文件生成在硬盘上的位置
	 */
	public static void freeMarkerGenerFile(Configuration cfg,Class clazz,String classpath,String ftl,String filePath,Map data){
		try {
			if(cfg==null){
				cfg = new Configuration(); 
				cfg.setDefaultEncoding("UTF-8");
			}
			cfg.setClassForTemplateLoading(clazz, classpath);//指定模板所在的classpath目录  
			Template t = cfg.getTemplate(ftl);//指定模板  
			//创建文件需要的生成路径
			String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
			File dir = new File(dirPath);
			dir.mkdirs();
			FileOutputStream fos = new FileOutputStream(new File(filePath));//jsp文件的生成目录
			t.process(data, new OutputStreamWriter(fos,"utf-8"));
			fos.flush();  
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 截取FormBean里的编号赋值给pgmCd
	 * @param simpleClassName
	 * @return
	 */
	public static String subForPgmCd(String simpleClassName){
		int ipos = simpleClassName.indexOf("FormBean");
		String pgmCd = simpleClassName.substring(0, ipos);
		return pgmCd;
	}
	
	/**
	 * 在控制台输出FormBean中字段为实体类型,且其实体类在定义时候标注了@FastEntity注解
	 * @param fieldOfFormBean
	 */
	public static void printTips (Field fieldOfFormBean){
		logger.info(fieldOfFormBean.getName()+":它是FormBean中的一个字段,且其对应的实体类在定义时标注了@FastEntity注解");
	}

	/**
	 * 根据大写字母把和数字字符串分割成字符数组,然后在每个大写字母前加上_组成新的 字符串
	 * @param s
	 * @return
	 */
	public static String replaceChar(String s){
		String[] ss = s.split("(?<!^)(?=[A-Z]|[0-9])");//将字符串根据大写字母和数字划分为字符串数组
		StringBuilder sb = new StringBuilder();//创建字符串拼接对象
	       for (int i = 0; i < ss.length; i++) {
	           sb.append(ss[i]);
	           if(i < ss.length-1) sb.append("_");
	       }
	       return sb.toString();
	}
	
	/**
	 * 递归获取FormBean中字段类型为EntityBean的父类的数据,用于生成FormBean相关代码,这里特指JSP
	 * @param clazz
	 * @param data
	 * @param compons
	 * @param hiddenCompons
	 * @param switchCompons
	 * @param multiCompons
	 * @param beanFieldOfFormBeanName
	 * @param fieldClassName
	 */
	protected static void getFieldClassAllParentData(Class clazz,Map data,List compons,List hiddenCompons,
		List switchCompons,List multiCompons,String beanFieldOfFormBeanName,String fieldClassName) {
		Class superclass = clazz.getSuperclass();
		if(superclass.isAnnotationPresent(FastEntity.class)){
			// 拿到标注了FastEntity注解的实体类的父类(比如CompanyBean的父类BaseEntityBean)里的所有字段,遍历每个字段,若字段上有input注解则解析并将数据放入集合
			getFiledDataOfEntityBeanForFormBean(superclass, data, compons, hiddenCompons, switchCompons, multiCompons, beanFieldOfFormBeanName, fieldClassName);
			//递归调用获取父类的数据
			getFieldClassAllParentData(superclass,data, compons, hiddenCompons, switchCompons, multiCompons, beanFieldOfFormBeanName, fieldClassName);
		}
		return ;
	}
	
	/**
	 * 拿到标注了FastEntity注解的实体类(比如CompanyBean)里的所有字段,遍历每个字段,若字段上有input注解则解析并将数据放入集合
	 * @param clazz
	 * @param data
	 * @param compons
	 * @param hiddenCompons
	 * @param switchCompons
	 * @param multiCompons
	 * @param beanFieldOfFormBeanName
	 * @param fieldClassName
	 */
	protected static void getFiledDataOfEntityBeanForFormBean(Class clazz,Map data,List compons,List hiddenCompons,List switchCompons,List multiCompons,
			String beanFieldOfFormBeanName,String fieldClassName) {
		// 拿到标注了FastEntity注解的实体类(比如CompanyBean)里的所有字段,遍历每个字段.
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			//若字段上有input注解则解析并将数据放入小map中再放入List集合中
			if (field.isAnnotationPresent(Input.class)) {
				//获取对象
				Input input = field.getAnnotation(Input.class);

				Map compon = new HashMap(); // 创建map容器存放当前控件的数据

				//定义一个标签指向的组件Id的前缀变量
				String lblForCompoIdPrefix = null;
				compon.put("maxlength", input.maxlength()); //最大长度(在前面定义方便后面修改)
				switch (input.inputType()) {
				case HIDDEN:
					compon.put("hidAttributePath",input.hidAttributePath()); // 隐藏属性存放位置
					compon.put("hidAttributeId",input.hidAttributeId()); // 隐藏属性id名
					hiddenCompons.add(compon); // 将控件放入list集合中
					break;
				case CODE:
				case TEXT:
					lblForCompoIdPrefix = "txt";
					// 当为普通输入,或者文本类型时,检查其有无显示定义maxleng(默认值为-1),若无则报错,
					if (input.maxlength() == -1) {
						throw new RuntimeException("please input the maxlength of code:"+ field.getName());
					} else {
						compon.put("maxlength", input.maxlength()); //最大长度
					}
					break;
				case SELECT:
					lblForCompoIdPrefix = "sel";
					// 当为下拉框输入类型时,检查其有无显示定义maxleng(默认值为-1),若无则将其设置为20,
					if (input.maxlength() == -1) {
						compon.put("maxlength", 20); // 最大长度
					} else {
						compon.put("maxlength", input.maxlength()); // 最大长度
					}
					break;
				case SWITCH:
					lblForCompoIdPrefix = "chk";
					compon.put("onValueTransl",input.onValueTransl()); // 滑块是标志(Jsp中使用'1')
					compon.put("offValueTransl",input.offValueTransl()); // 滑块否标志
					compon.put("onValueTranslJava", input.onValueTransl().replace("'", "\"")); // 滑块是标志(java中使用"1")
					compon.put("offValueTranslJava", input.offValueTransl().replace("'", "\"")); // 滑块否标志
					compon.put("onText", input.onText()); // 滑块"是"状态显示的字
					compon.put("offText", input.offText()); // 滑块"否"状态显示的字
					switchCompons.add(compon); // 将控件放入滑块list集合中
					break;
				case MULTLANG:
					lblForCompoIdPrefix = "mul";
					// 当为多国语输入类型时,检查其有无显示定义maxleng(默认值为-1),若无则将其设置为40,
					if (input.maxlength() == -1) {
						compon.put("maxlength", 40); // 最大长度
					} else {
						compon.put("maxlength", input.maxlength()); // 最大长度
					}
					compon.put("nameType", input.nameType());
					multiCompons.add(compon);// 将控件放入多国语list集合中
					break;
				case DATE:
				case DATETIME:
					lblForCompoIdPrefix = "dat";
					break;
				case NUM:
					lblForCompoIdPrefix = "num";
					break;
				default:
					break;
				}
				compon.put("inputType", input.inputType().toString());//将控件的类型赋值给inputType供freemarker生成代码
				compon.put("formInOrOutputPath", beanFieldOfFormBeanName+ "." + field.getName());// 控件接收或者读取数据的位置
				compon.put("labelForComponentId",lblForCompoIdPrefix + fieldClassName + "_"+ field.getName()); // 点label光标跳到哪个控件,也即是那个控件的id,
				compon.put("labelId", "lbl" + fieldClassName+ "_" + field.getName()); // label的id,
				compon.put("spanId", "span" + fieldClassName+ "_" + field.getName()); // span的id,
				
				// 标签上显示的字,如果用户指定了,则显示用户指定的,否则默认label.字段名(label.已经在ftl中写了),这里又是字符串的 比较记住用equals方法,不能用==
				if ("".equals(input.labelMsgCd())) {
					compon.put("labelMsgCd", field.getName()); // 标签上显示的字
				} else {
					compon.put("labelMsgCd", input.labelMsgCd()); // 标签上显示的字
				}
				compon.put("must", input.must()); // 是否必须输入
				compon.put("upperCase", input.upperCase()); // 是否要小写转大写
				if (input.upperCase() == true) {
					data.put("upperCaseFlg", true); // 是否存在有字段在注解中定义了需要转换大小写的标志(若有则在script部分加上$("input.upperCase").textInputLower2Upper();)
				}
				compon.put("readOnly", input.readOnly()); // 是否只可读
				compon.put("entityBean", beanFieldOfFormBeanName); // FormBean的字段名(比如companyBean),只有生成两个JSP时用到了,现在事太多,稍后做修改
				compon.put("fieldName", field.getName()); // 拿到字段名
				compon.put("typeCd", input.typeCd()); //拿到大分类
				compon.put("lblValBeanService", input.lblValBeanService());//拿到实体Service,供下拉框取实体.
				compons.add(compon); // 将控件放入list集合中
			}
			
		}
	}
	
	/**
	 * 获取使用FreeMarker生成FormBean相关文件所需的相应数据data
	 * @param clazz:即是FormBean的完整类名
	 * @return 返回值为Map类型
	 */
	public static Map getDataForFormBean(Class clazz){
		if (!clazz.isAnnotationPresent(FastForm.class)) {
			throw new RuntimeException(clazz.getName()+" must declare @FastForm.");
		}
		//判断xxxFormBean上面是否标注了@DataTransferObject,若无则抛出异常
		if (!checkIsDeclaredAnnotation(clazz,DataTransferObjectAnnotationNm)) {
			throw new RuntimeException("You must forget to mark @DataTransferObject on the FormBean,Please check!");
		}
		logger.info("数据开始读取");
		// 数据源
		Map data = new HashMap();// 创建map容器存放所有的数据
		
		List compons = new ArrayList();// 创建list容器存放entityBean中所有添加注解字段的相关数据
		List hiddenCompons = new ArrayList();// 创建list容器存放注解为隐藏字段的相关数据
		List switchCompons = new ArrayList();// 创建list容器存放注解为滑块字段的相关数据
		List multiCompons = new ArrayList();// 创建list容器存放注解为多国语字段的相关数据
		logger.info(clazz.getSimpleName()+ "上有FastForm注解");
		// 截取FormBean里的程序编号和模块编号分别赋值给pgmCd,moduleCd
		String simpleClassName = clazz.getSimpleName();
		String pgmCd = GenerateUtils.subForPgmCd(simpleClassName);
		String moduleCd = simpleClassName.substring(0, 2).toLowerCase();
		String packageCd = simpleClassName.substring(0, 4).toLowerCase();
		
		// 获取当前系统时间,并取其数字形式,用于JSP中引入外部js代码更换版本号(若不改变版本号,即便改变js代码,引用的还是未改之前的)
		Date date = new Date();// 默认取当前系统时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		String versionNum = dateFormat.format(date);

		// 截取类所在包名的前一部分
		String fullClassName = clazz.getName();
		String prePackage = fullClassName.substring(0,fullClassName.indexOf(".", fullClassName.indexOf(".") + 1));
		
		//获取FormBean类上标志的FastForm注解类型的相应对象
		FastForm fastForm = (FastForm) clazz.getAnnotation(FastForm.class);
		
		//将数据丢到Map集合中
		data.put("pgmCd", pgmCd);// 作者编号
		data.put("moduleCd", moduleCd);// 模块编号
		data.put("versionNum", versionNum);// 引用外部的js代码版本号,只要js代码变动就需要更新其版本号,这里我们取电脑当前时间.
		data.put("formDesc", fastForm.formDesc());// JSP画面描述
		data.put("formId", fastForm.formId());// 主键
		data.put("pgmController", pgmCd + "Controller");
		data.put("pgmFormBean", pgmCd + "FormBean");
		data.put("pgmService", pgmCd + "Service");
		data.put("pgmFormBeanValidator", pgmCd + "FormBeanValidator");
		data.put("prePackage", prePackage);
		data.put("packageCd", packageCd);

		//遍历FormBean的所有字段,若某字段为实体类型且相应实体类在其定义时标注了@FastEntity则进行相应处理.
		Field[] fieldsOfFormBean = clazz.getDeclaredFields();
		for (Field fieldOfFormBean : fieldsOfFormBean) {
			// 拿到字段的类型
			if (fieldOfFormBean.getType().isAnnotationPresent(FastEntity.class)) {
				//判断xxxEntityBean上面是否标注了@DataTransferObject,若无则抛出异常
				if (!checkIsDeclaredAnnotation(fieldOfFormBean.getType(),DataTransferObjectAnnotationNm)) {
					throw new RuntimeException("You must forget to mark @DataTransferObject on the EntityBean,Please check!");
				}
				logger.info(fieldOfFormBean.getName()+ "字段:它是FormBean中的字段对应的实体类在定义时标注了@FastEntity注解");
				String beanFieldOfFormBeanName = fieldOfFormBean.getName();//字段名(比如companyBean)
				
				String fieldClassName = fieldOfFormBean.getType().getSimpleName();// 获取字段名 的简单类名(比如CompanyBean)
				logger.info(fieldClassName);
				int ipos1 = fieldClassName.indexOf("Bean");
				String entityName = fieldClassName.substring(0, ipos1);
				data.put("entityService", entityName + "Service");
				data.put("entityBean", fieldClassName);//注意这个和下面那个不一样,这个是简单类名,而不是字段,比如(CompanyBean)
				data.put("entityBeanPackage", fieldOfFormBean.getType().getName());

				//遍历FormBean中标注了FastEntity注解的对应EntityBean类里的所有字段,若某字段在其定义时标注了@Input则进行相应处理.
				getFiledDataOfEntityBeanForFormBean(fieldOfFormBean.getType(), data, compons, hiddenCompons, switchCompons, multiCompons, beanFieldOfFormBeanName, fieldClassName);
				
				//递归获取父类的数据
				getFieldClassAllParentData(fieldOfFormBean.getType(),data, compons, hiddenCompons, switchCompons, multiCompons, beanFieldOfFormBeanName, fieldClassName);
			}
		}
	
		data.put("components", compons); // 将上面创建的list容器放入map中
		data.put("hiddenComponents", hiddenCompons); // 将上面创建的隐藏字段的list容器放入map中
		data.put("switchComponents", switchCompons); // 将上面创建的滑块字段的list容器放入map中
		data.put("multiComponents", multiCompons); // 将上面创建的多国语字段的list容器放入map中
		logger.info("===>getDataForFormBean读取成功");
		return data;
	}
	
	/**
	 * 获取使用FreeMarker生成EntityBean,Pojo和Table等相关文件所需的相应数据data
	 * @param clazz:即是EntityBean的完整类名
	 * @return 返回值为Map类型
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public static Map getDataForEntityBean(Class clazz) throws NoSuchFieldException, SecurityException{
		// 判断EntityBean的是否在定义时标注了@FastEntity,若未标注则直接抛出运行时异常,反之则标注了进行余下代码.
		if (!clazz.isAnnotationPresent(FastEntity.class)) {
			throw new RuntimeException(clazz.getName()+" must declare @FastEntity.");
		}
		//判断xxxEntityBean上面是否标注了@DataTransferObject,若无则抛出异常
		if (!checkIsDeclaredAnnotation(clazz,DataTransferObjectAnnotationNm)) {
			throw new RuntimeException("You must forget to mark @DataTransferObject on the EntityBean,Please check!");
		}
		logger.info("数据开始读取");
		Map data = new HashMap();// 创建map容器存放所有的数据
		List compons = new ArrayList();// 创建list容器存放各字段的相关数据
		List mustCompons = new ArrayList();// 创建list容器存放不能为空字段的相关数据
		List codeCompons = new ArrayList();// 创建list容器存放code字段的相关数据
		List switchCompons = new ArrayList();// 创建list容器存放滑块字段的相关数据
		List maxlengthCompons = new ArrayList();// 创建list容器存放最大长度为20字段的相关数据
		List multiCompons = new ArrayList();// 创建list容器存多国语字段的相关数据

		String fullClassName = clazz.getName();
		String prePackage = fullClassName.substring(0,fullClassName.indexOf(".", fullClassName.indexOf(".") + 1));

		FastEntity fastEntity = (FastEntity) clazz.getAnnotation(FastEntity.class);

		//对标注了@FastEntity进行校验,例如bieKey="xxx",对xxx进行检查若其不是以Cd结尾则抛出异常
		if(!fastEntity.bizKey().endsWith("Cd") || "".equals(fastEntity.bizKey())){
			throw new RuntimeException("The bizKey may undefined or end with 'Cd' ,So there may be an error on bizKey Of @FastEntity");
		}
		String pre_bizKey = fastEntity.bizKey().substring(0,fastEntity.bizKey().indexOf("Cd"));//取业务键的前一部分,比如stuCd,则取出stu.
		String fieldClassName = clazz.getSimpleName();// 获取EntityBeand的简单类名
		String entityName = fieldClassName.substring(0, fieldClassName.indexOf("Bean"));
		data.put("bizKey", fastEntity.bizKey());
		data.put("entityId", fastEntity.entityId());// 实体类的主键.
		data.put("pre_bizKey", pre_bizKey);
		data.put("entityIdDBName",replaceChar(fastEntity.entityId()));// 实体类的主键在数据库的表中的名字.
		data.put("pojoNm", fastEntity.pojoNm());
		data.put("SequenceNm", fastEntity.sequenceNm());
		data.put("entityName", entityName);
		data.put("entityBean",  fieldClassName);
		data.put("entityBeanPackage", clazz.getName());
		data.put("prePackage", prePackage);
		// 拿到实体类(比如:CompanyBean)里的所有字段,然后遍历每个字段,若字段上有input注解则解析并将数据放入集合
		getFiledOfEntityBeanDateModel(clazz, compons, mustCompons, codeCompons, switchCompons, multiCompons, maxlengthCompons, fastEntity);
		
		//递归获取父类的数据
		getAllParentData(clazz, compons, mustCompons, codeCompons, switchCompons, multiCompons, maxlengthCompons, fastEntity);
		data.put("components", compons); // 将上面创建的list容器放入map中
		data.put("mustComponents", mustCompons); // 将上面创建的滑块字段的list容器放入map中
		data.put("maxlengthComponents", maxlengthCompons); // 将上面创建的滑块字段的list容器放入map中
		data.put("multiComponents", multiCompons); // 将上面创建的多国语字段的list容器放入map中
		data.put("codeComponents", codeCompons); // 将上面创建的code字段的list容器放入map中
		logger.info("===>getDataForEntityBean读取成功");
		return data;
	}
	
	/**
	 * 获取使用FreeMarker生成SearchForm相关文件所需的相应数据data
	 * @param clazz:即是EntityBean的完整类名
	 * @return 返回值为Map类型
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws ClassNotFoundException 
	 */
	public static Map getDataForSearchForm(Class clazz) throws Exception{
		if (!clazz.isAnnotationPresent(FastSearchForm.class)) {
			throw new RuntimeException(clazz.getName()+" must declare @FastSearchForm.");
		}
		//判断xxxFormBean上面是否标注了@DataTransferObject,若无则抛出异常
		if (!checkIsDeclaredAnnotation(clazz,DataTransferObjectAnnotationNm)) {
			throw new RuntimeException("You must forget to mark @DataTransferObject on the FormBean,Please check!");
		}
		logger.info(clazz+"");
		// 截取一览FormBean里的程序编号和模块编号分别赋值给searchFormPgmCd,searchFormModuleCd
		String simpleClassName = clazz.getSimpleName();
		String searchFormPgmCd = GenerateUtils.subForPgmCd(simpleClassName);
		String searchFormModuleCd = simpleClassName.substring(0, 2).toLowerCase();
		String searchFormPackageCd = simpleClassName.substring(0, 4).toLowerCase();
		
		//获取一览FormBean(类)上标注的FastSearchForm注解类型的对象
		FastSearchForm fastSearchForm = (FastSearchForm) clazz.getAnnotation(FastSearchForm.class);
		String entityBean = fastSearchForm.entityBean();//得到一览对应的实体Bean(带包名)
		String crudPgmCd = fastSearchForm.crudPgmCd();//得到一览对应的CRUD编号
		String crudModuleCd = crudPgmCd.substring(0, 2).toLowerCase();
		String crudPackageCd = crudPgmCd.substring(0, 4).toLowerCase();
		
		//获取一览对应的实体类
		logger.info(entityBean);
		try {
			entityClazz = Class.forName(entityBean);
			//判断xxxEntityBean上面是否标注了@DataTransferObject,若无则抛出异常
			if (!checkIsDeclaredAnnotation(entityClazz,DataTransferObjectAnnotationNm)) {
				throw new RuntimeException("You must forget to mark @DataTransferObject on the EntityBean,Please check!");
			}
		} catch (ClassNotFoundException e) {
			logger.error("\""+entityBean+"\"ClassNotFoundException Please Check Input Class Name Such As  blank space");
		}
		logger.info(entityClazz+"");
		if (!entityClazz.isAnnotationPresent(FastEntity.class)) {
			return null;
		}
		logger.info(entityClazz.getSimpleName()+ "上有FastEntity注解");
		logger.info("数据开始读取");
		
		//获取EntityBean类上标志的FastEntity注解类型的相应对象
		FastEntity fastEntity = (FastEntity) entityClazz.getAnnotation(FastEntity.class);
		
		// 数据源
		Map data = new HashMap();// 创建map容器存放所有的数据
		
		List compons = new ArrayList();// 创建list容器存放各字段的相关数据
		List mustCompons = new ArrayList();// 创建list容器存放不能为空字段的相关数据
		List codeCompons = new ArrayList();// 创建list容器存放code字段的相关数据
		List switchCompons = new ArrayList();// 创建list容器存放滑块字段的相关数据
		List maxlengthCompons = new ArrayList();// 创建list容器存放最大长度为20字段的相关数据
		List multiCompons = new ArrayList();// 创建list容器存多国语字段的相关数据
		
		// 截取类所在包名的前一部分
		String fullClassName = entityClazz.getName();
		String prePackage = fullClassName.substring(0,fullClassName.indexOf(".", fullClassName.indexOf(".") + 1));
		
		//将数据丢到Map集合中
		data.put("searchFormPgmCd", searchFormPgmCd);// 加入检索画面编号
		data.put("searchFormModuleCd", searchFormModuleCd);// 加入检索画面编号
		data.put("searchFormPackageCd", searchFormPackageCd);// 加入检索画面编号
		data.put("crudPgmCd", crudPgmCd);// 增删改查画面编号
		data.put("crudModuleCd", crudModuleCd);// 模块编号
		data.put("crudPackageCd", crudPackageCd);
		data.put("prePackage", prePackage);
		data.put("entityId", fastEntity.entityId());// 主键
		data.put("entityIdDBName",replaceChar(fastEntity.entityId()));// 实体类的主键在数据库的表中的名字.
		data.put("bizKey", fastEntity.bizKey());//业务键
		data.put("pojoNm", fastEntity.pojoNm());

		//遍历EntityBean的所有字段,若某字段在其定义时标注了@Input则进行相应处理.
		getFiledOfEntityBeanDateModel(entityClazz, compons, mustCompons, codeCompons, switchCompons, multiCompons, maxlengthCompons, fastEntity);
		
		//递归获取父类的数据
		getAllParentData(entityClazz, compons, mustCompons, codeCompons, switchCompons, multiCompons, maxlengthCompons, fastEntity);
		
		data.put("components", compons); // 将上面创建的list容器放入map中
		logger.info("===>getDataForSearchForm读取成功");
		return data;
	}
	
	/**
	 * 递归获取EntityBean父类的数据,用于生成SearchForm,Pojo和Table等相关文件
	 * @param clazz
	 * @param compons
	 * @param fastEntity
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	protected static void getAllParentData(Class clazz,List compons,List mustCompons,List codeCompons,List switchCompons,List multiCompons,List maxlengthCompons,FastEntity fastEntity) throws NoSuchFieldException, SecurityException {
		Class superclass = clazz.getSuperclass();
		logger.info(superclass.toString());
		if(superclass.isAnnotationPresent(FastEntity.class)){
			// 拿到标注了FastEntity注解的实体类的父类(比如CompanyBean的父类BaseEntityBean)里的所有字段,遍历每个字段,若字段上有input注解则解析并将数据放入集合
			getFiledOfEntityBeanDateModel(superclass, compons, mustCompons, codeCompons, switchCompons, multiCompons, maxlengthCompons, fastEntity);
			//递归获取父类的数据
			getAllParentData(superclass, compons, mustCompons, codeCompons, switchCompons, multiCompons, maxlengthCompons, fastEntity);
		}
		return ;
	}
	
	/**
	 * 拿到标注了FastEntity注解的实体类里的所有字段
	 * 遍历每个字段,若字段上有input注解则解析并将数据放入集合
	 * @param clazz
	 * @param compons
	 * @param fastEntity
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	protected static void getFiledOfEntityBeanDateModel(Class clazz,List compons,List mustCompons,List codeCompons,List switchCompons,List multiCompons,List maxlengthCompons,FastEntity fastEntity) throws NoSuchFieldException, SecurityException {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Input.class)) {
				//获取对象
				Input input = field.getAnnotation(Input.class);
				//拿到字段类名的简单类名
				String fieldClassSimpleName = field.getType().getSimpleName();
				// 创建map容器存放当前控件的数据
				Map compon = new HashMap(); 

				compon.put("fieldName", field.getName()); // 拿到字段名(放在前面的好处便于不同输入类型的字段修改其在模板中想要显示的字段名)
				compon.put("fieldDBName", replaceChar(field.getName()));// 字段在数据库的表中的名字.
				compon.put("lblValBeanService", input.lblValBeanService());//拿到下拉框的实体Service
				compon.put("maxlength", input.maxlength());
				compon.put("inputType", input.inputType().toString());//将控件的类型赋值给inputType供freemarker生成代码
				switch (input.inputType()) {
				case HIDDEN:
					break;
				case CODE:
					codeCompons.add(compon); // 将控件放入滑块list集合中
					break;
				case SELECT:
					checkSelectTypeField(field, clazz);
					break;
				case TEXT:
					if(field.getName().equals("createUserNm") || field.getName().equals("updateUserNm")){
						String joinFiled = field.getName().substring(0, field.getName().indexOf("Nm"));
						compon.put("joinFiled", joinFiled);
					}
					compon.put("enableFullWidthChar",input.enableFullWidthChar()); // 拿到是否全角输入
					break;
				case SWITCH:
					compon.put("onValueTransl",input.onValueTransl()); // 滑块是标志(Jsp中使用'1')
					compon.put("offValueTransl",input.offValueTransl()); // 滑块否标志
					compon.put("onValueTranslJava", input.onValueTransl().replace("'", "\"")); // 滑块是标志(java中使用"1")
					compon.put("offValueTranslJava", input.offValueTransl().replace("'", "\"")); // 滑块否标志
					compon.put("onText", input.onText()); // 滑块"是"状态显示的字
					compon.put("offText", input.offText()); // 滑块"否"状态显示的字
					switchCompons.add(compon); // 将控件放入滑块list集合中
					break;
				case MULTLANG:
					compon.put("fieldName", field.getName()+"Cd"); // 对原始字段名进行修改以达到想要的结果
					compon.put("fieldDBName", replaceChar(field.getName())+"_Cd");// 字段在数据库的表中的名字,进行修改以达到想要的结果.
					compon.put("nameType", input.nameType());
					compon.put("unique", input.unique());// 是否唯一
					multiCompons.add(compon);// 将控件放入多国语list集合中
					checkMultiLangField(field, clazz);
					break;
				case NUM:	
					if (input.precision() == -1 || input.scale() == -1) // 当为NUM类型时,检查其有无显示定义precision和scale(默认值均为-1),若无则报错
						throw new RuntimeException("please input the precision,scale of field:"+ field.getName());
					compon.put("maxlength", (input.precision()-input.scale())/3+2+input.precision());//NUM类型的长度为总位数减去小数位数,再除以3再加2
				default:
					break;
				}
				compon.put("must", input.must()); // 是否必须输入
				if (input.must() == true) {
					mustCompons.add(compon); // 将控件放入list集合中
				}
				// 将字段为String类型且最大长度小于20的控件放入maxlengthCompons集合中，用于校验
				if(fieldClassSimpleName.equals("String")){
					if (input.maxlength() <= 20) {
						maxlengthCompons.add(compon); 
					}
				}
				// 判断字段input注解中是否标志了unique,当且仅当同时是多国语才可以用unique.其他要提示
				if (true == (input.unique())&& !TYPE.MULTLANG.equals(input.inputType())) {
					throw new RuntimeException("只能在多国语里面标注是unique!!!");
				}
				// 拿到字段类名,当注解nullable为false,若是包装类则取其基本类型(在调用的方法中进行的判断)
				compon.put("fieldClassSimpleName", getFieldClassSimpleName(field,input,fastEntity)); 
				compon.put("inputType", input.inputType().toString());//将控件的类型赋值给inputType供freemarker生成代码
				//判断字段是否为主键,这里注意是字符串比较不能用==或者!=
				if(fastEntity.entityId().equals(field.getName())){
					compon.put("unique", true);
					compon.put("nullable", false);
				}else{
					compon.put("unique", input.unique());
					compon.put("nullable", input.nullable());
				}
				//获取manyToOne标志的实体类对应的pojo
				if(!("".equals(input.manyToOne()))){
					try {
						Class entityClazz = Class.forName(input.manyToOne());
						if (entityClazz.isAnnotationPresent(FastEntity.class)) {
							//获取EntityBean类上标志的FastEntity注解类型的相应对象
							FastEntity manyToOneFastEntity = (FastEntity) entityClazz.getAnnotation(FastEntity.class);
							compon.put("manyToOnePojo", manyToOneFastEntity.pojoNm()); //拿到Pojo
						}
					} catch (ClassNotFoundException e) {
						throw new RuntimeException("The attribute Of manyToOne must error!");
					}
				}
				compon.put("typeCd", input.typeCd()); //拿到大分类
				compon.put("lblValBeanService", input.lblValBeanService());//拿到实体Service,供下拉框取实体.
				compon.put("precision", input.precision());
				compon.put("scale", input.scale());
				compons.add(compon); // 将控件放入list集合中
			}
		}
	}
	
	/**
	 *  获得字段的简单类名并进行相应的转换
	 *  封装类             --->    原始类型                       
	 *	Integer			int                    
	 *	Boolean			boolean                
	 *	Short			short                  
	 *	Long			long                   
	 *	Float			float                  
	 *	Double			double                
	 * @param field
	 * @param input
	 * @param fastEntity
	 * @return 
	 */
	private static String getFieldClassSimpleName(Field field,Input input,FastEntity fastEntity) {
		//拿到字段类名的简单类名
		String fieldClassSimpleName = field.getType().getSimpleName();
		//判断是否是主键(这里注意是字符串比较不能用==或者!=),若不是则继续判读内层if,想要达到的效果就是主键不用标注nullable,便可直接赋值,
		if(!fastEntity.entityId().equals(field.getName())){
			//判断是否标注了nullable=="false"(默认是true即可以为空),若标注了则得到字段已转换过的简单类名,若未标注则原样输出
			if(input.nullable()){
				return fieldClassSimpleName;
			}
		}
		switch (fieldClassSimpleName) {
		case "Integer":
			fieldClassSimpleName="int";break;
		case "Boolean":
			fieldClassSimpleName="boolean";break;
		case "Short":
			fieldClassSimpleName="short";break;
		case "Long":
			fieldClassSimpleName="long";break;
		case "Float":
			fieldClassSimpleName="float";break;
		case "Double":
			fieldClassSimpleName="double";break;
		default:
			break;
		}
		return fieldClassSimpleName;
	}
	
	/**
	 * 检查多国语其他两个字段xxxBeans和xxxCd是否存在同时判断类型是否匹配.注意泛型的提取
	 * @param field
	 * @param clazz
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	protected static void checkMultiLangField(Field field,Class clazz) throws NoSuchFieldException, SecurityException {
		String multiLang = field.getName();
		String multiLangFieldCd = field.getName()+"Cd";
		String multiLangFieldBeans = field.getName()+"Beans";
		
		Field findField1 = ReflectionUtils.findField(clazz, multiLangFieldCd, String.class);
		Field findField2 = ReflectionUtils.findField(clazz, multiLangFieldBeans, List.class);
		Type type = findField2.getGenericType();
		Type[] actTypeArgs = ((ParameterizedType)type).getActualTypeArguments();
		/*if(actTypeArgs.length>0){
			logger.info(actTypeArgs[0].toString());
		}*/
		//如果未找到对应的两个字段,或者泛型不是MultiLangBean则抛出运行时异常
		if(findField1 == null || findField2==null || !((Class) actTypeArgs[0]).getSimpleName().equals("MultiLangBean")){
			throw new RuntimeException("There need other two field "+multiLang+"Beans和"+multiLang+"Cd! At the same time genericType also need corresponding. Please check for sure!");
		}
	}
	
	/**
	 * 下拉框类型需要一个与字段对应的xxxNm,此方法为校验Bean中是否写了这样一个对应字段
	 * 这里需要注意的是:若以Cd结尾则需要去掉Cd再加Nm,此时再校验!
	 * @param field
	 * @param clazz
	 */
	protected static void checkSelectTypeField(Field field,Class clazz)  {
		String selectField = field.getName();
		/*if(selectField.endsWith("Cd")){
			selectField=selectField.substring(0, selectField.indexOf("Cd"));
		}*/
		String selectFieldNm = selectField+"Nm";
		Field findField = ReflectionUtils.findField(clazz, selectFieldNm, String.class);
		//如果未找到对应的那个xxNm字段,或者类型不是String则抛出运行时异常
		if(findField == null){
			throw new RuntimeException("There need the other field "+selectField+"Nm. Please check for sure!");
		}
	}
	
	/**
	 * 按照List中map的某一个key的值,将List中的map的位置进行重新排序
	 * @param compons
	 */
	protected static void sortList(List compons) {
		Collections.sort(compons,  new Comparator<Map<String, Object>>(){
			
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				int ret = 0;
				//比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
				ret = o1.get("length").toString().compareTo(o2.get("length").toString());//逆序的话就用o2.compareTo(o1)即可
				return ret;
			}
		} );
	}
	
	/**
	 * 检查指定类型是否有声明指定类名的注解
	 * @param clazz
	 * @param annotationClassNm 全类名
	 * @return true:是,false:否
	 */
	public static boolean checkIsDeclaredAnnotation(Class<?> clazz,String annotationClassNm){
		Annotation[] annos = clazz.getDeclaredAnnotations();
		for(Annotation ann : annos){
			if(ann.annotationType().getName().equals(annotationClassNm)){
				return true;
			}
		}
		return false;
	}
	
	
}