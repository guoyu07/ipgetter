package cn.ihuhai.ipgetter.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.ihuhai.ipgetter.AbstractHtmlIpGetter;
import cn.ihuhai.ipgetter.util.http.HttpClientManager;

public class Ip138IpGetter extends AbstractHtmlIpGetter{

	private Logger logger = LogManager.getLogger(Ip138IpGetter.class);
	private DefaultHttpClient httpclient = HttpClientManager.getClient();
	
	private static final String HttpAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5";
	private static final String UserAgent = "User-Agent";
	private static final String IP_PAGE = "http://iframe.ip138.com/ic.asp";
	private static final String REFERER = "http://www.ip138.com/";
	private static final String HOST = "iframe.ip138.com";
	
	@Override
	public void clean() {
		if (null != httpclient) {
			if (null != httpclient.getConnectionManager()) {
				httpclient.getConnectionManager().shutdown();
			}
		}
	}

	@Override
	public String getHtml() {
		StringBuilder builder = new StringBuilder();
		BufferedReader br = null;
		try {
			HttpGet get = new HttpGet(IP_PAGE);
			setHeader(get);
			HttpResponse response = httpclient.execute(get);
			
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
		}catch (Exception e) {
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
	
	private void setHeader(HttpRequestBase req) {
        req.setHeader(UserAgent, HttpAgent);
        req.setHeader("Host", HOST);
        req.setHeader("Cache-Control", "max-age=0");
        req.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        req.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        req.setHeader("Accept-Language", "en-US,en;q=0.8");
        req.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
        req.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        req.setHeader("Referer", REFERER);
    }

	@Override
	public String extractIp(String html) {
		String ip = null;
		logger.info(html);
		Matcher matcher = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})").matcher(html);
		if(matcher.find()){
			ip = matcher.group(1);
			logger.info("find ip: " + ip);
		}
		return ip;
	}

}
