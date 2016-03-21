${gg.setOverride(true)}
<#include "/macro.include"/>
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.dao;

public interface ${className}Mapper<T> extends BaseMapper<T> {

}
