package po;

/**
 * 微信通讯接口凭证
 * Created by ling on 2015/4/29.
 */
public class AccessToken {
	//获取到的凭证
	private String token;
	//凭证有效时间，单位秒
	private int expireIn;


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getExpireIn() {
		return expireIn;
	}

	public void setExpireIn(int expireIn) {
		this.expireIn = expireIn;
	}
}
