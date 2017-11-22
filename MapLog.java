package com.dps.common;

import java.io.FileOutputStream;

import org.apache.log4j.Level;

import com.dps.i18n.Catalog;
import com.dps.logger.DPSLogger;

/****
 * 系统日志
 * 
 * @author xiangxsh
 * 
 */
public class MapLog {
	private static String strFileName = null;
	private static String resourcesName ="CommonOther";
	private static FileOutputStream pFileWriter = null;
	public static boolean bIsLog = true;
	static DPSLogger logger =null;
	/***
	 * 真正实现日志信息写入
	 * 
	 * @param Msg
	 */
	public static synchronized void init(){
		if(logger==null){
			logger=DPSLogger.getEhlLogger(strFileName);// 当strFileName为null时系统会自动使用默认值
		}
	}
	public static synchronized void WriteMsg(Object Msg) {
		if(strFileName==null)
		{
			//防止文件路径为空出错
			return;
		}
		init();
		if(logger!=null)
		logger.info(Msg);
	}
	public static synchronized void logger(Object Msg) {
		init();
		if(logger!=null)
		logger.info(Msg);
	}
	public static synchronized void debug(Object Msg) {
		init();
		if(logger!=null)
		logger.debug(Msg);
	}
	public static synchronized void debug(Object Msg,Throwable t) {
		init();
		if(logger!=null)
		logger.debug(Msg,t);
	}
	public static synchronized void info(Object Msg) {
		init();
		if(logger!=null)
		logger.info(Msg);
	}
	public static synchronized void info(Object Msg,Throwable t) {
		init();
		if(logger!=null)
		logger.info(Msg,t);
	}
	
	public static synchronized void warn(Object Msg) {
		init();
		if(logger!=null)
		logger.warn(Msg);
	}
	public static synchronized void warn(Object Msg,Throwable t) {
		init();
		if(logger!=null)
			logger.warn(Msg,t);
	}
	
	public static synchronized void error(Object Msg) {
		init();
		if(logger!=null)
		logger.error(Msg);
	}
	public static synchronized void error(Object Msg,Throwable t) {
		init();
		if(logger!=null)
		logger.error(Msg,t);
	}

	/***
	 * 获取日志文件路径
	 * 
	 * @return
	 */
	public static String getLogPath() {
//		if (LogFile != null) {
//			return LogFile.getAbsolutePath().toString();
//		}
		
		return DPSLogger.getPath();
	}

	/**
	 * 日志文件的完整路径
	 * 
	 * @param strFile
	 */
	public static void setLogFile(String strFile) {
		if(logger==null){
			logger=DPSLogger.getEhlLogger(strFile);
			if(logger==null){
				System.out.println(Catalog.getLocalizationForKey("LogDebug.logPathFailure", resourcesName));
			}
//			logger("配置日志成功！");
		}
	}
	@SuppressWarnings("static-access")
	public static void setLogLevel(Level level) { 
		if(logger==null){
			System.out.println(Catalog.getLocalizationForKey("LogDebug.levelLoggerNULL", resourcesName));
			return;
		}
		logger.setEhlLoggerLevel(level);
	}
	@SuppressWarnings("static-access")
	public static Level getLogLevel() { 
		if(logger==null){
			System.out.println(Catalog.getLocalizationForKey("LogDebug.levelLoggerNULL", resourcesName));
			return null;
		}
		return logger.getDefaultLevel();
	}
	@SuppressWarnings("static-access")
	public static void setLogOutType(int type) { 
		if(logger==null){
			System.out.println(Catalog.getLocalizationForKey("LogDebug.outTypeLoggerNULL", resourcesName));
			return;
		}
		logger.setLogType(type);
		logger.resetLogAppender();
	}
	public static int getLogOutType() { 
		if(logger==null){
			System.out.println(Catalog.getLocalizationForKey("LogDebug.outTypeLoggerNULL", resourcesName));
			return -1;
		}
		return DPSLogger.getLogType();
	}
	/***
	 * 日志前面带日期
	 * 
	 * @param Msg
	 */
	public static void WriteLog(String Msg) {
		WriteMsg(Msg);
	}

	/****
	 * 不换行追加日志内容
	 * 
	 * @param Msg
	 */
	public static void AppendLog(String Msg) {
		WriteMsg(Msg);
	}

	/***
	 * 换行增加日志文件（前面没年月日时分秒）
	 * 
	 * @param Msg
	 */
	public static void AppendlnLog(String Msg) {
		WriteMsg(Msg);
		Writeln();
	}

	/****
	 * 插入换行号
	 */
	public static void Writeln() {
		String newLine = System.getProperty("line.separator");
		WriteMsg(newLine);// 不管用什么打开都是换行的
		// WriteMsg("\n");//用记事本打开没换行，用editplus是换行的
	}

	/***
	 * 插入信息并换行
	 * 
	 * @param strMsg
	 */
	public static void Writeln(String strMsg) {
		String newLine = System.getProperty("line.separator");
		WriteMsg(strMsg + newLine);
		// WriteMsg(strMsg + '\n');
	}

	/***
	 * 前面增加年月日时分秒：插入信息并换行
	 * 
	 * @param strMsg
	 */
	public static void WritelnLog(String Msg) {
		WriteLog(Msg);
		//Writeln();
	}

	public static void close() {
		try {
			pFileWriter.close();
		} catch (Exception ignore) {
			System.out.println(ignore.getMessage());
		}
	}
	public static Level getLogLevel(String loglevel){
		Level l=null;
		if("debug".equalsIgnoreCase(loglevel)){
			l=Level.DEBUG;
		}else if("info".equalsIgnoreCase(loglevel)){
			l=Level.INFO;
		}else if("warn".equalsIgnoreCase(loglevel)){
			l=Level.WARN;
		}else if("error".equalsIgnoreCase(loglevel)){
			l=Level.ERROR;
		}
		return l;
	}
	public static void main(String[] args){
		MapLog.debug("test");
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace(); 
		for(int i=0;i<stacks.length;i++){
			String location = "  at "+stacks[i].getClassName() + "." + stacks[i].getMethodName()
			+ "(" + stacks[i].getFileName() + ":"
			+ stacks[i].getLineNumber() + ")";
			System.out.println(location);
		}
	}
}
