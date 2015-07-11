package com.nao20010128nao.Kanamozic;
import java.io.*;
import android.util.*;

public class KanamozicCore
{
	public static final String CONVERT_ENGLETTERS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	public static final String CONVERT_JPNLETTERS="あいうえおかきくけこさしすせそたちつてとなにぬねのはアイウエオカキクケコサシスセソタチツテトナニヌネノハんをわろれるりらよゆンワ";
	public static final String FULL_JPNLETTERS="あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをんアイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン";
	public static boolean isOnlyKanas(String s){
		for(int i=0;i<s.length();i++){
			if(FULL_JPNLETTERS.indexOf(s.charAt(i))==-1)
				return false;
		}
		return true;
	}
	
	public static String encode(String s,byte key){
		if(isOnlyKanas(s)){
			return 'が'+encodeKana(s,key);
		}else{
			return 'ぎ'+encodeBinary(s,key);
		}
	}
	private static String encodeKana(String s,byte key){
		char[] buf=new char[s.length()];
		for(int i=0;i<s.length();i++){
			char value=s.charAt(i);
			int offset=FULL_JPNLETTERS.indexOf(value);
			int requiredOffset=(offset+key)%FULL_JPNLETTERS.length();
			buf[i]=FULL_JPNLETTERS.charAt(requiredOffset);
		}
		return new String(buf);
	}
	private static String encodeBinary(String s,byte key){
		s=encodeBase64(s);
		char[] buf=new char[s.length()];
		for(int i=0;i<s.length();i++){
			char value=s.charAt(i);
			int offset=CONVERT_ENGLETTERS.indexOf(value);
			int requiredOffset=(offset+key)%CONVERT_JPNLETTERS.length();
			buf[i]=CONVERT_JPNLETTERS.charAt(requiredOffset);
		}
		return new String(buf);
	}

	public static String decode(String s,byte key){
		if(s.charAt(0)=='が'){
			return decodeKana(s.substring(1),key);
		}else if(s.charAt(0)=='ぎ'){
			return decodeBinary(s.substring(1),key);
		}else{
			throw new IllegalArgumentException(s+" is incorrect format. The string must start with 'が'(ga) or 'ぎ'(gi).");
		}
	}
	private static String decodeKana(String s,byte key){
		char[] buf=new char[s.length()];
		int baseLen=FULL_JPNLETTERS.length();
		for(int i=0;i<s.length();i++){
			char value=s.charAt(i);
			int offset=FULL_JPNLETTERS.indexOf(value);
			int requiredOffset=increaseIfNegative(offset-key,baseLen)%baseLen;
			buf[i]=FULL_JPNLETTERS.charAt(requiredOffset);
		}
		return new String(buf);
	}
	private static String decodeBinary(String s,byte key){
		char[] buf=new char[s.length()];
		int baseLen=CONVERT_ENGLETTERS.length();
		for(int i=0;i<s.length();i++){
			char value=s.charAt(i);
			int offset=CONVERT_JPNLETTERS.indexOf(value);
			int requiredOffset=increaseIfNegative(offset-key,baseLen)%baseLen;
			buf[i]=CONVERT_ENGLETTERS.charAt(requiredOffset);
		}
		return decodeBase64(new String(buf));
	}
	
	private static int increaseIfNegative(int value,int once){
		while(value<0){
			value+=once;
		}
		return value;
	}
	
	public static byte[] toByteArray(String s){
		try{
			return s.getBytes("UTF-8");
		}catch (UnsupportedEncodingException e){
			//unreachable in almost platforms
		}
		//unreachable in almost platforms
		return null;
	}
	public static String toString(byte[] array){
		try{
			return new String(array,"UTF-8");
		}catch (UnsupportedEncodingException e){
			//unreachable in almost platforms
		}
		//unreachable in almost platforms
		return null;
	}
	
	/*
	 *FOR DEVELOPERS
	 *
	 *If you want to use this class in another platforms, please modify encodeBase64(String) and decodeBase64(String).
	 */
	public static String encodeBase64(String s){
		return Base64.encodeToString(toByteArray(s),Base64.NO_WRAP|Base64.NO_PADDING);
	}
	public static String decodeBase64(String s){
		return toString(Base64.decode(s,Base64.NO_WRAP|Base64.NO_PADDING));
	}
}
