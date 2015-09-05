package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import po.Button;
import po.CommonButton;
import po.ComplexButton;
import po.Menu;

import java.io.UnsupportedEncodingException;

/**
 * 菜单管理类
 *
 * Created by ling on 2015/4/29.
 */
public class MenuManager {
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	public static void main(String[] args) throws UnsupportedEncodingException {
		//单元测试

	}

	/**
	 * 组装菜单数据
	 *
	 * @return
	 */
	private static Menu getMenu() {
		CommonButton btn11 = new CommonButton();
		btn11.setName("天气预报");
		btn11.setType("click");
		btn11.setKey("11");

		CommonButton btn12 = new CommonButton();
		btn12.setName("上课预报");
		btn12.setType("click");
		btn12.setKey("12");

		CommonButton btn21 = new CommonButton();
		btn21.setName("歌曲点播");
		btn21.setType("click");
		btn21.setKey("21");

		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("生活助手");
		mainBtn1.setSub_button(new CommonButton[] {btn11,btn12});

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("娱乐");
		mainBtn2.setSub_button(new CommonButton[] {btn21});

		Menu menu = new Menu();
		menu.setButton(new Button[] {mainBtn1, mainBtn2});

		return menu;
	}
}
