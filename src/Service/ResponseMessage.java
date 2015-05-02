package Service;

import Util.DataUtil;
import Util.MessageUtil;
import Util.RobotUtil;
import Util.WeiXinUtil;
import message.Article;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import po.robot.NewsArticl;
import po.robot.RobotMessage;
import po.robot.Train;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

/**
 * 自动回复消息
 *
 * Created by ling on 2015/4/30.
 */
public class ResponseMessage {

	private static Logger log = LoggerFactory.getLogger(ResponseMessage.class);

	/**
	 * 主菜单
	 * @return
	 */
	public static String menuText() {
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎您的关注！可以回复数字查看各类使用说明\n");
		sb.append("1、课表查询\n");
		sb.append("2、四六级查询\n");
		sb.append("3、登陆学校服务系统\n");

		sb.append("4、天气查询\n");
		sb.append("5、快递查询\n");
		sb.append("6、列车航班查询\n");

		sb.append("7、看新闻\n");
		sb.append("8、简易计算器\n");
		sb.append("9、聊天机器人小L使用说明\n");
		sb.append("回复？调出此使用说明");
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

	/**
	 * Url http请求 返回Document
	 *
	 * @param url 请求的url
	 * @return Document
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Document getDocumentFromUrl(String url) throws IOException, DocumentException {
		URL weatherUrl = new URL(url);
		URLConnection conn = weatherUrl.openConnection();

		InputStream ins = conn.getInputStream();

		SAXReader reader = new SAXReader();
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


	/**
	 * 图文消息内容
	 * @return
	 */
	public static List<Article> articlesMessage(){
		Article article = new Article();
		article.setTitle("ACM #1014 : Trie树");
		article.setDescription("Trie树，是一种树形结构，是一种哈希树的变种。典型应用是用于统计，排序和保存大量的字符串（但不仅限于字符串），所以经常被搜索引擎系统用于文本词频统计。它的优点是：利用字符串的公共前缀来减少查询时间，最大限度地减少无谓的字符串比较，查询效率比哈希树高。");
		article.setPicUrl("http://images.laoqianzhuang.com/2015/0331/20150331013239711.jpg");
		article.setUrl("http://blog.csdn.net/lsh199196/article/details/43410105");

		List<Article> articleList = new ArrayList<Article>();
		articleList.add(article);

		return articleList;
	}

	private static final String  roboturl = "http://www.tuling123.com/openapi/api?key=KEY&info=CONTENT&userid=USERID";

	/**
	 *
	 * @param content
	 * @param toUserName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String robotMessage(String toUserName, String fromUserName, String content) throws UnsupportedEncodingException {
		String key = "1878d1501ea2bc334748a1dea149c755";

		//转换编码
//		String t2 = java.net.URLEncoder.encode(content);
		String contUtf8 = URLEncoder.encode(content, "utf-8");
		String url = roboturl.replace("KEY", key).replace("CONTENT", contUtf8).replace("USERID",fromUserName);

		String text = null;
//		StringBuffer message = new StringBuffer();

		//获得Json格式数据
		JSONObject jsonObject = WeiXinUtil.httpRequest(url, "GET");

		//请求成功
		if (null != jsonObject){
			try {
				int code = jsonObject.getInt("code");
				text = jsonObject.getString("text");
//				message.append(text);

				switch (code){
					case  RobotUtil.robotMessage_NEWS: //回复的是新闻
						//将json格式转换成newsRobot对象
						RobotMessage<NewsArticl> newsRobotMessage = RobotUtil.newsJsonToString(jsonObject);

						//将newsRobot对象转变长微信news对象
						List<Article> articles = RobotUtil.nesRobotToArticleList(newsRobotMessage);
						text = MessageUtil.initArticle(toUserName,fromUserName,articles);
						break;
					case RobotUtil.robotMessage_TRAIN: //回复的是火车列次
						//将json格式的数据转成trainRobot对象
						RobotMessage<Train> trainRobotMessage = RobotUtil.trainJsonToString(jsonObject);

						//将trainRobot对象转换成微信news对象
						List<Article> trains = RobotUtil.trainRobotToArticleList(trainRobotMessage);
						text = MessageUtil.initArticle(toUserName,fromUserName,trains);
						break;
					case RobotUtil.robotMessage_PLANT://回复的是飞机航班信息
						String planturl
								= "<a href=\"http://touch.qunar.com/h5/flight/flightlist?bd_source=chongdong&startCity=&destCity=&startDate=&backDate=&flightType=oneWay&priceSortType=1\">请点击查您所需的航班信息</a>";
						text = MessageUtil.initText(toUserName,fromUserName,planturl);

//						//将json格式的数据转成trainRobot对象
//						RobotMessage<Plant> plantRobotMessage = RobotUtil.plantJsonToString(jsonObject);
//						//将trainRobot对象转换成微信news对象
//						List<Article> plants = RobotUtil.plantRobotToArticleList(plantRobotMessage);
//						text = MessageUtil.initArticle(toUserName,fromUserName,plants);
						break;


					default:
						text = MessageUtil.initText(toUserName, fromUserName, text);
						break;
				}



			} catch (JSONException e) {
				// 获取token失败
				log.error("获取失败 errcode:{}", jsonObject.getInt("code"));
			}
		}

		return text;
	}

}
