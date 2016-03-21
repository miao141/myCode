${gg.setOverride(true)}
<#include "/macro.include"/>
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.dao.help;

import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @description: The automatically generated
 * @Package  ${basepackage}.dao.help
 * @Date	 ${.now?string("yyyy-MM-dd HH:mm:ss")}
 * @version  V1.0
 */
public class DaoHelper {

	private static final String pkey = "key";
	private static final String pval = "val";

	public static String where(Map<String, String> params) {
		StringBuffer data = new StringBuffer();
		params.forEach((key,val)->data.append(String.format("%1s=%2s and ", DaoHelper.toSnake(key), val)));
		return removeEnd(data.toString(), "and");
	}

	public static <T> String where(T model) {
		return build(model, "%1s=%2s and ", "and");
	}

	public static String kv(Map<String, Object> param) {
		return param.get(pkey) + String.format("=${'#{'}%s${'}'}", pval);
	}

	public static <T> String updData(T model) {
		return build(model, "%1s=%2s,", ",");
	}

	private static <T> String build(T model, String part, String remove) {
		JSONObject json = JSON.parseObject(JSON.toJSONString(model));
		StringBuffer data = new StringBuffer();
		json.forEach((key,val)->data.append(String.format(part, DaoHelper.toSnake(key), val)));

		return removeEnd(data.toString(), remove);
	}

	private static String removeEnd(String data, String remove){
		return data.substring(0, data.lastIndexOf(remove)).trim();
	}

	private static String toSnake(String str) {
		if (str == null) return null;

		StringBuilder sb = new StringBuilder();
		boolean prevUpper = false, curUpper = false, nextUpper = false;
		for (int i = 0; i < str.length(); i++) {
			char s = str.charAt(i);

			prevUpper = curUpper;
			curUpper = (i == 0) ? Character.isUpperCase(s) : nextUpper;
			nextUpper = (i < str.length() - 1 ? Character.isUpperCase(str.charAt(i + 1)) : true);

			if(String.valueOf(s).equals("_")) continue;
			if(i > 0 && curUpper && !(nextUpper && prevUpper)) sb.append("_");

			sb.append(Character.toLowerCase(s));
		}
		return sb.toString();
	}

}
