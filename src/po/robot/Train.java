package po.robot;

/**
 * 火车列次信息
 *
 * Created by ling on 2015/5/2.
 */
public class Train {
	private String trainnum;
	private String start;
	private String terminal;
	private String starttime;
	private String endtime;
	private String icon;
	private String detailurl;

	public String getTrainnum() {
		return trainnum;
	}

	public void setTrainnum(String trainnum) {
		this.trainnum = trainnum;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
