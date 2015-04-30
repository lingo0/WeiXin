package Util;


import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import po.AccessToken;
import po.Menu;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchProviderException;

/**
 * 公众平台通用接口类
 *
 * Created by ling on 2015/4/29.
 */
public class WeiXinUtil {
	private static Logger log = LoggerFactory.getLogger(WeiXinUtil.class);

	/**
	 * 发起http请求获取Jason格式数据
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式
	 * @return 返回Jason格式数据
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod) {
		JSONObject jsonObject = null;

		try {
			URL robotUrl = new URL(requestUrl);

			//建立连接
			URLConnection conn = robotUrl.openConnection();

			//获取请求得到的数据
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


			//将数据转成Json格式
			StringBuffer buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null){
				buffer.append(str);
			}

			//释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			jsonObject = JSONObject.fromObject(buffer.toString());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}




	/**
	 * 发起https请求并获取数据
	 *
	 * @param requestUrl  请求地址
	 * @param requestMethod 请求方式（GET,POST）
	 * @param outputStr 提交数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {

		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();

		//以下是解决https请求问题
		//创建sslContext对象，并使用自己指定的信任管理器初始化
		TrustManager[] tm = {
			new MyX509TrustManager()
		};

		try {
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			//到此以上是解决https请求问题

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			//设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)){
				httpUrlConn.connect();
			}

			//当有数据需要请求提交时,使用post提交数据
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				//防止乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			//将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null){
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			//释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			ce.printStackTrace();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			log.error("Weixin server connection timed out.");
		} catch (KeyManagementException e) {
			e.printStackTrace();
			log.error("https request error:{}", e);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			log.error("https request error:{}", e);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("https request error:{}", e);
		}
		return jsonObject;

	}

	private final static String access_token_url
			= "https://api.weixin.qq.com/cgi-bin/token?" +
			"grant_type=client_credential&appid=APPID&secret=APPSECRET";


	/**
	 * 获取AccessToken
	 * @param appid 个人ID
	 * @param appsecret 密钥
	 * @return 返回AccessToken
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;

		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
		//请求成功
		if (null != jsonObject){
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpireIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// 获取token失败
				log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}

	//菜单创建（post）
	public static String menu_create_url
			= "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	/**
	 * 创建菜单
	 *
	 * @param menu 菜单实例
	 * @param accessToken 有效的access_token
	 * @return 0表示成功
	 */
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;

		//拼接创建菜单的URL
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		//将菜单对象转换成Json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();

		//调用接口创建菜单
		JSONObject jsonObject = httpsRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error("创建菜单失败 errcode:{} errmsg:{}",jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}
		return result;
	}


}
