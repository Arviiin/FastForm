package nextone.fastform.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Input {
	
	/**
	 * 类型枚举
	 * 分为:隐藏类型,普通输入类型，下拉框类型，滑块类型,多国语类型,文本类型,日期类型,日期含时间类型,数字类型
	 * @author jlzhuang
	 */
    public enum TYPE{ HIDDEN,CODE,SELECT,SWITCH,MULTLANG,TEXT,DATE,DATETIME,NUM};
    /**
     * 输入类型
     * @return
     */
    TYPE inputType() default TYPE.CODE;
	
	/**
	 * 最大输入长度
	 * @return
	 */
	public int maxlength() default -1;
	  
	/**
     * (Optional) The precision for a decimal (exact numeric)
     * column. (Applies only if a decimal column is used.)
     * Value must be set by developer if used when generating
     * the DDL for the column.
     */
	public int precision() default -1;
	
	/**
     * (Optional) The scale for a decimal (exact numeric) column.
     * (Applies only if a decimal column is used.)
     */
	public int scale() default -1;
	
	/**
     * (Optional) Whether the database column is nullable.
     */
	public boolean nullable() default true;
    
    /**
     * 是否必须不能为空
     * 默认可以为空
     * @return
     */
    public boolean must() default false;
    
    /**
     * 是否唯一
     * 默认可以不唯一
     * @return
     */
    public boolean unique() default false;
    
    /**
     * 是否需要小写转大写
     * @return
     */
    public boolean upperCase() default false;
    
    /**
     * 
     * enableFullWidthChar bool 是否允许输入全角字符（默认true,即是可以输入中文）
     * @return
     */
    public boolean enableFullWidthChar() default true;
    
    /**
	 * 是否只可读
	 * @return
	 */
	public String readOnly() default "false";
    
	/**
	 * 滑块是
	 * @return
	 */
	public String onValueTransl() default "";
	
	/**
	 * 滑块否
	 * @return
	 */
	public String offValueTransl() default "" ;
	
	
	/**
	 * 滑块"是"状态显示的字
	 * @return
	 */
	public String onText() default "label.yes";

	/**
	 * 滑块"否"状态显示的字
	 * @return
	 */
	public String offText() default "label.off";
	
	/**
	 * 大分类编号(可使用定义的常量)
	 * @return
	 */
	public String typeCd() default "";
	
	/**
	 * 下拉框与大分类编号相对应另一种表达方式(标注时有我没它typeCd)
	 * @return
	 */
	public String lblValBeanService() default "";
	
	/**
	 * 标签上显示的字
	 * @return
	 */
	public String labelMsgCd() default "";
	
	/**
	 * 隐藏属性存放位置
	 * @return
	 */
	public String hidAttributePath() default "";
	
	/**
	 * 隐藏属性id名
	 * @return
	 */
	public String hidAttributeId() default "";
	
	/**
	 * 名称类型
	 * @return
	 */
	public String nameType() default "";
	
	/**
	 * 表的多对一关系(也包括一对一关系)
	 * @return
	 */
	public String manyToOne() default "";
	
	/**
	 * 表的一对多关系
	 * @return
	 */
	public String oneToMany() default "";
	
	
}