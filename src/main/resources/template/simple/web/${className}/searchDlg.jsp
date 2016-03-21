${gg.setOverride(true)}
<#include "/macro.include"/>
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- 高级查询  Order_mainSearchDlg-->
<div id="${classNameLower}SearchDlg">
	<form id="${classNameLower}SearchFm" method="post">
		<table>
			<tr>
				<th>条件</th>
				<th>字段名</th>
				<th>条件</th>
				<th>值</th>
			</tr>
			<tr>
				<td>
					<select name="searchAnds">
						<option value="&&"></option>
						<option value="||">或者</option>
						<option value="&&">并且</option>
					</select>
				</td>
				<td>
					<select name="searchColumnNames">
					 
					<#list table.columns as column>
						<option value="${column.columnNameLower}">${column.columnNameLower}</option>
					</#list>
			  
					</select>
				</td>
				<td>
					<select name="searchConditions">
						<option value="="></option>
						<option value="=">等于</option>
						<option value="<>">大于小于</option>
						<option value="<">小于</option>
						<option value=">">大于</option>
						<option value="Like">模糊</option>
					</select>
				</td>
					<td><input name="searchVals" size="18"> <a href="javascript:void(0)">日期框</a>
					 <a href="javascript:void(0)" onclick="${classNameLower}SearchRemove(this)">删除</a>
				</td>
			</tr>
		</table>
	</form>
</div>
