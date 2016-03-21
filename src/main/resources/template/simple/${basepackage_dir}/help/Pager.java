${gg.setOverride(true)}
<#include "/macro.include"/>
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.dao.help;

/**
 * @description: The automatically generated
 * @Package  ${basepackage}.dao.help
 * @Date	 ${.now?string("yyyy-MM-dd HH:mm:ss")}
 * @version  V1.0
 */
public class Pager {
	public enum Order {
		DESC, ASC;
	}
	private String order = "";
	private String col = "";
	private Integer start = 0;
	private Integer count = 0;
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public static Pager build(int count) {
		Pager lo = new Pager();
		lo.setCount(count);
		return lo;
	}
	public static Pager build(int start, int count) {
		Pager lo = new Pager();
		lo.setStart(start);
		lo.setCount(count);
		return lo;
	}
	public static Pager build(int count, Order order, String col) {
		Pager lo = new Pager();
		lo.setOrder(order.name());
		lo.setCount(count);
		lo.setCol(col);
		return lo;
	}
	public static Pager build(int start, int count, Order order, String col) {
		Pager lo = new Pager();
		lo.setOrder(order.name());
		lo.setCol(col);
		lo.setStart(start);
		lo.setCount(count);
		return lo;
	}
	public static Pager build(Order order, String col) {
		Pager lo = new Pager();
		lo.setOrder(order.name());
		lo.setCol(col);
		return lo;
	}

}
