package Util;

import com.thoughtworks.xstream.XStream;
import message.Article;
import message.NewsMessage;
import message.TextMessage;

/**
 * Created by ling on 2015/4/30.
 */
public class ToXml {
	/**
	 * 将消息转化为Xml
	 * @param textMessage 要转换的text
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 将图文消息转换成xml
	 * @param message 要转换的news
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage message) {
		XStream xStream = new XStream();
		xStream.alias("xml", message.getClass());
		xStream.alias("item", new Article().getClass());
		return xStream.toXML(message);
	}
}
