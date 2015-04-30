package po.robot;

import java.util.List;

/**
 * 新闻类
 *
 * Created by ling on 2015/4/30.
 */
public class NewsRobotMessage extends RobotBaseMessageBase {

	public List<NewsArticl> list;

	public void setList(List<NewsArticl> list){
		this.list = list;
	}

	public List<NewsArticl> getList() {
		return list;
	}
}
