package nextone.fastform.xmlcore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 * 自动生成文件共通方法的工具类
 * @author jlzhuang
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TestGenerateUtils {
	public static String rootDirPath = null;//定义静态根路径变量
	public static String subAndChangeBeanPath = null;//定义静态部分路径变量
	public static String ModulePath = null;//定义静态模块路径变量
	private static Logger logger = LoggerFactory.getLogger(TestGenerateUtils.class);
	/**
	 * 截取输入参数类的全路径的部分,并将其转换,eg: nextone.master.bean.StudentBean   
	 * 截取nextone.master并将其转换为nextone/master/              
	 * @param formBeanFullClassPath
	 * @return
	 */
	public static String getSubAndChangeBeanPath(String formBeanFullClassPath){
		//字符串按小圆点进行分割,split(".") 直接这样写是不行的, 正确的写法是:对小圆点进行转义
		
		String[] beanSplitPath = formBeanFullClassPath.split("\\.");
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
	 * @param XmlPath
	 * @param data
	 * @param compons
	 * @param hiddenCompons
	 * @param switchCompons
	 * @param multiCompons
	 * @param beanFieldOfFormBeanName
	 * @param fieldClassName
	 * @throws DocumentException 
	 */
	protected static void getFieldClassAllParentData(String XmlPath,Map data,List compons,List hiddenCompons,
			List switchCompons,List multiCompons,String beanFieldOfFormBeanName,String fieldClassName) throws DocumentException {
			SAXReader saxReader=new SAXReader();
			Document doc = saxReader.read(XmlPath);
			Element rootElement = doc.getRootElement();
			if(rootElement.attributeValue("extendsBaseEntity")!=null){
			String superXmlPath = rootElement.attributeValue("extendsBaseEntity");
			// 将原本解析实体类的路径改为解析实体类的父类路径(比如CompanyBean的父类BaseEntityBean)从而遍历父类里面的元素
			getFiledDataOfEntityBeanForFormBean(superXmlPath, data, compons, hiddenCompons, switchCompons, multiCompons, beanFieldOfFormBeanName, fieldClassName);
			//递归调用获取父类的数据
			getFieldClassAllParentData(superXmlPath,data, compons, hiddenCompons, switchCompons, multiCompons, beanFieldOfFormBeanName, fieldClassName);
			}
	}
	
	/**
	 * 拿到标注了FastEntity注解的实体类(比如CompanyBean)里的所有字段,遍历每个字段,若字段上有input注解则解析并将数据放入集合
	 * @param XmlPath
	 * @param data
	 * @param compons
	 * @param hiddenCompons
	 * @param switchCompons
	 * @param multiCompons
	 * @param beanFieldOfFormBeanName
	 * @param fieldClassName
	 * @throws DocumentException 
	 */
	protected static void getFiledDataOfEntityBeanForFormBean(String XmlPath,Map data,List compons,List hiddenCompons,List switchCompons,List multiCompons,
			String beanFieldOfFormBeanName,String fieldClassName) throws DocumentException {
			SAXReader saxReader=new SAXReader();
			Document doc = saxReader.read(XmlPath);
			Element rootElement = doc.getRootElement();
			List<Element> childElements= rootElement.elements();
			  for(Element childElement:childElements){
			   if(childElement.getName().equals("fields")){
				 List<Element> fieldElements =childElement.elements();
				 for (Element fieldElement : fieldElements) {
				 Map compon=new HashMap();
				 String lblForCompoIdPrefix = null; 
				 //将元素的类型放在compon里面
				 compon.put("fieldType", fieldElement.attributeValue("fieldType"));
				 //将元素的属性中字段名字放在compon里面
				 compon.put("fieldName", fieldElement.attributeValue("fieldName"));
				 String fieldName=fieldElement.attributeValue("fieldName");
				 compon.put("fieldDBName", replaceChar(fieldElement.attributeValue("fieldName").toString()));
				 List<Element> attributeElements=fieldElement.elements();
				 //遍历field里面的属性，利用switch语句将属性一一赋值
				 for (Element attributeElement : attributeElements) {
				 switch (attributeElement.getName()) {
					case "inputType":
						compon.put("inputType", attributeElement.getTextTrim());
					break;
					case "hidAttributePath":
						compon.put("hidAttributePath", attributeElement.getTextTrim());
					break;
					case "hidAttributeId":
						compon.put("hidAttributeId", attributeElement.getTextTrim());
					break;
					case "precision":
						compon.put("precision", attributeElement.getTextTrim());
				    break;
				    case "scale":
				    	compon.put("scale", attributeElement.getTextTrim());
					break;
					case "maxlength":
						compon.put("maxlength", attributeElement.getTextTrim());
					break;
					case "must":
						compon.put("must", attributeElement.getTextTrim());
					break;
					case "upperCase":
						compon.put("upperCase", attributeElement.getTextTrim());
					break;
					case "onValueTransl":
						compon.put("onValueTransl", attributeElement.getTextTrim());
						compon.put("onValueTranslJava", attributeElement.getTextTrim().replace("'", "\""));// 滑块否标志
					break;
					case "offValueTransl":
						compon.put("offValueTransl", attributeElement.getTextTrim());
						compon.put("offValueTranslJava", attributeElement.getTextTrim().replace("'", "\""));// 滑块否标志
					break;
					case "onText":
						compon.put("onText", attributeElement.getTextTrim());
						break;
					case "offText":
						compon.put("offText", attributeElement.getTextTrim());
						break;
					case "labelMsgCd":
						compon.put("labelMsgCd", attributeElement.getTextTrim());
						break;
					case "unique":
					compon.put("unique", attributeElement.getTextTrim());
						break;
					case "typeCd":
					compon.put("typeCd",  attributeElement.getTextTrim());
						break;
					case "readOnly":
					compon.put("readOnly",  attributeElement.getTextTrim());
						break;
					case "nullable":
					compon.put("nullable",  attributeElement.getTextTrim());
						break;
					case "nameType":
					compon.put("nameType",  attributeElement.getTextTrim());
						break;
					case "enableFullWidthChar":
					compon.put("enableFullWidthChar",  attributeElement.getTextTrim());
						break;
					case "oneToMany":
					compon.put("oneToMany",  attributeElement.getTextTrim());
						break;	
					case "manyToOne":
					compon.put("manyToOne",  attributeElement.getTextTrim());
						break;	
					case "lblValBeanService":
					compon.put("lblValBeanService",  attributeElement.getTextTrim());
						break;
					default:
						break;
				 }
				 }
				 //若field不含有某些属性调用inneedMap方法将未标注的属性赋值（默认值）
				 inneedMap(compon);
				 //如果属性inputType为HIDDEN则将该field以及属性全部放在hiddencompons中
				 if(compon.get("inputType").equals("HIDDEN")){
					 hiddenCompons.add(compon); 
				 }
				 //如果属性inputType为TEXT则将该field以及属性全部放在hiddencompons中
				 if(compon.get("inputType").equals("TEXT")||compon.get("inputType").equals("code")){
					 lblForCompoIdPrefix = "txt";
					// 当为普通输入,或者文本类型时,检查其有无显示定义maxleng(默认值为-1),若无则报错,
					 if((Integer.parseInt(compon.get("maxlength").toString()))==-1){
						throw new RuntimeException("please input the maxlength of code:"+ fieldElement.attributeValue("fieldName"));
						} else {
						compon.put("maxlength", compon.get("maxlength")); //最大长度
						} 
					 }
				 if(compon.get("inputType").equals("SELECT")){
					 lblForCompoIdPrefix = "sel";
						// 当为下拉框输入类型时,检查其有无显示定义maxleng(默认值为-1),若无则将其设置为20,
						if ((Integer.parseInt(compon.get("maxlength").toString()))==-1) {
							compon.put("maxlength", 20); // 最大长度
						} else {
							compon.put("maxlength", compon.get("maxlength")); // 最大长度
						}	 
				 }
				 if(compon.get("inputType").equals("SWITCH")){
						lblForCompoIdPrefix = "chk";
						switchCompons.add(compon); // 将控件放入滑块list集合中
				 }
				 if(compon.get("inputType").equals("MULTLANG")){
					 	lblForCompoIdPrefix = "mul";
					// 当为多国语输入类型时,检查其有无显示定义maxleng(默认值为-1),若无则将其设置为40,
					if ((Integer.parseInt(compon.get("maxlength").toString()))==-1) {
						compon.put("maxlength", 40); // 最大长度
					} else {
						compon.put("maxlength",compon.get("maxlength")); // 最大长度
					}
					multiCompons.add(compon);// 将控件放入多国语list集合中
				 }
				 if(compon.get("inputType").equals("DATATIME")){
					 lblForCompoIdPrefix = "dat";
				 }
				 if(compon.get("inputType").equals("NUM")){
					 lblForCompoIdPrefix = "num";
				 }
			    compon.put("formInOrOutputPath", beanFieldOfFormBeanName+ "." + fieldName);// 控件接收或者读取数据的位置
				compon.put("labelForComponentId",lblForCompoIdPrefix + fieldClassName + "_"+ fieldName); // 点label光标跳到哪个控件,也即是那个控件的id,
				compon.put("labelId", "lbl" + fieldClassName+ "_" + fieldName); // label的id,
				compon.put("spanId", "span" + fieldClassName+ "_" + fieldName); // span的id,	
				if("".equals(compon.get("labelMsgCd"))){
					compon.put("labelMsgCd", fieldName);
				}else {
					compon.put("labelMsgCd", compon.get("labelMsgCd"));
				}
				if (compon.get("upperCase").equals(true)) {
					data.put("upperCaseFlg", true); // 是否存在有字段在注解中定义了需要转换大小写的标志(若有则在script部分加上$("input.upperCase").textInputLower2Upper();)
				}
				compon.put("entityBean", beanFieldOfFormBeanName);
				compons.add(compon);
			   }
			  }
		}
		
	}
	/**
	 * 获取使用FreeMarker生成FormBean相关文件所需的相应数据data
	 * @param clazz:即是FormBean的完整类名
	 * @return 返回值为Map类型
	 * @throws DocumentException 
	 */
	public static Map getDataForFormBean(String XmlPath) throws DocumentException{
		logger.info("数据开始读取");
		// 数据源
		Map data=new HashMap();// 创建map容器存放所有的数据
		List compons = new ArrayList();// 创建list容器存放entityBean中所有添加注解字段的相关数据
		List hiddenCompons = new ArrayList();// 创建list容器存放注解为隐藏字段的相关数据
		List switchCompons = new ArrayList();// 创建list容器存放注解为滑块字段的相关数据
		List multiCompons = new ArrayList();// 创建list容器存放注解为多国语字段的相关数据
		SAXReader saxReader=new SAXReader();
		Document doc = saxReader.read(XmlPath);
		Element rootElement = doc.getRootElement();
		// 截取类所在包名的前一部分
		String fullClassName = rootElement.attributeValue("formbeanxmlpath");
	    data.put("formbeanxmlpath", fullClassName);
		String prePackage = fullClassName.substring(0,fullClassName.indexOf(".", fullClassName.indexOf(".") + 1));
		String simpleClassName=fullClassName.substring(fullClassName.lastIndexOf(".")+1);
		logger.info(simpleClassName+ "上有FastForm注解");
		// 截取FormBean里的程序编号和模块编号分别赋值给pgmCd,moduleCd
		String pgmCd = TestGenerateUtils.subForPgmCd(simpleClassName);
		String moduleCd = simpleClassName.substring(0, 2).toLowerCase();
		String packageCd = simpleClassName.substring(0, 4).toLowerCase();
		data.put("moduleCd", moduleCd);
		data.put("pgmCd", pgmCd);// 作者编号
		data.put("pgmController", pgmCd + "Controller");
		data.put("pgmFormBean", pgmCd + "FormBean");
		data.put("pgmService", pgmCd + "Service");
		data.put("pgmFormBeanValidator", pgmCd + "FormBeanValidator");
		data.put("prePackage", prePackage);
		data.put("packageCd", packageCd);
		// 获取当前系统时间,并取其数字形式,用于JSP中引入外部js代码更换版本号(若不改变版本号,即便改变js代码,引用的还是未改之前的)
		Date date = new Date();// 默认取当前系统时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		String versionNum = dateFormat.format(date);
		data.put("versionNum", versionNum);
		//获取FormBean.Xml上根节点下的元素
	    List<Element> childElements=rootElement.elements();
	    for (Element childElement : childElements) {
	    	switch (childElement.getName().toString()) {
	    	case "formId":// 主键
	    	data.put("formId",childElement.getTextTrim());
	    	break;
	    	case "formDesc":// JSP画面描述
	    	data.put("formDesc",childElement.getTextTrim());
	    	break;
	    	case "fields":
		    	List<Element> fieldsElements=childElement.elements();
		    	for (Element fieldsElement : fieldsElements) {
		    	//遍历FormBean的所有字段,若某字段为实体类型且相应实体类在其定义时具有属性。
		    	if(rootElement.attributeValue("connectType").equals(fieldsElement.attributeValue("fieldType"))){
				logger.info(fieldsElement.getText()+ "字段:它是FormBean中的字段对应的实体类在定义时标注了@FastEntity注解");
				String beanFieldOfFormBeanName = fieldsElement.getText();//字段名(比如companyBean)
				data.put("beanFieldOfFormBeanName",beanFieldOfFormBeanName);
				String fieldClassName =fieldsElement.attributeValue("fieldType");// 获取字段名 的简单类名(比如CompanyBean)
				logger.info(fieldClassName);
				int ipos1 = fieldClassName.indexOf("Bean");
				String entityName = fieldClassName.substring(0, ipos1);
				data.put("entityService", entityName + "Service");
				data.put("entityBean", fieldClassName);//注意这个和下面那个不一样,这个是简单类名,而不是字段,比如(CompanyBean)
				data.put("entityBeanPackage", fieldsElement.attributeValue("connectpath"));//获得该字段里面的路径信息
				String connectXmlPath=fieldsElement.attributeValue("connectXmlPath");//解析connectXPath路径信息
				//遍历FormBean中有实体类，若某字段有属性则进行相应处理
				getFiledDataOfEntityBeanForFormBean(connectXmlPath, data, compons, hiddenCompons, switchCompons, multiCompons, beanFieldOfFormBeanName, fieldClassName);
				//递归获取父类的数据
				getFieldClassAllParentData(connectXmlPath,data, compons, hiddenCompons, switchCompons, multiCompons, beanFieldOfFormBeanName, fieldClassName);
			}
		}
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
	 * @throws DocumentException 
	 */
	public static Map getDataForEntityBean(String XmlPath) throws NoSuchFieldException, SecurityException, DocumentException{
		
		logger.info("数据开始读取");
		Map data =null;
			List compons = new ArrayList();// 创建list容器存放各字段的相关数据
			List mustCompons = new ArrayList();// 创建list容器存放不能为空字段的相关数据
			List codeCompons = new ArrayList();// 创建list容器存放code字段的相关数据
			List switchCompons = new ArrayList();// 创建list容器存放滑块字段的相关数据
			List maxlengthCompons = new ArrayList();// 创建list容器存放最大长度为20字段的相关数据
			List multiCompons = new ArrayList();// 创建list容器存多国语字段的相关数据
			data=new HashMap();
			SAXReader saxReader=new SAXReader();
			Document doc = saxReader.read(XmlPath);
			Element rootElement = doc.getRootElement();
			String fullClassName = rootElement.attributeValue("EntityBeanxmlpath");
			data.put("EntityBeanxmlpath", fullClassName);
			String prePackage = fullClassName.substring(0,fullClassName.indexOf(".", fullClassName.indexOf(".") + 1));
			String fieldClassName=fullClassName.substring(fullClassName.lastIndexOf(".")+1);
			String entityName = fieldClassName.substring(0, fieldClassName.indexOf("Bean"));
			List<Element> childElements= rootElement.elements();
		    for (Element childElement : childElements) {
		    	switch (childElement.getName().toString()) {
				case "bizKey":
					String bizKey=childElement.getTextTrim();
		    		String pre_bizKey=bizKey.substring(0,bizKey.indexOf("Cd"));
		    		data.put("pre_bizKey", pre_bizKey);	
		    		data.put("bizKey",bizKey);
					break;
				case "SequenceNm":
					data.put("SequenceNm",childElement.getTextTrim());
					break;	
				case "pojoNm":
					data.put("pojoNm",childElement.getTextTrim());
					break;
				case "entityId":
					data.put("entityId",childElement.getTextTrim());
					data.put("entityIdDBName",replaceChar(childElement.getTextTrim()));// 实体类的主键在数据库的表中的名字.
					break;
				default:
					break;
				}
		    }
		    data.put("entityName", entityName);
		    data.put("entityBean",  fieldClassName);
		    data.put("entityBeanPackage", fullClassName);
		    data.put("prePackage", prePackage);
			// 拿到实体类(比如:CompanyBean)里的所有字段,然后遍历每个字段,若字段上有系列属性则解析并将数据放入集合
			getFiledOfEntityBeanDataModel(XmlPath, compons, mustCompons, codeCompons, switchCompons, multiCompons, maxlengthCompons);
			
			//递归获取父类的数据
			getAllParentData(XmlPath, compons, mustCompons, codeCompons, switchCompons, multiCompons, maxlengthCompons);
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
	public static Map getDataForSearchForm(String XmlPath) throws Exception{
		logger.info(XmlPath+"");
		Map data=null;
		// 截取一览FormBean里的程序编号和模块编号分别赋值给searchFormPgmCd,searchFormModuleCd
		data=new HashMap();
		List compons = new ArrayList();// 创建list容器存放各字段的相关数据
		List mustCompons = new ArrayList();// 创建list容器存放不能为空字段的相关数据
		List codeCompons = new ArrayList();// 创建list容器存放code字段的相关数据
		List switchCompons = new ArrayList();// 创建list容器存放滑块字段的相关数据
		List maxlengthCompons = new ArrayList();// 创建list容器存放最大长度为20字段的相关数据
		List multiCompons = new ArrayList();// 创建list容器存多国语字段的相关数据
		SAXReader saxReader=new SAXReader();
		Document doc = saxReader.read(XmlPath);
		Element rootElement = doc.getRootElement();
		String fullClassName = rootElement.attributeValue("searchformbeanxmlpath");
		data.put("searchformbeanxmlpath", fullClassName);
		String simpleClassName = rootElement.attributeValue("SearchFormname");
		String searchFormPgmCd = TestGenerateUtils.subForPgmCd(simpleClassName);
		
		String searchFormModuleCd = simpleClassName.substring(0, 2).toLowerCase();
		
		String searchFormPackageCd = simpleClassName.substring(0, 4).toLowerCase();
		
		String connectionXmlPath=rootElement.attributeValue("connectionXmlPath");
		//获取一览FormBean(类)上标注的FastSearchForm注解类型的对象
		List<Element> childElements=rootElement.elements();
		for (Element childElement : childElements) {
		switch (childElement.getName().toString()) {
		case "crudPgmCd":
			String crudPgmCd =	childElement.getTextTrim();
			String crudModuleCd = crudPgmCd.substring(0, 2).toLowerCase();
			String crudPackageCd = crudPgmCd.substring(0, 4).toLowerCase();
			data.put("crudPgmCd", crudPgmCd);// 增删改查画面编号
			data.put("crudModuleCd", crudModuleCd);// 模块编号
			data.put("crudPackageCd", crudPackageCd);
			break;
		case "entityBean":
		fullClassName = childElement.getTextTrim();
		String prePackage = fullClassName.substring(0,fullClassName.indexOf(".", fullClassName.indexOf(".") + 1));
		data.put("prePackage", prePackage);
			SAXReader EntitysaxReader=new SAXReader();
			Document Entitydoc = EntitysaxReader.read(connectionXmlPath);
			Element EntityrootElement = Entitydoc.getRootElement();
			List<Element> EntitychildElements= EntityrootElement.elements();
			for(Element EntitychildElement:EntitychildElements){
				switch (EntitychildElement.getName().toString()) {
			    case "entityId":
					data.put("entityId",EntitychildElement.getTextTrim());
					data.put("entityIdDBName",replaceChar(EntitychildElement.getTextTrim()));
					break;
				case "pojoNm":
					data.put("pojoNm",EntitychildElement.getTextTrim());
					break;
				case "bizKey":
					data.put("bizKey",EntitychildElement.getTextTrim());
					break;
				default:
					break;
				}
				}
				//遍历实体类的所有字段,若某字段在其定义时标注了属性则进行相应处理.
				getFiledOfEntityBeanDataModel(connectionXmlPath, compons, mustCompons, codeCompons, switchCompons, multiCompons, maxlengthCompons);
				//递归获取父类的数据
				getAllParentData(connectionXmlPath, compons, mustCompons, codeCompons, switchCompons, multiCompons, maxlengthCompons);
		default:
			break;
		}
		
		}
		data.put("searchFormPgmCd", searchFormPgmCd);// 加入检索画面编号
		data.put("searchFormModuleCd", searchFormModuleCd);// 加入检索画面编号
		data.put("components", compons); // 将上面创建的list容器放入map中
		data.put("searchFormPackageCd", searchFormPackageCd);// 加入检索画面编号
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
	 * @throws DocumentException 
	 */
	protected static void getAllParentData(String XmlPath,List compons,List mustCompons,List codeCompons,List switchCompons,List multiCompons,List maxlengthCompons) throws NoSuchFieldException, SecurityException, DocumentException {
		SAXReader saxReader=new SAXReader();
		Document doc = saxReader.read(XmlPath);
		Element rootElement = doc.getRootElement();
		if(rootElement.attributeValue("extendsBaseEntity")!=null){
		String superXmlPath = rootElement.attributeValue("extendsBaseEntity");
			// 拿到具有属性的实体类的父类(比如CompanyBean的父类BaseEntityBean)里的所有字段,遍历每个字段,若字段上有i属性则解析并将数据放入集合
			getFiledOfEntityBeanDataModel(superXmlPath, compons, mustCompons, codeCompons, switchCompons, multiCompons, maxlengthCompons);
			//递归获取父类的数据
			getAllParentData(superXmlPath, compons, mustCompons, codeCompons, switchCompons, multiCompons, maxlengthCompons);
		}
	}
	
	/**
	 * 拿到标注了FastEntity注解的实体类里的所有字段
	 * 遍历每个字段,若字段上有input注解则解析并将数据放入集合
	 * @param XmlPath
	 * @param compons
	 * @param fastEntity
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws DocumentException 
	 */
	protected static void getFiledOfEntityBeanDataModel(String XmlPath,List compons,List mustCompons,List codeCompons,List switchCompons,List multiCompons,List maxlengthCompons) throws NoSuchFieldException, SecurityException, DocumentException {
		SAXReader saxReader=new SAXReader();
		Document doc = saxReader.read(XmlPath);
		Element rootElement = doc.getRootElement();
		List<Element> childElements= rootElement.elements();
		  String mainkey=null;
		 for(Element childElement:childElements){
				//如果他的子目录是fields
			if(childElement.getName().equals("entityId")){
				mainkey=childElement.getText();
			}
			if(childElement.getName().equals("fields")){
				 List<Element> fieldElements =childElement.elements();
				 for (Element fieldElement : fieldElements) {
				 Map compon=new HashMap();
				 compon.put("fieldType", fieldElement.attributeValue("fieldType"));
				 String fieldClassSimpleName=fieldElement.attributeValue("fieldType");
				 compon.put("fieldName", fieldElement.attributeValue("fieldName"));
				 String fieldName=fieldElement.attributeValue("fieldName");
				 compon.put("fieldDBName", replaceChar(fieldName.toString()));
				 List<Element> attributeElements=fieldElement.elements();
				 for (Element attributeElement : attributeElements) {
						switch (attributeElement.getName()) {
						case "inputType":
							compon.put("inputType", attributeElement.getTextTrim());
						break;
						case "hidAttributePath":
							compon.put("hidAttributePath", attributeElement.getTextTrim());
						break;
						case "hidAttributeId":
							compon.put("hidAttributeId", attributeElement.getTextTrim());
						break;
						case "precision":
							compon.put("precision", attributeElement.getTextTrim());
					    break;
					    case "scale":
					    	compon.put("scale", attributeElement.getTextTrim());
						break;
						case "maxlength":
							compon.put("maxlength", attributeElement.getTextTrim());
						break;
						case "must":
							compon.put("must", attributeElement.getTextTrim());
						break;
						case "upperCase":
							compon.put("upperCase", attributeElement.getTextTrim());
						break;
						case "onValueTransl":
							compon.put("onValueTransl", attributeElement.getTextTrim());
							compon.put("onValueTranslJava", attributeElement.getTextTrim().replace("'", "\""));// 滑块否标志
						break;
						case "offValueTransl":
							compon.put("offValueTransl", attributeElement.getTextTrim());
							compon.put("offValueTranslJava", attributeElement.getTextTrim().replace("'", "\"")); // 滑块否标志(java中使用"1")
							break;
						case "onText":
							compon.put("onText", attributeElement.getTextTrim());
							break;
						case "offText":
							compon.put("offText", attributeElement.getTextTrim());
							break;
						case "labelMsgCd":
							compon.put("labelMsgCd", attributeElement.getTextTrim());
							break;
						case "unique":
						compon.put("unique", attributeElement.getTextTrim());
							break;
						case "typeCd":
						compon.put("typeCd",  attributeElement.getTextTrim());
							break;
						case "readOnly":
						compon.put("readOnly",  attributeElement.getTextTrim());
							break;
						case "nullable":
						compon.put("nullable",  attributeElement.getTextTrim());
							break;
						case "nameType":
						compon.put("nameType",  attributeElement.getTextTrim());
							break;
						case "enableFullWidthChar":
						compon.put("enableFullWidthChar",  attributeElement.getTextTrim());
							break;
						default:
							break;
						}
					
					}
				 inneedMap(compon);
					if(compon.get("inputType").equals("CODE")){
						codeCompons.add(compon);
				    }
					if(compon.get("inputType").equals("SWITCH")){
						switchCompons.add(compon); 
					}
					if(compon.get("inputType").equals("MULTLANG")){	
						compon.put("fieldName", compon.get("fieldName").toString()+"Cd"); // 对原始字段名进行修改以达到想要的结果
						compon.put("fieldDBName", replaceChar(compon.get("fieldName").toString()));
						multiCompons.add(compon);	
					}
					if(compon.get("fieldName").equals("createUserNm") || compon.get("fieldName").equals("updateUserNm")){
						String joinFiled = compon.get("fieldName").toString().substring(0, compon.get("fieldName").toString().indexOf("Nm"));
						compon.put("joinFiled", joinFiled);
					}
					if(compon.get("must").toString().equals("true")){
						mustCompons.add(compon);
					}
					if(fieldClassSimpleName.equals("String")){
						if((Integer.parseInt(compon.get("maxlength").toString()))<=20){
						maxlengthCompons.add(compon); 	
					}
					}
				
					if(compon.get("inputType").equals("NUM")){
						if((Integer.parseInt(compon.get("precision").toString()))==-1||(Integer.parseInt(compon.get("scale").toString()))==-1)
							throw new RuntimeException("please input the precision,scale of field:"+ compon.get("fieldName"));
						compon.put("maxlength", ((Integer.parseInt(compon.get("precision").toString()))-(Integer.parseInt(compon.get("scale").toString())))/3+2+(Integer.parseInt(compon.get("precision").toString())));
					}
					
				if(mainkey.equals(compon.get("fileName"))){
					compon.put("unique", true);
					compon.put("nullable", false);
				}else{
					compon.put("unique", compon.get("unique"));
					compon.put("nullable",compon.get("nullable"));
				}
				compon.put("fieldClassSimpleName", getFieldClassSimpleName(fieldClassSimpleName));
				compons.add(compon);
				 }
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
	 * @throws DocumentException 
	 */
	private static String getFieldClassSimpleName(String fieldClassSimpleName) throws DocumentException {
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
	 * @throws DocumentException 
	 */
	protected static void checkMultiLangField(String fieldName,String XmlPath) throws NoSuchFieldException, SecurityException, DocumentException {
		String multiLang =fieldName;
		String multiLangFieldCd = fieldName+"Cd";
		String multiLangFieldBeans = fieldName+"Beans";
		
		Field findField1 = ReflectionUtils.findField(XmlPath.getClass(), multiLangFieldCd, String.class);
		Field findField2 = ReflectionUtils.findField(XmlPath.getClass(), multiLangFieldBeans, List.class);
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
	protected static void checkSelectTypeField(String fieldName,String XmlPath)  {
		String selectField = fieldName;
		/*if(selectField.endsWith("Cd")){
			selectField=selectField.substring(0, selectField.indexOf("Cd"));
		}*/
		String selectFieldNm = selectField+"Nm";
		Field findField = ReflectionUtils.findField(XmlPath.getClass(), selectFieldNm, String.class);
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
	 * 检查字段里面是否不具有某些属性
	 * 若无该属性则将赋值默认值
	 * 其中某些属性用字符串形式写true或者false，同意换成Boolean型
	 * 只有readonly属性为字符串的类型
	 * @param compon
	 *
	 */
 protected static void inneedMap(Map compon){
	 if(compon.containsKey("hidAttributePath")==false){
		 compon.put("hidAttributePath", "");
	 }
	 if(compon.containsKey("hidAttributeId")==false){
		 compon.put("hidAttributeId", "");
	 }
	 if(compon.containsKey("labelMsgCd")==false){
		 compon.put("labelMsgCd", "");
	 } 
	 if(compon.containsKey("nameType")==false){
		 compon.put("nameType", "");
	 } 
	 if(compon.containsKey("maxlength")==false){
		 compon.put("maxlength", -1);
	 } 
	 if(compon.containsKey("precision")==false){
		 compon.put("precision", -1);
	 } 
	 if(compon.containsKey("scale")==false){
		 compon.put("scale", -1);
	 }
	 if(compon.containsKey("nullable")==false){
		 compon.put("nullable", true);
	 } 
	 if(compon.containsKey("must")==false){
		 compon.put("must", false);
	 } 
	 if(compon.containsKey("unique")==false){
		 compon.put("unique", false);
	 }
	 if(compon.containsKey("upperCase")==false){
		 compon.put("upperCase", false);
	 } 
	 if(compon.containsKey("enableFullWidthChar")==false){
		 compon.put("enableFullWidthChar", true);
	 } 
	 if(compon.containsKey("readOnly")==false){
		 compon.put("readOnly", "false");
	 } 
	 if(compon.containsKey("onValueTransl")==false){
		 compon.put("onValueTransl", "");
	 }
	 if(compon.containsKey("offValueTransl")==false){
		 compon.put("offValueTransl", "");
	 } 
	 if(compon.containsKey("onText")==false){
		 compon.put("onText", "");
	 } 
	 if(compon.containsKey("offText")==false){
		 compon.put("offText", "");
	 } 
	 if(compon.containsKey("typeCd")==false){
		 compon.put("typeCd", "");
	 }
	 if(compon.containsKey("lblValBeanService")==false){
		 compon.put("lblValBeanService", "");
	 }
	 if(compon.containsKey("manyToOne")==false){
		 compon.put("manyToOne", "");
	 }
	 if(compon.containsKey("oneToMany")==false){
		 compon.put("oneToMany", "");
	 }
	 if(compon.get("must").equals("true")){
		 compon.put("must", true);
	 }
	 if(compon.get("nullable").equals("false")){
		 compon.put("nullable", false);
	 }
	 if(compon.get("unique").equals("true")){
		 compon.put("unique", true);
	 }
	 if(compon.get("upperCase").equals("true")){
		 compon.put("upperCase", true);
	 } 
	
	 if(compon.get("enableFullWidthChar").equals("false")){
		 compon.put("enableFullWidthChar", false);
	 } 
 }
}