package edu.buaa.rse.dotx.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleMacAddress {
	public static String getMacAddress() {
		String os = getOSName();
		String mac = null;
		if (os.startsWith("windows")) {
			mac = getWindowsMacAddress();
		} else {
			mac = getUnixMacAddress();
		}

		return mac;
	}

	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}

	public static String getUnixMacAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;

		try {
			process = Runtime.getRuntime().exec("ip link");
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String e = null;
			boolean index = true;

			while ((e = bufferedReader.readLine()) != null) {
				int index1 = e.toLowerCase().indexOf("link/ether");
				if (index1 >= 0) {
					mac = e.substring(index1 + "link/ether".length() + 1, index1 + "link/ether".length() + 19).trim();
					System.out.println(mac);
					break;
				}
			}
		} catch (IOException arg12) {
			arg12.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException arg11) {
				arg11.printStackTrace();
			}

			bufferedReader = null;
			process = null;
		}

		return mac;
	}

	public static String getWindowsMacAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;

		try {
			process = Runtime.getRuntime().exec("ipconfig /all");
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "gb2312"));
			String e = null;
			boolean index = true;

			while ((e = bufferedReader.readLine()) != null) {
				int index1 = e.indexOf("物理地址");
				if (index1 >= 0) {
					index1 = e.indexOf(":");
					if (index1 >= 0) {
						mac = e.substring(index1 + 1).trim();
					}
					break;
				}
			}
		} catch (IOException arg12) {
			arg12.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException arg11) {
				arg11.printStackTrace();
			}

			bufferedReader = null;
			process = null;
		}

		return mac;
	}
}