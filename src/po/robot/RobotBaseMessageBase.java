package po.robot;

/**
 * 机器人回复消息基类
 *
 * Created by ling on 2015/4/30.
 */
public class RobotBaseMessageBase {
	//返回码
	private int code;
	//返回内容
	private String text;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
