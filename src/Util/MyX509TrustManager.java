package Util;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 证书信任管理器
 * 对于https请求，
 * 需要一个证书信任管理器，这个管理器类需要自己定义，
 * 可以实现X509TrustManager接口
 *
 * 这个证书管理器的作用就是让它信任我们指定的证书，上面的代码意味着信任所有证书，不管是否权威机构颁发。
 *
 * Created by ling on 2015/4/29.
 */
public class MyX509TrustManager implements X509TrustManager {
	@Override
	public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

	}

	@Override
	public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}
}
