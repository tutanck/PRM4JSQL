package db.tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
/**
 * useful tools for SQL database
 * @author joan
 */
public class DBToolBox{

	/**
	 * Return the complete StackTrace of the throwable as String 
	 * @param thr
	 * @return */
	public static String getStackTrace(Throwable thr){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		thr.printStackTrace(pw);
		return sw.toString(); // stack trace as a string
	}

	/**
	 * @description return the current time in format day/month/year/ hour:minutes:seconds
	 * @return */
	public static String getCurrentTime(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy/ HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date); //21/02/2015/ 13:52:11
	}


	/**
	 * @description return the current timestamp
	 * @return */
	public static Timestamp getCurentTimeStamp(){
		return new Timestamp(new Date().getTime());
	}


	/**
	 * @description Generate key(sequence of 32 hexadecimal digits)
	 *  with The 128-bit MD5 hash algorithm 
	 * @return */
	public static String generateMD5ID(){
		String hashtext=null;
		try {
			MessageDigest m =MessageDigest.getInstance("MD5");
			m.reset();
			m.update(getCurentTimeStamp().toString().getBytes());
			hashtext=new BigInteger(1,m.digest()).toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while(hashtext.length() < 32 )
				hashtext = "0"+hashtext;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();hashtext=generateHomeMadeID();}
		return  hashtext;
	}

	/**
	 * @description  Home made 32 characters ID generated 
	 * @return 
	 */
	public static String generateHomeMadeID()  {
		char[] tab = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		for (int i = 0; i < 32; i++)
			sb.append( tab[r.nextInt( tab.length )] );
		return sb.toString();
	}


	public static void main(String[] args) {
		System.out.println(getCurrentTime());
		System.out.println(generateMD5ID());
	}
}
