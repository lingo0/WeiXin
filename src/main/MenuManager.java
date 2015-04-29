package main;

import Util.WeiXinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import po.*;

/**
 * 菜单管理类
 *
 * Created by ling on 2015/4/29.
 */
public class MenuManager {
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	public static void main(String[] args) {
		//第三方用户唯一凭证
		String appId = "wx635dff39af49ae3e";
		//凭证密钥
		String appSecret = "e24878fb5e43debdb1a9520caba11f21";

		//调用接口获取access——token
		AccessToken accessToken = WeiXinUtil.getAccessToken(appId,appSecret);

		if (accessToken != null) {
			//调用接口创建菜单
			int result = WeiXinUtil.createMenu(getMenu(), accessToken.getToken());

			//判断菜单创建结果
			if ( 0 == result) {
				log.info("菜单创建成功");
			} else {
				log.info("菜单创建失败，错误码：" + result);
			}
		}

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
