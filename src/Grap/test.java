package Grap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
	public static void main(String[] args) {
		String basic="http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/";
		HttpRequest req=HttpRequest.getInstance();
		req.setCharset("gb2312");
		String data=req.doGet(basic);
		Pattern pattern = Pattern.compile("(\\d+)\\.html'>(\\D+)</a>");
		Matcher matcher = pattern.matcher(data);
		while(matcher.find())
		{
			StringBuilder json=new StringBuilder();
			String info = matcher.group();
			String aUrl = basic + info.replaceAll(".>.*", "");
			String aData = info.replaceAll("\\w|\\.|<|>|/|'", "");
			json.append("{\"ID\":\"");
			json.append(matcher.group(1));
			json.append("\",\"Name\":\"");
			json.append(aData);
			json.append("\",\"Url\":\"");
			json.append(aUrl);
			json.append("\"}");
			System.out.println(json);
		}
	}
}
