package com.dps.function.logs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Level;

import com.dps.bean.OperationLog;
import com.dps.common.MapLog;
import com.dps.logger.LogConfigXml;

public class Log4j {
	/**
	 * 保存系统日志配置
	 * @param request
	 * @param response
	 */
	public void saveLog4jConfig(HttpServletRequest request, HttpServletResponse response){
		boolean b=false;
		String loglevel=request.getParameter("loglevel");
		String logType=request.getParameter("logType");
		Level l=MapLog.getLogLevel(loglevel);
		if(l!=null){
			MapLog.setLogLevel(l);
		}else{
			MapLog.debug("错误的日志级别："+loglevel);
		}
		 if(logType!=null){
			 MapLog.setLogOutType(Integer.parseInt(logType));
		 }
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/html;charset=UTF-8");
		try {
			LogConfigXml logConfigXml=new LogConfigXml();
			HashMap attrs=new HashMap();
			//lo4jLevel,String lo4jOutType
			attrs.put("lo4jLevel", loglevel);
			attrs.put("logType", logType);
			b=logConfigXml.saveConfig(attrs);
			response.getWriter().print(b);
		} catch (IOException e) {
			MapLog.error(e);
		}
	}
	/**
	 * 读取系统日志配置
	 * @param request
	 * @param response
	 */
	public void getLog4jConfig(HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/html;charset=UTF-8");
		HashMap hm=new HashMap();
		hm.put("loglevel", MapLog.getLogLevel().toString());
		hm.put("logType", MapLog.getLogOutType());
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/html;charset=UTF-8");
		JSONObject json = JSONObject.fromObject(hm);
		try {
			response.getWriter().print(json.toString().toLowerCase());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getOperationLogConfig(HttpServletRequest request, HttpServletResponse response){
		List list=OperationLog.getLogTypes();
		String temp="";
		for(int i=0;i<list.size();i++){
			temp+=list.get(i)+",";
		}
		if(!"".equals(temp)){
			temp=temp.substring(0,temp.length()-1);
		}
		try {
			response.getWriter().print(temp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void saveOperationLogConfig(HttpServletRequest request, HttpServletResponse response){
		boolean b=false;
		String logTypes=request.getParameter("logTypes");
		OperationLog.setLogTypes(Arrays.asList(logTypes.split(",")));
		try {
			LogConfigXml logConfigXml=new LogConfigXml();
			HashMap attrs=new HashMap();
			attrs.put("operationTypes", logTypes);
			b=logConfigXml.saveConfig(attrs);
			response.getWriter().print(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readLog4jFile(HttpServletRequest request, HttpServletResponse response){
		String log=this.readLog4jFile();
		if("".equals(log)){
			log="暂无系统日志！";
		}
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().print(log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String getEncode(File file){
        String charset="UTF-8";
        InputStream in=null;
		try {
			 in = new java.io.FileInputStream(file);
			 byte[] b = new byte[3];
		     in.read(b);
		     
		     if (b[0] == -17 && b[1] == -69 && b[2] == -65){
		            System.out.println(file.getName() + "：编码为UTF-8");
		    	 charset="UTF-8";
		     }else{
		            System.out.println(file.getName() + "：可能是GBK，也可能是其他编码");
		    	 charset="GBK";
		     }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(in!=null){
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return charset;
	}
	public String readLog4jFile(){
		BufferedReader br=null;
		int count=0;
		String str=null;
		Stack stack = new Stack();
		StringBuffer sb=new StringBuffer("");
		int maxLine=200;
		try {
			File f=new File(MapLog.getLogPath());
			
			
			//InputStreamReader isr = new InputStreamReader(new FileInputStream(f), getEncode(f));
			InputStreamReader isr = new InputStreamReader(new FileInputStream(f), System.getProperty("file.encoding"));
			//System.out.println("获取系统的编码："+System.getProperty("file.encoding"));
			br = new BufferedReader(isr);
			while ((str = br.readLine()) != null) {
				//System.out.println(str);
				stack.push(str+"\n");
				count++;
				if(count>maxLine){
					stack.remove(0);
				}
			}
			//System.out.println("共有："+count+"行！"+stack.size());
			//System.out.println(stack.toString());
			for(int i=0;i<stack.size();i++){
				sb.append(stack.get(i));
			}
			stack.clear();
			stack=null;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
					br=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	public static void main(String[] args){
		System.out.println(System.getProperty("file.encoding")); 
		//Log4j log=new Log4j();
		//System.out.println(log.readLog4jFile());
	}
}
