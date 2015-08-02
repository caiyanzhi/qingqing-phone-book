package com.example.qingqing701;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.os.Message;
import android.util.Log;

/**
* Android Zipѹ����ѹ��
* @author ronald ([url]www.r-base.net[/url])
*/
public final class ZipUtil {
  private ZipUtil(){
  }

  /**
   * ȡ��ѹ�����е� �ļ��б�(�ļ���,�ļ���ѡ)
   * @param zipFileString                ѹ��������
   * @param bContainFolder        �Ƿ���� �ļ���
   * @param bContainFile                �Ƿ���� �ļ�
   * @return
   * @throws Exception
   */
  public static java.util.List<java.io.File> getFileList(String zipFileString, boolean bContainFolder, 
          boolean bContainFile)throws Exception {
    java.util.List<java.io.File> fileList = new java.util.ArrayList<java.io.File>();
    java.util.zip.ZipInputStream inZip = 
                     new java.util.zip.ZipInputStream(new java.io.FileInputStream(zipFileString));
    java.util.zip.ZipEntry zipEntry;
    String szName = "";                
    while ((zipEntry = inZip.getNextEntry()) != null) {
        szName = zipEntry.getName();
        if (zipEntry.isDirectory()) {
          // get the folder name of the widget
          szName = szName.substring(0, szName.length() - 1);
          java.io.File folder = new java.io.File(szName);
          if (bContainFolder) {
            fileList.add(folder);
          }
        } else {
          java.io.File file = new java.io.File(szName);
          if (bContainFile) {
            fileList.add(file);
          }
        }
    }//end of while                
    inZip.close();
    return fileList;
  }

  /**
   * ����ѹ�����е��ļ�InputStream
   * 
   * @param zipFilePath        ѹ���ļ�������
   * @param fileString        ��ѹ�ļ�������
   * @return InputStream
   * @throws Exception
   */
public static java.io.InputStream upZip(String zipFilePath, String fileString)throws Exception {
        java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(zipFilePath);
        java.util.zip.ZipEntry zipEntry = zipFile.getEntry(fileString);

        return zipFile.getInputStream(zipEntry);
}

/**
* ��ѹһ��ѹ���ĵ� ��ָ��λ��
* @param zipFileString        ѹ����������
* @param outPathString        ָ����·��
* @throws Exception
*/
public static void unZipFolder(InputStream input, String outPathString)throws Exception {
        java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(input);
        java.util.zip.ZipEntry zipEntry = null;
        String szName = "";
        Log.d("sfdfafdf","0");
        while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();

                if (zipEntry.isDirectory()) {
                  // get the folder name of the widget
                	szName = szName.substring(0, szName.length() - 1);
                	java.io.File folder = new java.io.File(outPathString + java.io.File.separator + szName);
                	Log.d("sfdfafdf","1");
                	folder.mkdirs();
                	Message mess = new Message();
                	mess.what = 6;
  					mess.obj = "root";
  					MainActivity.login_handler.sendMessage(mess);
                } else {
                	
                	java.io.File file = new java.io.File(outPathString + java.io.File.separator + szName);
                	file.createNewFile();
                  // get the output stream of the file
                	java.io.FileOutputStream out = new java.io.FileOutputStream(file);
                	int len;
                	byte[] buffer = new byte[1024];
                  // read (len) bytes into buffer
                	while ((len = inZip.read(buffer)) != -1) {
                        // write (len) byte from buffer at the position 0
                        out.write(buffer, 0, len);
                        out.flush();
                	}
                	out.close();
                }
        }//end of while
        
        }

        /**
         * ��ѹһ��ѹ���ĵ� ��ָ��λ��
         * @param zipFileString        ѹ����������
         * @param outPathString        ָ����·��
         * @throws Exception
         */
        public static void unZipFolder(String zipFileString, String outPathString)throws Exception {

        	Log.d("into zip file name", zipFileString);
           try
           {
        	   ZipFile zipFile = new ZipFile(zipFileString);
               Enumeration<?> emu = zipFile.entries();
                while(emu.hasMoreElements()){
                   ZipEntry entry = (ZipEntry)emu.nextElement();
                   if (entry.isDirectory())
                    {
                	   Log.d("zip = ",outPathString+'/'+entry.getName() );
                       File f = new File(outPathString,entry.getName());
                       if(f.exists() == false)
                       {
                    	   f.mkdir();//mkdirs ����󣿣���
                       }
                       continue;
                   }
                   BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                   File file = new File(outPathString,entry.getName());
                   file.createNewFile();
                   File parent = file.getParentFile();
                    if(parent != null && (!parent.exists())){
                       parent.mkdir();
                   }
                   
                    Log.d("zip file = ",outPathString+'/'+entry.getName() );
                   int BUFFER = 1024*4;
                   FileOutputStream fos = new FileOutputStream(file);
                   BufferedOutputStream bos = new BufferedOutputStream(fos,BUFFER);           
                   
                   int count;
                   byte data[] = new byte[BUFFER];
                   while ((count = bis.read(data, 0, BUFFER)) != -1)
                    {
                       bos.write(data, 0, count);
                   }
                   bos.flush();
                   bos.close();
                   bis.close();
               }
               zipFile.close();
            } 
           	catch (Exception e) {
               e.printStackTrace();
            }
        }//end of func


        /**
         * ѹ���ļ�,�ļ���
         * 
         * @param srcFilePath        Ҫѹ�����ļ�/�ļ�������
         * @param zipFilePath        ָ��ѹ����Ŀ�ĺ�����
         * @throws Exception
         */
        public static void zipFolder(String srcFilePath, String zipFilePath)throws Exception {
          //����Zip��
          java.util.zip.ZipOutputStream outZip = 
              new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFilePath));

          //��Ҫ������ļ�
          java.io.File file = new java.io.File(srcFilePath);

          //ѹ��
          zipFiles(file.getParent()+java.io.File.separator, file.getName(), outZip);

          //���,�ر�
          outZip.finish();
          outZip.close();

        }//end of func

        /**
         * ѹ���ļ�
         * @param folderPath
         * @param filePath
         * @param zipOut
         * @throws Exception
         */
        private static void zipFiles(String folderPath, String filePath, 
                     java.util.zip.ZipOutputStream zipOut)throws Exception{
          if(zipOut == null){
            return;
          }

          java.io.File file = new java.io.File(folderPath+filePath);

          //�ж��ǲ����ļ�
          if (file.isFile()) {
            java.util.zip.ZipEntry zipEntry =  new java.util.zip.ZipEntry(filePath);
            java.io.FileInputStream inputStream = new java.io.FileInputStream(file);
            zipOut.putNextEntry(zipEntry);

            int len;
            byte[] buffer = new byte[4096];

            while((len=inputStream.read(buffer)) != -1) {
                 zipOut.write(buffer, 0, len);
            }

             zipOut.closeEntry();
          } else {
           //�ļ��еķ�ʽ,��ȡ�ļ����µ����ļ�
           String fileList[] = file.list();

           //���û�����ļ�, ����ӽ�ȥ����
           if (fileList.length <= 0) {
                  java.util.zip.ZipEntry zipEntry =  
                       new java.util.zip.ZipEntry(filePath+java.io.File.separator);
                zipOut.putNextEntry(zipEntry);
                zipOut.closeEntry();                                
           }

           //��������ļ�, �������ļ�
           for (int i = 0; i < fileList.length; i++) {
                zipFiles(folderPath, filePath+java.io.File.separator+fileList[i], zipOut);
           }//end of for

         }//end of if

     }//end of func
}