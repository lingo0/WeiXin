package Util;

import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import po.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by ling on 2015/4/28.
 */
public class MessageUtil {

	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBLE = "subscrible";
	public static final String MESSAGE_UNSUBCRIBLE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";



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
	 * 将消息转化为Xml
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage){
		XStream xstream = new XStream();
//		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 装配XML
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initText(String toUserName,String fromUserName, String content) {
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		//转换成Xml
		return textMessageToXml(text);
	}

	/**
	 * 主菜单
	 * @return
	 */
	public static String menuText() {
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎您的关注！可以回复数字\n");
		sb.append("1、天气预报\n");
		sb.append("2、上课预报\n\n");
		sb.append("回复？调出此菜单");
		return sb.toString();
	}

	public static String firstMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("今天的是个好天气");
		return sb.toString();
	}

	public static String ClassMessage() {
		StringBuffer sb = new StringBuffer();
		String week = DataUtil.getWeekOfDate(new Date());
		sb.append("今天是" + week +"，");

		String Class = new String();
		if ("星期一".equals(week)){
			Class = "有英语课，下午2点在两江院405";
		} else if ("星期二".equals(week)) {
			Class = "有计算机网络课，下午2点在两江院401";
		} else if ("星期五".equals(week)){
			Class = "有算法课，上午九点四十五，下午两点在两江院403";
		} else {
			Class = "今天无课，也要加油";
		}

		sb.append(Class);
		return sb.toString();
	}

	private static Map<String,ArrayList<String>> m = new HashMap<String,ArrayList<String>>();

	//使用ArrayList实现一个Key对应一个ArrayList实现一对多
	public static void putAdd(String sr,String s){
		if(!m.containsKey(sr)){
			m.put(sr, new ArrayList<String>());
			m.get(sr).add(s);
		} else {
			m.get(sr).add(s);
		}
	}

	public static Document getDocumentFromUrl(String url) throws IOException, DocumentException{
		URL weatherUrl = new URL(url);
		URLConnection conn = weatherUrl.openConnection();

		SAXReader reader = new SAXReader();
		InputStream ins = conn.getInputStream();
		Document doc = reader.read(ins);
		ins.close();
		return doc;
	}

	public static String getWeather() throws IOException, DocumentException {
		String url = "http://api.map.baidu.com/telematics/v3/weather?location=%E8%8B%8F%E5%B7%9E&output=xml&ak=EVsXtjSUNzxV9iGlLTpaA68g";

		Document doc = getDocumentFromUrl(url);
		Element root = doc.getRootElement();
		Element rootResults = root.element("results");
		Element rootWeatherData = rootResults.element("weather_data");

		List<Element> listResults = rootResults.elements();
		List<Element> listWeatherData =  rootWeatherData.elements();

		Map<String,String> mapResults = new HashMap<String, String>();//currentCity,pm25
//		Map<String,String> mapWeatherData = new HashMap<String, String>();//date,weather,wind,temperature


		for (Element e:listResults){
			mapResults.put(e.getName(), e.getText());
		}

//		for (Element e:listWeatherData){
//			mapWeatherData.put(e.getName(), e.getText());
//		}

		for (Element e:listWeatherData){
			putAdd(e.getName(), e.getText());
		}

//		ins.close();

		String pm25 = mapResults.get("pm25");
		String currentCity = mapResults.get("currentCity");

//		String date = mapWeatherData.get("date");
		ArrayList<String> dateArray = m.get("date");
		String date0 = dateArray.get(0);
		String date1 = dateArray.get(1);
		String date2 = dateArray.get(2);

//      String weather = mapWeatherData.get("weather");
		ArrayList<String> weatherArray = m.get("weather");
		String weather0 = weatherArray.get(0);
		String weather1 = weatherArray.get(1);
		String weather2 = weatherArray.get(2);


//		String wind = mapWeatherData.get("wind");
		ArrayList<String> windArray = m.get("wind");
		String wind0 = windArray.get(0);
		String wind1 = windArray.get(1);
		String wind2 = windArray.get(2);

//		String temperature = mapWeatherData.get("temperature");
		ArrayList<String> temperatureArray = m.get("temperature");
		String temperature0 = temperatureArray.get(0);
		String temperature1 = temperatureArray.get(1);
		String temperature2 = temperatureArray.get(2);

		//组装成string
		StringBuffer weatherStringBuffer = new StringBuffer();
		weatherStringBuffer.append(currentCity);
		weatherStringBuffer.append("实时天气预报：");
		weatherStringBuffer.append(date0 + ",");
		weatherStringBuffer.append(weather0+ ",");
		weatherStringBuffer.append(wind0+ ",");
		weatherStringBuffer.append("pm2.5:" + pm25 +"\n");

		weatherStringBuffer.append("明后天气预报：");
		weatherStringBuffer.append(date1 + ",");
		weatherStringBuffer.append(weather1+ ",");
		weatherStringBuffer.append(wind1+ ",");
		weatherStringBuffer.append("气温:" + temperature1 +";");
		weatherStringBuffer.append(date2 + ",");
		weatherStringBuffer.append(weather2 + ",");
		weatherStringBuffer.append(wind2 + ",");
		weatherStringBuffer.append("气温:" + temperature2 +"\n");

		m.clear();
		return weatherStringBuffer.toString();
	}

//	public static void main(String arg[]) throws IOException, DocumentException {
//		MessageUtil ms = new MessageUtil();
//		String weather = MessageUtil.getWeather();
//	}

}
