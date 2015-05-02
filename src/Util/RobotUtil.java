package Util;

import message.Article;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import po.robot.NewsArticl;
import po.robot.Plant;
import po.robot.RobotMessage;
import po.robot.Train;

import java.util.ArrayList;
import java.util.List;


/**
 * 机器人消息工具类
 *
 * Created by ling on 2015/4/30.
 */
public class RobotUtil {
	public static final int robotMessage_Text = 100000;
	public static final int robotMessage_TRAIN = 305000;
	public static final int robotMessage_PLANT = 306000;
	public static final int robotMessage_WEBURL = 200000;
	public static final int robotMessage_NEWS = 302000;
	public static final int robotMessage_FOOD = 308000;

	/**
	 * 将新闻json对象转变成newsRobot对象
	 * @param jsonObject
	 * @return
	 */
	public static RobotMessage<NewsArticl> newsJsonToString(JSONObject jsonObject){
		RobotMessage<NewsArticl> newsRobotMessage = new RobotMessage<NewsArticl>();
		newsRobotMessage.setCode(jsonObject.getInt("code"));
		newsRobotMessage.setText(jsonObject.getString("text"));
		JSONArray jArray =  jsonObject.getJSONArray("list");
		int size = jArray.size();

		List<NewsArticl> newsArticlList = new ArrayList<NewsArticl>();
		for (int i = 0; i < size; i++) {
			JSONObject jo = jArray.getJSONObject(i);
			NewsArticl articl = new NewsArticl();
			articl.setArticle(jo.getString("article"));
			articl.setSource(jo.getString("source"));
			articl.setIcon(jo.getString("icon"));
			articl.setDetailurl(jo.getString("detailurl"));
			newsArticlList.add(articl);
		}
		newsRobotMessage.setList(newsArticlList);

		return newsRobotMessage;
	}

	/**
	 * 将newsarticle转变成为微信article
	 * @param newsArticl
	 * @return
	 */
	public static Article newsArtileToWeixinArtil(NewsArticl newsArticl) {
		Article article = new Article();
		article.setTitle(newsArticl.getArticle());
		article.setDescription("来自：" + newsArticl.getSource());
		article.setPicUrl(newsArticl.getIcon());
		article.setUrl(newsArticl.getDetailurl());
		return article;
	}

	/**
	 * 将newsRobot对象转变长微信news对象
	 *
	 */
	public static List<Article> nesRobotToArticleList(RobotMessage<NewsArticl> newsRobotMessage) {
		List<NewsArticl> newsArticls= newsRobotMessage.getList();
		List<Article> articles = new ArrayList<Article>();
		int size = newsArticls.size();
		if (size >= 10) {
			size = 9;
		}
		for (int i = 0; i < size; i++) {
			Article article = newsArtileToWeixinArtil(newsArticls.get(i));
			articles.add(article);
		}
		return articles;
	}


	/**
	 * 将火车列次json对象转变成Robot消息对象
	 * @param jsonObject
	 * @return
	 */
	public static RobotMessage<Train> trainJsonToString(JSONObject jsonObject) {
		RobotMessage<Train> trainRobotMessage = new RobotMessage<Train>();
		trainRobotMessage.setCode(jsonObject.getInt("code"));
		trainRobotMessage.setText(jsonObject.getString("text"));
		JSONArray jArray =  jsonObject.getJSONArray("list");
		int size = jArray.size();

		List<Train> trainList = new ArrayList<Train>();
		for (int i = 0; i < size; i++){
			JSONObject jo = jArray.getJSONObject(i);
			Train train = new Train();
			train.setTrainnum(jo.getString("trainnum"));
			train.setStart(jo.getString("start"));
			train.setTerminal(jo.getString("terminal"));
			train.setStarttime(jo.getString("starttime"));
			train.setEndtime(jo.getString("endtime"));
			train.setDetailurl(jo.getString("detailurl"));
			train.setIcon(jo.getString("icon"));
			trainList.add(train);
		}
		trainRobotMessage.setList(trainList);
		return trainRobotMessage;
	}

	/**
	 * 将trainArticle转变成为微信图文article
	 * @param train
	 * @return
	 */
	public static Article trainToWeixinArtil(Train train) {
		Article article = new Article();

		StringBuffer title = new StringBuffer();
		title.append(train.getStart() + "-" + train.getTerminal());
		title.append("\n");
		title.append(train.getStarttime() + " " + train.getEndtime());

		article.setTitle(title.toString());
		article.setDescription("");
		article.setPicUrl(train.getIcon());
		article.setUrl(train.getDetailurl());
		return article;
	}

	/**
	 * 将TrainMessage对象转成图文消息
	 * @param trainRobotMessage
	 * @return
	 */
	public static List<Article> trainRobotToArticleList(RobotMessage<Train> trainRobotMessage) {
		List<Train> trains = trainRobotMessage.getList();
		List<Article> articles = new ArrayList<Article>();

		int size = trains.size();
		if (size >= 10) {
			size = 9;
		}

		for (int i = 0; i < size; i++) {
			Article article = trainToWeixinArtil(trains.get(i));
			articles.add(article);
		}

		return articles;
	}

	/**
	 * 格式转换
	 * @param jsonObject
	 * @return
	 */
	public static RobotMessage<Plant> plantJsonToString(JSONObject jsonObject) {
		RobotMessage<Plant> plantRobotMessage = new RobotMessage<Plant>();
		plantRobotMessage.setCode(jsonObject.getInt("code"));
		plantRobotMessage.setText(jsonObject.getString("text"));
		JSONArray jArray =  jsonObject.getJSONArray("list");
		int size = jArray.size();

		List<Plant> PlantList = new ArrayList<Plant>();
		for (int i = 0; i < size; i++){
			JSONObject jo = jArray.getJSONObject(i);
			Plant plant = new Plant();
			plant.setFlight(jo.getString("flight"));
//			plant.setState(jo.getString("state"));
			plant.setStarttime(jo.getString("starttime"));
			plant.setEndtime(jo.getString("endtime"));
//			plant.setDetailurl(jo.getString("detailurl"));
			plant.setIcon(jo.getString("icon"));
		}
		plantRobotMessage.setList(PlantList);
		return plantRobotMessage;
	}

	/**
	 * 将plantArticle转变成为微信图文article
	 * @param plant
	 * @return
	 */
	public static Article plantToWeixinArtil(Plant plant) {
		Article article = new Article();

		StringBuffer title = new StringBuffer();
		title.append(plant.getFlight());
		title.append("\n");
		title.append("出发："+ plant.getStarttime() + "，到达：" + plant.getEndtime());

		article.setTitle(title.toString());
		article.setDescription("");
		article.setPicUrl(plant.getIcon());
		String url = "http://touch.qunar.com/h5/flight/flightlist?bd_source=chongdong&startCity=STARTCITY&destCity=DESTCITY&startDate=STARTDATA&backDate=&flightType=oneWay&priceSortType=1";

		article.setUrl(url);
		return article;
	}




	public static List<Article> plantRobotToArticleList(RobotMessage<Plant> plantRobotMessage) {
		return null;
	}
}
