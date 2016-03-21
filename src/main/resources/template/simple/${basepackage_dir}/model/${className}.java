<#include "/macro.include"/>
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.model;

<#include "/java_imports.include">
/**
 * @description: The automatically generated ${className}
 * @table ${table.sqlName}   ${table.remarks}
 * @Package  ${basepackage}.model
 * @Date	 ${.now?string("yyyy-MM-dd HH:mm:ss")}
 * @version  V1.0
 */
public class ${className} {

	<#list table.columns as column>
	/**
     * 描述:${column.columnAlias!}
     * 字段:${column.sqlName}  ${column.sqlTypeName?lower_case}(${column.size})
     */
	private ${column.simpleJavaType} ${column.columnNameLower};
	<#if column.isDateTimeColumn>
	//【非数据库字段，查询时使用】
	private ${column.simpleJavaType} ${column.columnNameLower}Begin;
	//【非数据库字段，查询时使用】
	private ${column.simpleJavaType} ${column.columnNameLower}End;
	</#if>

	</#list>
	<@generateJavaColumns/>
	<@generateJavaOneToMany/>
	<@generateJavaManyToOne/>
}

<#macro generateJavaColumns>
	<#list table.columns as column>
	public void set${column.columnName}(${column.simpleJavaType} ${column.columnNameLower}) {
		this.${column.columnNameLower} = ${column.columnNameLower};
	}
	public ${column.simpleJavaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}

	<#if column.isDateTimeColumn>
    public void set${column.columnName}Begin(${column.simpleJavaType} ${column.columnNameLower}Begin) {
		this.${column.columnNameLower}Begin = ${column.columnNameLower}Begin;
	}
	public ${column.simpleJavaType} get${column.columnName}Begin() {
		return this.${column.columnNameLower}Begin;
	}
	public void set${column.columnName}End(${column.simpleJavaType} ${column.columnNameLower}End) {
		this.${column.columnNameLower}End = ${column.columnNameLower}End;
	}
	public ${column.simpleJavaType} get${column.columnName}End() {
		return this.${column.columnNameLower}End;
	}
	</#if>
	</#list>
</#macro>

<#macro generateJavaOneToMany>
	<#list table.exportedKeys.associatedTables?values as foreignKey>
	<#assign fkSqlTable = foreignKey.sqlTable>
	<#assign fkTable    = fkSqlTable.className>
	<#assign fkPojoClass = fkSqlTable.className>
	<#assign fkPojoClassVar = fkPojoClass?uncap_first>

	private Set ${fkPojoClassVar}s = new HashSet(0);
	public void set${fkPojoClass}s(Set<${fkPojoClass}> ${fkPojoClassVar}){
		this.${fkPojoClassVar}s = ${fkPojoClassVar};
	}

	public Set<${fkPojoClass}> get${fkPojoClass}s() {
		return ${fkPojoClassVar}s;
	}
	</#list>
</#macro>

<#macro generateJavaManyToOne>
	<#list table.importedKeys.associatedTables?values as foreignKey>
	<#assign fkSqlTable = foreignKey.sqlTable>
	<#assign fkTable    = fkSqlTable.className>
	<#assign fkPojoClass = fkSqlTable.className>
	<#assign fkPojoClassVar = fkPojoClass?uncap_first>

	private ${fkPojoClass} ${fkPojoClassVar};

	public void set${fkPojoClass}(${fkPojoClass} ${fkPojoClassVar}){
		this.${fkPojoClassVar} = ${fkPojoClassVar};
	}

	public ${fkPojoClass} get${fkPojoClass}() {
		return ${fkPojoClassVar};
	}
	</#list>
</#macro>
