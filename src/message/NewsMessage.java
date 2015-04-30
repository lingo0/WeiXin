package message;

import java.util.List;

/**
 * 图文消息
 *
 * Created by ling on 2015/4/30.
 */
public class NewsMessage extends BaseMessage{
	private int ArticleCount;

	private List<Article> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}


	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		this.Articles = articles;
	}
}
