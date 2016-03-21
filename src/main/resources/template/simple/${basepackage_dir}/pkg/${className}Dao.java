${gg.setOverride(true)}
<#include "/macro.include"/>
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage};

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @description: The automatically generated ${className}Dao
 * @Package  ${basepackage}
 * @Date	 ${.now?string("yyyy-MM-dd HH:mm:ss")}
 * @version  V1.0
 */
public interface ${className}Dao {

	public enum Enum${className}{
		<#list table.columns as column>${column?upper_case}<#if column_has_next>,</#if></#list>
	}

	String pkey = "key";
	String pval = "val";
	String table = "${table.sqlName}";
	String selCols = "<#list table.columns as column>${column.sqlName}<#if column.columnNameLower != column.sqlName> ${column.columnNameLower}</#if><#if column_has_next>,</#if></#list>";
	String insCols = "<#list table.columns as column>${column.sqlName}<#if column_has_next>,</#if></#list>";
	String insVals = "<#list table.columns as column>${'#{'}${column.columnNameLower}${'}'}<#if column_has_next>,</#if></#list>";
	String updCols = "<#list table.notPkColumns as column>${column.sqlName}=<@mapperEl column.columnNameFirstLower/><#if column_has_next>,</#if></#list>";
	String insMulVals = "<#list table.columns as column>${'#{'}it.${column.columnNameLower}${'}'}<#if column_has_next>,</#if></#list>";

	<#list table.columns as column>
	<#if column.pk>
	/** select by pk ${column.sqlName} **/
	@SelectProvider(type=SqlProvider.class, method="getByPk")
	public ${className} getBy${column.columnName}(<@pkparam/>);

	/** delete by pk ${column.sqlName} **/
	@DeleteProvider(type=SqlProvider.class, method="delByPk")
    public boolean delBy${column.columnName}(<@pkparam/>);

    /** update by pk ${column.sqlName} **/
    @UpdateProvider(type=SqlProvider.class, method="updByPk")
	public boolean updBy${column.columnName}(${className} ${classNameLower});

	</#if>
	<#if column.unique && !column.pk>
	/** select by unique ${column.sqlName} **/
	@SelectProvider(type=SqlProvider.class, method="getBy${column.columnName}")
	public ${className} getBy${column.columnName}(${column.simpleJavaType} ${column.columnNameLower});

	/** delete by unique ${column.sqlName} **/
	@DeleteProvider(type=SqlProvider.class, method="delBy${column.columnName}")
    public boolean delBy${column.columnName}(${column.simpleJavaType} ${column.columnNameLower});

    /** update by unique ${column.sqlName} **/
    @UpdateProvider(type=SqlProvider.class, method="updBy${column.columnName}")
	public boolean updBy${column.columnName}(${className} ${classNameLower});

	</#if>
	</#list>

	/** count all record **/
	@SelectProvider(type=SqlProvider.class, method="count")
	public Integer count();

	/** count by where key=val **/
	@SelectProvider(type=SqlProvider.class, method="countByKV")
	public Integer countByKV(@Param(pkey)Enum${className} key, @Param(pval)Object val);

	/** count by where key1=val1 and key2=val2 ... **/
	@SelectProvider(type=SqlProvider.class, method="countByMap")
	public Integer countByMap(Map<String, Object> params);

	@SelectProvider(type=SqlProvider.class, method="countByModel")
	public Integer countByModel(${className} ${classNameLower});

	/** select all record **/
	@SelectProvider(type=SqlProvider.class, method="getAll")
	public List<${className}> getAll();

	/** select List<${className}> by where key=val **/
	@SelectProvider(type=SqlProvider.class, method="getByKV")
	public List<${className}> getByKV(@Param(pkey)Enum${className} key, @Param(pval)Object val);

	/** select List<${className}> by where key1=val1 and key2=val2 ... **/
	@SelectProvider(type=SqlProvider.class, method="getByMap")
	public List<${className}> getByMap(Map<String, Object> params);

	@SelectProvider(type=SqlProvider.class, method="getByModel")
	public List<${className}> getByModel(${className} ${classNameLower});

	/** select List<${className}> by where key1=val1 and key2=val2 ... Order by col Limit m,n**/
	@SelectProvider(type=SqlProvider.class, method="getByMapPage")
	public List<${className}> getByMapPage(@Param("param")Map<String, Object> params, @Param("lo")Pager lo);

