package cn.ihuhai.ipgetter.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.ihuhai.ipgetter.AbstractHtmlIpGetter;
import cn.ihuhai.ipgetter.util.http.HttpClientManager;

@Deprecated
public class TpLinkIpGetter extends AbstractHtmlIpGetter{

	private Logger logger = LogManager.getLogger(TpLinkIpGetter.class);
	private DefaultHttpClient httpclient = HttpClientManager.getClient();
	private String userName = "admin";
	private String password = "admin";

	@Override
	public String extractIp(String html){
		String ip = null;
		if(null != html){
			System.out.println(html);
			Document doc = Jsoup.parse(html);
			Elements tables = doc.select("table");
			for(Element table : tables){
				System.out.println(table.text());
			}
		}
		return ip;
	}

	@Override
	public String getHtml() {
		StringBuilder builder = new StringBuilder();
		BufferedReader br = null;
		HttpHost targetHost = new HttpHost("192.168.1.1", 80, "http");
		try {
			httpclient.getCredentialsProvider().setCredentials(
					new AuthScope(targetHost.getHostName(),
							targetHost.getPort()),
					new UsernamePasswordCredentials(userName, password));

			// Create AuthCache instance
			AuthCache authCache = new BasicAuthCache();
			// Generate BASIC scheme object and add it to the local
			// auth cache
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(targetHost, basicAuth);

			// Add AuthCache to the execution context
			BasicHttpContext localcontext = new BasicHttpContext();
			localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);

			HttpGet httpget = new HttpGet("/userRpm/StatusRpm.htm");

			logger.info("executing request: " + httpget.getRequestLine());
			logger.info("to target: " + targetHost);

			HttpResponse response = httpclient.execute(targetHost, httpget,
					localcontext);
			HttpEntity entity = response.getEntity();

			if(200 == response.getStatusLine().getStatusCode() && entity != null) {
				br = new BufferedReader(new InputStreamReader(entity.getContent(), "gb2312"));
				String line = null;
				while(null != (line = br.readLine())){
					builder.append(line);
				}
				br.close();
			}else{
				logger.info(response.getStatusLine());
			}
			EntityUtils.consume(entity);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally{
			if(null != br){
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		return builder.toString();
	}

	@Override
	public void clean() {
		if (null != httpclient) {
			if (null != httpclient.getConnectionManager()) {
				httpclient.getConnectionManager().shutdown();
			}
		}

	}

}
