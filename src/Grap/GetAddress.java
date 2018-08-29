package Grap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetAddress {

	private String basicUrl;
	
	public GetAddress(String year) {
		basicUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/" + year + "/";
	}
	
	public void doGet() throws IOException {
		HttpRequest request=HttpRequest.getInstance();
		request.setCharset("gb2312");
		File f;//=new File("D:/CityInfo/provincetr_11.txt");
		OutputStream ots;//=new FileOutputStream(f);
		OutputStreamWriter wr;//=new OutputStreamWriter(ots,"gb2312");
		try {
			String data=request.doGet(basicUrl);

			// Load ID & Url
			Pattern pattern = Pattern.compile("(\\d+)\\.html'>(\\D+)</a>");
			Matcher matcher = pattern.matcher(data);
			
			for(int i=0;i<3;i++)matcher.find();// Temp
			
			while (matcher.find()) {
				String info = matcher.group();
				String aUrl = basicUrl + info.replaceAll(".>.*", "");
				String aData = info.replaceAll("\\w|\\.|<|>|/|'", "");

				System.out.println(aData);
				System.out.println(aUrl);

				StringBuilder json=new StringBuilder("{\"");
				json.append(aData);
				json.append("\":[");
				json.append(getInfo(aUrl));
				json.append("]}");
				
				// Save to file
				f=new File("D:/CityInfo/provincetr_" + matcher.group(1) + ".txt");
				ots=new FileOutputStream(f);
				wr=new OutputStreamWriter(ots,"gb2312");
				wr.append(json);
				wr.close();
				ots.close();
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * grap info by Url
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String getInfo(String url) throws Exception {
		StringBuilder json = new StringBuilder("");
		HttpRequest request=HttpRequest.getInstance();
		String data = request.doGet(url);

		// 请求出错的我时候
		int y = 0;
		while ("".equals(data) || null == data) {
			if (y == 10) {
				break;
			}
			data = request.doGet(url);
			y++;
		}

		if ("".equals(data) || null == data) {
			throw new Exception("未请求到数据");
		}
		// 取得对应区域的数据
		Pattern pattern = Pattern.compile("<tr class='[a-z]*'>.+?</tr>");
		Matcher matcher = pattern.matcher(data);
		//int x = 0;
		while (matcher.find()) {

			//if (x == 0) {
			//	x++;
			//	continue;
			//}
			String info = matcher.group();
			// 获得正确的url
			String status = url.replaceAll("\\d+\\.html", "");
			String shh = getDataByRegex(info, "\\d+/\\d+.html");
			// 匹配到url
			String aUrl = status + shh;
			// 匹配Id
			String aId = getDataByRegex(info, "\\d{12}");
			// 匹配中文
			String aData = getDataByRegex(info, "[\u4e00-\u9fa5]+");
			// 打印匹配信息
			// System.out.println(aUrl);
			System.out.println(aId);
			System.out.println(aData);
			// 添加匹配带的信息
			json.append("{\"id\":\"");
			json.append(aId);
			json.append("\",\"name\":\"");
			json.append(aData);
			json.append("\"");
			if (!"".equals(shh)) {
				json.append(",\"children\":[");
				json.append(getInfo(aUrl));
				json.append("]");
			}
			json.append("},");
		}
		return json.toString();
	}

	/**
	 * 执行正则
	 * 
	 * @param data
	 * @param regex
	 * @return
	 */
	public String getDataByRegex(String data, String regex) {
		try {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(data);
			matcher.find();
			return matcher.group();
		} catch (Exception e) {
			return "";
		}
	}
}
