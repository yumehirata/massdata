package jp.co.rakus.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Service;

/**
 * ログイン/アウトの記録をcsvに吐き出す.
 * 
 * @author yume.hirata
 *
 */
@Service
public class LoginLogService {

	/**
	 * ログインの記録を指定csvに吐く.
	 * 
	 * @param name	ログイン者名
	 */
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
	
	/**
	 * ログアウトの記録を指定csvに吐く.
	 * 
	 * @param name	ログイン者名
	 */
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
