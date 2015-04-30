package po.robot;

/**
 * 新闻
 * Created by ling on 2015/4/30.
 */
public class NewsArticl {
	//标题
	private String article;
	//来源
	private String source;
	//详细地址
	private String detailurl;
	//图标地址
	private String icon;

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}
}
