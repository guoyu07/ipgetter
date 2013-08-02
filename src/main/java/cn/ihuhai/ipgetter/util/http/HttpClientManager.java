package cn.ihuhai.ipgetter.util.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientManager {
	

	public static DefaultHttpClient getClient(){
		DefaultHttpClient client = ClientCreator.createDefaultClient();
		return client; 
	}
	
	static class ClientCreator{
		private static DefaultHttpClient client = new DefaultHttpClient();;
		
		private static DefaultHttpClient createDefaultClient(){
			if(client == null){
				client = new DefaultHttpClient();
			}
			return client;
		} 
	}
}
