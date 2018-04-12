package nextone.fastform.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 快速一览画面标志
 * @author jlzhuang
 * 注解元素必须有确定的值，要么在定义注解的默认值中指定，要么在使用注解时指定，
 * 非基本类型(Sting,Class,Enum,Annotation)的注解元素的值不可为null。
 * 若既没有默认值,又在使用时未指定则会报错.因此, 使用空字符串或-1作为默认值是一种常用的做法。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FastSearchForm {
	
	/**
	 * JSP表单头部说明描述即是此JSP的作用
	 * @return
	 */
	public String searchFormDesc() default "";

	/**
	 * 对应的增删改查程序编号
	 * @return
	 */
	public String crudPgmCd() default "";
	
	/**
	 * 对应的实体Bean.
	 * 注意要加包名
	 * @return
	 */
	public String entityBean() default "";
}