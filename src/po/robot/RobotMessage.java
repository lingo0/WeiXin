package po.robot;

import java.util.List;

/**
 * 消息模板类，用来输入不同的消息类型
 * 如新闻，火车列次飞机航班等
 *
 * Created by ling on 2015/5/2.
 */
public class RobotMessage<T>  extends RobotBaseMessageBase{

	public List<T> list;

	public void setList(List<T> list){
		this.list = list;
	}

	public List<T> getList() {
		return list;
	}
}
