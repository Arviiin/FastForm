package nextone.fastform.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体类
 * 注解元素必须有确定的值，要么在定义注解的默认值中指定，要么在使用注解时指定，
 * 非基本类型(Sting,Class,Enum,Annotation)的注解元素的值不可为null。
 * 若既没有默认值,又在使用时未指定则会报错.因此, 使用空字符串或-1作为默认值是一种常用的做法。
 * @author jlzhuang
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FastEntity {
	/**
	 * 实体类对应的id
	 * @return
	 */
	public String entityId() default "";
	/**
	 * 实体类对应的pojo
	 * @return
	 */
	public String pojoNm() default "";
	/**
	 * 实体类对应的番号
	 * @return
	 */
	public String sequenceNm() default "";
	/**
	 * biz层的键
	 * @return
	 */
	public String bizKey() default "";
}