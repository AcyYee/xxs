package com.cwy.xxs.util;

import com.cwy.xxs.vo.ResultData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Update by acy on 2017/8/3.
 */

public class FileUp {

    //图片类型文件
    public static final int IMAGE = 1;
    //音频类型文件
    public static final int AUDIO = 2;
    //视频类型文件
    public static final int VIDEO = 3;
    //文档类型文件
    public static final int DOCUMENT = 4;
    //其他类型文件
    public static final int OTHER = 5;

    
    private static int index=0;
    /**
     * 实现SpringMVC中文件的上传的功能
     *
     * @param files     上传的文件,类型:MultipartFile
     * @param dirNames  文件夹名,多层文件夹名用,号隔开例如:"1,2,3"
     * @param type      类型 图片为1,音频为2,视频为3,文档为4,其他为5 可使用类内的静态变量
     * @param isNewName 是否使用新文件名 boolean 是为true
     * 返回(文件夹名和文件名)或者空 例如:["/dsda/1.jpg","/dsda/2.jpg","/dsda/3.jpg"]
     */
    public static void upFiles(MultipartFile[] files, HttpServletRequest request, String dirNames, int type, boolean isNewName, ResultData[] rs) {
        for (int i = 0; i < files.length; i++) {
            rs[i].setCode(upFile(files[i], request, dirNames, type, isNewName, rs[i]));
        }
    }

    public static int upFile(MultipartFile file, HttpServletRequest request, String dirName, int type, boolean isNewName, ResultData rs) {
        String newName;
        if (file == null || file.isEmpty()) {
            rs.setMessage("文件为空");
            return 0;
        }
        int index = file.getOriginalFilename().lastIndexOf(".");
        String postfix = file.getOriginalFilename().substring(index);

        if (!isTrueType(type, postfix)) {
            rs.setMessage("文件格式不正确");
            return  0;
        }
        if (isNewName) {
            newName = newFileName(type, postfix);
        } else {
            newName = file.getOriginalFilename();
        }
        String path = request.getSession().getServletContext().getRealPath("/");
        StringBuilder builder = new StringBuilder(path);
        builder.append(dirName);
        File temp = new File(builder.toString().trim());
        File temp2 = new File(builder.toString(), newName.trim());
        if (!temp.exists()) {
            if (!temp.mkdirs()) {
                rs.setMessage("无法创建文件夹");
                return 0;
            }
        }
        try {
            file.transferTo(temp2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rs.setObject(dirName + "/" + newName);
        return 100;
    }

    private static boolean isTrueType(int type, String postfix) {
        if (type == IMAGE) {
            return ".jpg".equals(postfix) || ".jpeg".equals(postfix) || ".png".equals(postfix) || ".ico".equals(postfix) || ".gif".equals(postfix);
        } else if (type == AUDIO) {
            return ".mp3".equals(postfix) || ".silk".equals(postfix) || ".wav".equals(postfix);
        } else if (type == VIDEO) {
            return ".mp4".equals(postfix) || ".flv".equals(postfix) || ".avi".equals(postfix) || ".rmvb".equals(postfix);
        } else if (type == DOCUMENT) {
            return ".doc".equals(postfix) || ".ppt".equals(postfix) || ".xls".equals(postfix) || ".docx".equals(postfix) || ".pptx".equals(postfix) || ".xlsx".equals(postfix) || ".txt".equals(postfix) || ".pdf".equals(postfix);
        } else {return type == OTHER;}
    }

    public static String makeNewDir(HttpServletRequest request, String dirName) {
        StringBuilder path = new StringBuilder(request.getSession().getServletContext().getRealPath("/"));
        StringBuilder dirNames = new StringBuilder();
        String[] newDirs = dirName.split(",");
        if (newDirs.length > 1) {
            dirNames.append("");
            for (String newDir : newDirs) {
                dirNames.append("/");
                dirNames.append(newDir);
            }
        } else {
            dirNames.append("/");
            dirNames.append(dirName);
        }
        path.append(dirNames.toString());
        File temp = new File(path.toString().trim());
        if (!temp.exists()) {
            if (!temp.mkdirs()) {
                request.getSession().setAttribute("error", "无法创建文件夹");
            }
        }
        return dirNames.toString();
    }

    private synchronized static String newFileName(int type, String postfix) {
    		if (type == IMAGE) {
    			return "image" + TimeUtil.getDateTime(TimeUtil.FormatType.TO_MS_NONE)+"-" + postfix;
    		} else if (type == AUDIO) {
    			return "audio" + TimeUtil.getDateTime(TimeUtil.FormatType.TO_MS_NONE)+"-"+ postfix;
    		} else if (type == VIDEO) {
    			return "video" + TimeUtil.getDateTime(TimeUtil.FormatType.TO_MS_NONE)+"-" + postfix;
    		} else if (type == DOCUMENT) {
    			return "document" + TimeUtil.getDateTime(TimeUtil.FormatType.TO_MS_NONE)+"-" + postfix;
    		} else {
    			return "file" +TimeUtil.getDateTime(TimeUtil.FormatType.TO_MS_NONE)+"-" + postfix;
    		}
    }

    public static boolean deleteFolder(String deletePath, HttpServletRequest request) {
        StringBuilder path = new StringBuilder(request.getSession().getServletContext().getRealPath("/"));
        StringBuilder dirNames = new StringBuilder("");
        String[] newDirs = deletePath.split(",");
        if (newDirs.length > 1) {
            for (int j = newDirs.length - 1; j >= 0; j--) {
                dirNames.append("/");
                dirNames.append(newDirs[j]);
            }
        }
        path.append(dirNames);
        System.out.println(path);
        File temp = new File(path.toString().trim());
        return temp.delete();
    }

    public static String[] rename(String path, int type, String oldName, String newName, HttpServletRequest request) {
        String[] temp = new String[2];
        StringBuilder dirNames = new StringBuilder("");
        String[] newDirs = path.split(",");
        if (newDirs.length > 1) {
            for (int j = newDirs.length - 1; j >= 0; j--) {
                dirNames.append("/");
                dirNames.append(newDirs[j]);
            }
        }
        if (type == 1) {
            String realPath = request.getServletContext().getRealPath("/") + dirNames;
            File file1 = new File(realPath + "/" + oldName);
            //将原文件夹更改为A，其中路径是必要的。注意
            if (file1.renameTo(new File(realPath + "/" + newName))){
                temp[0] = file1.getName();
                temp[1] = dirNames + "/" + newName;
            }else {
                return null;
            }
        } else {
            String realPath = request.getServletContext().getRealPath("/") + dirNames;
            if (newName.contains(".")) {
                File file1 = new File(realPath + "/" + oldName);
                //将原文件夹更改为A，其中路径是必要的。注意
                if (file1.renameTo(new File(realPath + "/" + newName))){
                    temp[0] = file1.getName();
                    temp[1] = dirNames + "/" + newName;
                }else {
                    return null;
                }
            } else {
                int index = oldName.lastIndexOf(".");
                String postfix = oldName.substring(index);
                File file1 = new File(realPath + "/" + oldName);
                //将原文件夹更改为A，其中路径是必要的。注意
                if (file1.renameTo(new File(realPath + "/" + newName))){
                    temp[0] = file1.getName();
                    temp[1] = dirNames + "/" + newName;
                }else {
                    return null;
                }
            }
        }
        return temp;
    }

}