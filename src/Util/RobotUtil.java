package Util;

import message.Article;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import po.robot.NewsArticl;
import po.robot.NewsRobotMessage;

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
	 * 将json对象转变成newsRobot对象
	 * @param jsonObject
	 * @return
	 */
	public static NewsRobotMessage newsJsonToString(JSONObject jsonObject){
		NewsRobotMessage newsRobotMessage = new NewsRobotMessage();
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
	public static List<Article> nesRobotToArticleList(NewsRobotMessage newsRobotMessage) {
		List<NewsArticl> newsArticls= newsRobotMessage.getList();
		List<Article> articles = new ArrayList<Article>();
		int size = newsArticls.size();
		if (size > 10) {
			size = 9;
		}
		for (int i = 0; i < size; i++) {
			Article article = newsArtileToWeixinArtil(newsArticls.get(i));
			articles.add(article);
		}
		return articles;
	}


}