	/** insert a ${className} **/
	@Insert("insert into " + table + "(" + insCols + ") values(" + insVals + ")")
	@SelectKey(statement="select last_insert_id()", keyProperty="id", before=false, resultType=Integer.class)
    public boolean add(${className} ${classNameLower});

	/** insert List<${className}> **/
	@Insert("<script>insert into " + table + " (" + insCols + ") values "
            + "<foreach item='it' collection='list' separator=','>(" + insMulVals + ")</foreach></script>")
	public boolean adds(@Param("list")List<${className}> list);

	/** delete by where key=val **/
	@DeleteProvider(type=SqlProvider.class, method="delAll")
    public boolean delAll();

	/** delete by where key=val **/
	@DeleteProvider(type=SqlProvider.class, method="delByKV")
    public boolean delByKV(@Param(pkey)Enum${className} key, @Param(pval)Object val);

	/** delete by where key1=val1 and key2=val2 ... **/
	@DeleteProvider(type=SqlProvider.class, method="delByMap")
    public boolean delByMap(Map<String, Object> params);

	@DeleteProvider(type=SqlProvider.class, method="delByModel")
    public boolean delByModel(${className} ${classNameLower});


	static class SqlProvider {
		private SQL baseDel(){
			return new SQL().DELETE_FROM(table);
		}
		public SQL baseUpd(){
			return new SQL().UPDATE(table);
		}
		private SQL baseGet(){
			return new SQL().SELECT(selCols).FROM(table);
		}
		private SQL baseCount(){
			return new SQL().SELECT("count(1)").FROM(table);
		}

		<#list table.columns as column>
		<#if column.pk>
		public String updByPk(${className} model){
			String set = DaoHelper.updData(model);
			return baseUpd().SET(set).WHERE("<@pkwhere/>").toString();
		}
		public String getByPk(<@pkparam/>){
			return baseGet().WHERE("<@pkwhere/>").toString();
		}
		public String delByPk(<@pkparam/>){
			return baseDel().WHERE("<@pkwhere/>").toString();
		}
		</#if>
		<#if column.unique && !column.pk>
		public String updBy${column.columnName}(${className} model){
			String set = DaoHelper.updData(model);
			return baseUpd().SET(set).WHERE("<@uniquewhere/>").toString();
		}
		public String getBy${column.columnName}(${column.simpleJavaType} ${column.columnNameLower}){
			return baseGet().WHERE("<@uniquewhere/>").toString();
		}
		public String delBy${column.columnName}(${column.simpleJavaType} ${column.columnNameLower}){
			return baseDel().WHERE("<@uniquewhere/>").toString();
		}
		</#if>
		</#list>

		public String getAll(){
			return baseGet().toString();
		}
		public String getByKV(Map<String, Object> param){
			return baseGet().WHERE(DaoHelper.kv(param)).toString();
		}
		public String getByMap(Map<String, Object> params){
			return baseGet().WHERE(DaoHelper.where(params)).toString();
		}
		public String getByModel(${className} model){
			return baseGet().WHERE(DaoHelper.where(model)).toString();
		}
		public String getByMapPage(Map<String, Object> params){
			@SuppressWarnings("unchecked")
			Map<String, Object> param = (Map<String, Object>)params.get("param");
			Pager lo = (Pager) params.get("lo");
			String sql = baseGet().WHERE(DaoHelper.where(param)).toString();

			if(!lo.getCol().equals("")) sql += " order by " + lo.getCol() + " " + lo.getOrder();
			if(lo.getCount() > 0) sql += " limit " + lo.getStart() + ", " + lo.getCount();
			return sql;
		}

		public String delAll(){
			return baseDel().toString();
		}
		public String delByKV(Map<String, Object> param){
			return baseDel().WHERE(DaoHelper.kv(param)).toString();
		}
		public String delByMap(Map<String, Object> params){
			return baseDel().WHERE(DaoHelper.where(params)).toString();
		}
		public String delByModel(${className} model){
			return baseDel().WHERE(DaoHelper.where(model)).toString();
		}


		public String count(){
			return baseCount().toString();
		}
		public String countByKV(Map<String, Object> param){
			return baseCount().WHERE(DaoHelper.kv(param)).toString();
		}
		public String countByMap(Map<String, Object> params){
			return baseCount().WHERE(DaoHelper.where(params)).toString();
		}
		public String countByModel(${className} model){
			return baseCount().WHERE(DaoHelper.where(model)).toString();
		}
	}
}
