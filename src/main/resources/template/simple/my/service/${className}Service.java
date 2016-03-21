<#include "/macro.include"/>
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.service;

import ${basepackage}.dao.${className}Mapper;
public interface ${className}Service<T>  extends BaseService<T> , ${className}Mapper<T> {    

}