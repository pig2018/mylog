

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException; // import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/****
 * 
 * @author xiangxsh
 * 
 */
public class FileOperate {
	public static boolean createFolder(String folderPath) throws IOException {
		boolean result = false;

		if ((folderPath == null) || (folderPath.equalsIgnoreCase(""))) {
			return result;
		}
		File f = new File(folderPath);
		if (!(f.exists())) {
			result = f.mkdirs();
//			System.out.println("创建目录："+folderPath);
		}
		return result;
	}

	public void deleteFolder(File dir) {
		File[] filelist = dir.listFiles();
		int listlen = filelist.length;
		for (int i = 0; i < listlen; ++i) {
			if (filelist[i].isDirectory())
				deleteFolder(filelist[i]);
			else {
				filelist[i].delete();
			}
		}

		dir.delete();
	}
	/**
	 * 删除超过指定时间的文件 默认单位 秒
	 */
	public void deleteFolderFiles(File dir,long timeToSave){
		this.deleteFolderFiles(dir,timeToSave,"S");
	}
	public void deleteFolderFiles(File dir,long timeToSave,String unit){
		if(dir.isDirectory()){
			File[] files=dir.listFiles();
			long now=new Date().getTime();
			long modTime=0;
			long minTime=0;
//			long timeToSave=1;
//			String unit="D";//只保存1天
			for(int i=0;i<files.length;i++){
				modTime=files[i].lastModified();
				minTime=now-modTime;
				if(unit.toUpperCase().equals("D")){
					minTime=minTime/(3600000*24);
				}else if(unit.toUpperCase().equals("H")){
					minTime=minTime/3600000;
				}else if(unit.toUpperCase().equals("M")){
					minTime=minTime/60000;
				}else if(unit.toUpperCase().equals("S")){
					minTime=minTime/1000;
				}
				if(minTime>timeToSave){
					if(files[i].isDirectory()){
						deleteFolder(files[i]);
					}else{
						files[i].delete();
					}
				}
			}
		}
	}
	public static boolean makeFile(String filepath) throws IOException {
		boolean result = false;
		File file = new File(filepath);
		result = file.createNewFile();
		file = null;
		return result;
	}

	public static boolean isDel(String filepath) {
		boolean result = false;
		File file = new File(filepath);
		result = file.delete();
		file = null;
		return result;
	}

	public static boolean renamefile(String filepath, String destname) {
		boolean result = false;
		File f = new File(filepath);
		String fileParent = f.getParent();
		String filename = f.getName();
		File rf = new File(fileParent + "//" + destname);
		if (f.renameTo(rf)) {
			result = true;
		}
		f = null;
		rf = null;
		return result;
	}

	/****
	 * 
	 * @param filepath
	 * @param content
	 * @throws Exception
	 */
	public static void WriteFile(String filepath, String content)
			throws Exception {
		FileWriter filewriter = new FileWriter(filepath, true);
		PrintWriter printwriter = new PrintWriter(filewriter);
		printwriter.println(content);
		printwriter.flush();
		printwriter.close();
		filewriter.close();
	}

	/***
	 * 
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public static String ReadFile(String filepath) throws Exception {
		StringBuffer strContent = new StringBuffer();
		FileReader input = new FileReader(filepath);
		BufferedReader bufRead = new BufferedReader(input);

		String line = bufRead.readLine();

		while (line != null) {
			strContent.append(line);

			line = bufRead.readLine();
		}
		if (bufRead != null)
			bufRead.close();
		if (input != null)
			input.close();

		return strContent.toString();
	}

	public static void main(String[] arg) {
		try {
			System.out.println("111");
			String str1 = ReadFile("E:\\T");
			System.out.println("1111");
			System.out.println("str1:"+str1);
			//String str2 = ReadFile("D://STSS");
			//WriteFile("E:\\FILETEST\\SSS.jsp", str1);
		} catch (Exception e) {

		}

	}

	/****
	 * 
	 * @param filePath
	 * @param baksize
	 * @throws IOException
	 */
	public static void logBak(String filePath, long baksize) throws IOException {
		File f = new File(filePath);
		long len = f.length();
		SimpleDateFormat simpledateformat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		String s = simpledateformat.format(new Date());
		String fileName = f.getName();
		int dot = fileName.indexOf(".");
		String bakName = s + fileName.substring(dot);
		System.out.println(bakName);
		if (len >= baksize) {
			renamefile(filePath, bakName);
			makeFile(filePath);
		}
		f = null;
	}

	public static boolean bFileExist(String strFile) {
		boolean bIsExist = false;
		File pFile = new File(strFile);
		if (pFile.exists()) {
			bIsExist = true;
		}
		return bIsExist;
	}

	public static boolean deleteFile(String strFile) {
		boolean bIsOK = false;
		File pFile = new File(strFile);
		if (pFile.exists()) {
			bIsOK = pFile.delete();
		}

		return bIsOK;
	}
	 // 复制文件
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    // 复制文件夹
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        // 新建目标目录
        //(new File(targetDir)).mkdirs();
    	FileOperate.createFolder(targetDir);
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + File.separator + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + File.separator + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }


}
