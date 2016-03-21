<#include "/macro.include"/>
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.service.imp;

import ${basepackage}.dao.${className}Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ${basepackage}.service.${className}Service;
@Service("${className}Service")
public class ${className}ServiceImp<T>  extends BaseServiceImp<T> implements ${className}Service<T> {        
         @Autowired 
         private ${className}Mapper<T> mapper; 
         
         public ${className}Mapper<T> getMapper() {
             return mapper;
         }
}
