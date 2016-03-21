<#include "/macro.include"/>
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- 弹出框 ${classNameLower}Dlg -->
  
  <div id="${classNameLower}Dlg">
  <form id="${classNameLower}Fm" method="post">
  	<table>
  <#list table.columns as column>
	<tr>
    <td><label> ${column.columnAlias}: </label></td>
	  <td>
		<input name="${column.columnNameLower}" class="easyui-validatebox"  />
	  </td>
	</tr>
  </#list>
  </table>
  </form>
  </div>
  
