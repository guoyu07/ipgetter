package cn.ihuhai.ipgetter;

public abstract class AbstractHtmlIpGetter implements IpGetter {

	public String getIp() {
		String ip = extractIp(getHtml());
		return ip;
	}

	public abstract void clean() ;	
	public abstract String getHtml();
	public abstract String extractIp(String html);
}
