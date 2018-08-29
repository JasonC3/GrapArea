package Grap;

import java.io.IOException;

public class Process {
	public static void main(String[] args) {
		GetAddress grap=new GetAddress("2017");
		try {
			grap.doGet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
