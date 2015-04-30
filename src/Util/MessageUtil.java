package Util;

import message.Article;
import message.NewsMessage;
import message.TextMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 *
 * Created by ling on 2015/4/28.
 */
public class MessageUtil {

	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBLE = "subscribe";
	public static final String MESSAGE_UNSUBCRIBLE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	public static final String MESSAGE_NEWS = "news";


	/**
	 * xml转为map集合
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {

		Map<String,String> map = new HashMap<String, String>();

		SAXReader reader = new SAXReader();
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);

		Element root = doc.getRootElement();

		List<Element> list = root.elements();

		for (Element e:list){
			map.put(e.getName(), e.getText());
		}
		ins.close();

		return map;
	}


	/**
	 * text装配XML
	 * @param toUserName 从哪里发出
	 * @param fromUserName  发送给谁
	 * @param content 发送的文本内容
	 * @return 转换好的xml
	 */
	public static String initText(String toUserName,String fromUserName, String content) {
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		//转换成Xml
		return ToXml.textMessageToXml(text);
	}

	/**
	 *
	 * @param toUserName 从哪里发出
	 * @param fromUserName  发送给谁
	 * @return 转换好的xml
	 * @return
	 */
	public static String initArticle(String toUserName,String fromUserName, List<Article> articleList) {

		NewsMessage newsMessage = new NewsMessage();

		newsMessage.setFromUserName(toUserName);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MESSAGE_NEWS);

		newsMessage.setArticleCount(articleList.size());
		newsMessage.setArticles(articleList);

		// 将图文消息对象转换成xml字符串
		return ToXml.newsMessageToXml(newsMessage);
	}


}
