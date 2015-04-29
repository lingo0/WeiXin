package po;

/**
 * 按钮的基类
 * 首先是菜单项的基类，
 * 所有一级菜单、二级菜单都共有一个相同的属性，那就是name。
 * 菜单项基类的封装代码如下：
 * Created by ling on 2015/4/29.
 */
public class Button {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
