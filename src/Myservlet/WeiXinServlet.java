package Myservlet;

import Service.ResponseMessage;
import Util.CheckUtil;
import Util.MessageUtil;
import org.dom4j.DocumentException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 用于和微信后台交互
 *
 * Created by ling on 2015/4/28.
 */
public class WeiXinServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");

		PrintWriter out = resp.getWriter();
		if (CheckUtil.checkSignature(signature,timestamp,nonce)) {
			out.print(echostr);
		}

	}

	/**
	 * 文本消息
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		PrintWriter out = resp.getWriter();
		try {
			//接收消息
			Map<String,String> map = MessageUtil.xmlToMap(req);
			String fromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");

			String message = null;

			if (MessageUtil.MESSAGE_TEXT.equals(msgType)){ //如果是文本则返回消息

				if ("1".equals(content)) {
					//课表查询
					message = MessageUtil.initText(toUserName, fromUserName, ResponseMessage.ClassMessage());


				}else if ("2".equals(content)) {
					//四六级查询
					String url
							= "<a href=\"http://apix.sinaapp.com/cet/?appkey=trialuser\">请点击查询您的四六级成绩</a>";
					message = MessageUtil.initText(toUserName,fromUserName,url);

				}else if ("3".equals(content)) {
					//登陆学校服务系统
					String url
							= "<a href=\"http://202.119.4.150/nstudent/index.aspx\">请点击登陆</a>";
					message = MessageUtil.initText(toUserName,fromUserName,url);

				}else if ("4".equals(content)) {
					//天气查询
					StringBuffer title = new StringBuffer();
					title.append("\ue04a天气预报使用指南\n\n");
					title.append("回复：天气+城市名称\n例如：苏州天气\n或者：天气苏州\n\n");
					title.append("回复“?”显示主菜单\ue301");
					message = MessageUtil.initText(toUserName,fromUserName,title.toString());

				}else if ("5".equals(content)) {
					//快递查询
					StringBuffer title = new StringBuffer();
					title.append("\ue42f快递查询使用指南\n\n");
					title.append("回复：快递名称+快递单号\n例如：顺丰快递 XXXX\n\n");
					title.append("回复“?”显示主菜单\ue301");
					message = MessageUtil.initText(toUserName,fromUserName,title.toString());

				}else if ("6".equals(content)) {
					//列车航班查询
					StringBuffer title = new StringBuffer();
					title.append("列车航班查询使用指南\n\n");
					title.append("\ue435查询火车车次信息\n");
					title.append("格式：时间+出发地+目的地+火车\n");
					title.append("例如：明天杭州到北京的火车\n\n");

					title.append("\ue01d查询飞机航班信息\n");
					title.append("格式：时间+出发地+目的地+飞机\n");
					title.append("或者：明天杭州到北京的航班\n\n");
					title.append("回复“?”显示主菜单\ue301");
					message = MessageUtil.initText(toUserName,fromUserName,title.toString());

				}else if ("7".equals(content)) {
					//看新闻
					StringBuffer title = new StringBuffer();
					title.append("\ue148热点新闻查看使用指南\n\n");
					title.append("回复：新闻\n例如：我想看娱乐新闻\n或者：看体育新闻\n\n");
					title.append("回复“?”显示主菜单\ue301");
					message = MessageUtil.initText(toUserName,fromUserName,title.toString());

				}else if ("8".equals(content)) {
					//简易计算器
					StringBuffer title = new StringBuffer();
					title.append("\ue00a简易计算器使用指南\n\n");
					title.append("回复：需要计算的式子\n例如：一加二\n或者：3乘五\n\n");
					title.append("回复“?”显示主菜单\ue301");
					message = MessageUtil.initText(toUserName,fromUserName,title.toString());

				}else if ("9".equals(content)) {
					StringBuffer instructions = new StringBuffer();
					instructions.append("\ue056小L聊天使用功能\n\n");
					instructions.append("你好，我是小L，闲暇无聊，来找小L聊天吧，小L很能聊，有问必答！\n例如：\n");
					instructions.append("\ue231讲个笑话\n");
					instructions.append("\ue231周杰伦百科\n");
					instructions.append("\ue231今年是什么属相\n\n");
					instructions.append("随便说点什么和小L开始聊天吧\n");
					instructions.append("回复“?”显示主菜单");
					message = MessageUtil.initText(toUserName, fromUserName,instructions.toString());
				}else if ("?".equals(content) || "？".equals(content) || "你会干嘛".equals(content)) {
					message = MessageUtil.initText(toUserName, fromUserName, ResponseMessage.menuText());
				}
				else {
					message = ResponseMessage.robotMessage(toUserName,fromUserName,content);
				}

			} else if (MessageUtil.MESSAGE_EVENT.equals(msgType)) { //当用户第一次关注时
				String eventType = map.get("Event");
				if (MessageUtil.MESSAGE_SUBSCRIBLE.equals(eventType)) {
					message = MessageUtil.initText(toUserName, fromUserName,ResponseMessage.menuText());
				}
			} else {
				message = MessageUtil.initText(toUserName, fromUserName, ResponseMessage.menuText());
			}
			System.out.println(message);
			out.print(message);//返回给微信后台
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}

	}
}
