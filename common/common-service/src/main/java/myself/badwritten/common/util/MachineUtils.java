package myself.badwritten.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MachineUtils {

	private static Log logger = LogFactory.getLog(MachineUtils.class);

	/**
	 * 根据指定ip段,获取本机IPV4地址地址
	 * 
	 * @param prefix
	 * @return
	 */
	public static InetAddress getIpAddress(String prefix) {
		List<InetAddress> list = getIpAddressList();
		InetAddress ipAddress = null;
		for (InetAddress ia : list) {
			String ip = ia.getHostAddress();
			if (ip.startsWith(prefix)) {
				ipAddress = ia;
				break;
			}
		}
		return ipAddress;
	}

	/**
	 * 获取所有IPV4地址
	 * 
	 * @return
	 */
	public static List<InetAddress> getIpAddressList() {
		List<InetAddress> list = new ArrayList<InetAddress>();
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface ni = en.nextElement();
				Enumeration<InetAddress> ias = ni.getInetAddresses();
				while (ias.hasMoreElements()) {
					InetAddress iaddr = ias.nextElement();

					if (iaddr instanceof Inet4Address) {
						if (iaddr.isSiteLocalAddress()) {
							list.add(iaddr);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取IP失败 -" + e.getMessage(), e);
		}
		return list;
	}

}
