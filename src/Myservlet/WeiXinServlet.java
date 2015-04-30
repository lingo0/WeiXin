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
 * Created by ling on 2015/4/28.
 */
public class WeiXinServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		super.doGet(req, resp);
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
//		super.doPost(req, resp);

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

				if ("1".equals(content) || "天气".equals(content)) {
					//请求天气预报
					message = MessageUtil.initText(toUserName, fromUserName, ResponseMessage.getWeather());

				}else if ("?".equals(content) || "？".equals(content)) {
					message = MessageUtil.initText(toUserName, fromUserName, ResponseMessage.menuText());
				} else if ("2".equals(content)) {
					StringBuffer instructions = new StringBuffer();
					instructions.append("你好，我是小L，欢迎来和我聊天：");
					instructions.append("1、回复“看新闻”，可以获取实时热门新闻。\n");
					instructions.append("如回复“娱乐新闻”。\n\n");
					instructions.append("随便说点什么和小L开始聊天吧");
					message = MessageUtil.initText(toUserName, fromUserName,instructions.toString());
//					message = MessageUtil.initArticle(toUserName,fromUserName,ResponseMessage.articlesMessage());
				}
				else {
					message = ResponseMessage.robotMessage(toUserName,fromUserName,content);
//					message = MessageUtil.initText(toUserName, fromUserName, ResponseMessage.robotMessage(toUserName,fromUserName,content));
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
