package cn.ihuhai.test.ipgetter;

import org.junit.Test;

import cn.ihuhai.ipgetter.IpGetter;
import cn.ihuhai.ipgetter.impl.Ip138IpGetter;
import cn.ihuhai.ipgetter.impl.TpLinkIpGetter;

public class IpGetterTest {
	

	@Test
	public void testTpLinkIpGetter(){
		IpGetter getter = new TpLinkIpGetter();
		System.out.println(getter.getIp());
	}
	
	@Test
	public void testIp138IpGetter(){
		IpGetter getter = new Ip138IpGetter();
		System.out.println(getter.getIp());
	}
}
