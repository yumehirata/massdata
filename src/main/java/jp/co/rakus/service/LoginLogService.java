package jp.co.rakus.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class LoginLogService {

	public void loginLog(String name) {

		Date date = new Date();
		String fileName="massdata_log";
		try {
			FileWriter fw = new FileWriter("C:\\env\\spring-workspace\\"+ fileName + ".csv",true);
			fw.write("login");
			fw.write(",");
			fw.write(name);
			fw.write(",");
			fw.write(date.toString());
			fw.write("\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void logoutLog(String name) {
		Date date = new Date();
		String fileName="massdata_log";
		try {
			FileWriter fw = new FileWriter("C:\\env\\spring-workspace\\"+ fileName + ".csv",true);
			fw.write("logout");
			fw.write(",");
			fw.write(name);
			fw.write(",");
			fw.write(date.toString());
			fw.write("\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
