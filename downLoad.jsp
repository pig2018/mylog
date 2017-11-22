<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>
<%@ page import="com.mylog.common.GetRealPath,com.mylog.common.ZipUtil,java.text.SimpleDateFormat,java.util.Calendar"%>
<%
//先对日志进行压缩
 String strRootPath = config.getServletContext().getRealPath("");
 if(strRootPath==null){
 	strRootPath=GetRealPath.getRootPath();
 }  
String logsDir=strRootPath+File.separatorChar+"logs";
String zipPath=strRootPath+File.separatorChar+"logs.zip";
ZipUtil zip = new ZipUtil(); 
zip.zip(zipPath,logsDir);
//
String filename = (String)request.getParameter("fileName");
if (request.getAttribute("file") != null) {
    filename = (String)request.getAttribute("file");
}
if(filename==null){
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	String ly_time = sdf.format(new java.util.Date()); 
	filename="SystemLog("+ly_time+").zip";
}
System.out.println("filename: "+filename);
response.setContentType("application/*");
response.setHeader("Content-disposition", "attachment; filename="
        + filename);

BufferedInputStream bis = null;
BufferedOutputStream bos = null;
try {
//    bis = new BufferedInputStream(new FileInputStream(getServletContext().getRealPath("/permitLic/" + filename)));
    bis = new BufferedInputStream(new FileInputStream(zipPath));
    
    bos = new BufferedOutputStream(response.getOutputStream());
    byte[] buff = new byte[2048];
    int bytesRead;
    while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
        bos.write(buff, 0, bytesRead);
    }
} catch (final IOException e) {
    System.out.println("IOException." + e);
} finally {
    if (bis != null)
        bis.close();
    if (bos != null)
        bos.close();
    File f=new File(zipPath);
    f.delete();
}
 out.clear();
 out  =  pageContext.pushBody();
%>
