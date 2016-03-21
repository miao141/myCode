${gg.setOverride(true)}
<#include "/macro.include"/>
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.consts;

/**
 * @description: The automatically generated ${className}Dao
 * @Package  ${basepackage}.dao
 * @Date	 ${.now?string("yyyy-MM-dd HH:mm:ss")}
 * @version  V1.0
 */
public class ${className}Consts {

	<#list table.columns as column>
	public static final String ${column.columnName} = "${column.columnNameLower}";
	</#list>

}
