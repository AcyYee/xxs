package com.cwy.xxs.controller;

import com.cwy.xxs.util.FileUp;
import com.cwy.xxs.vo.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("fileInfo")
public class  FileController {

	/**
	 * 单文件上传
	 * @param file 文件对象
	 * @param type 文件类型
	 * @return 返回结果
	 */
    @PostMapping("upload")
    public ResultData upload(MultipartFile file, Integer type, HttpServletRequest request, HttpServletResponse rep){
        rep.setHeader("Access-Control-Allow-Origin","*");
        ResultData rs = new ResultData();
        rs.setCode(FileUp.upFile(file,request,"resources/upload",type,true,rs));
        return rs;
    }

    /**
	 * 多文件上传s
	 * @param types 文件类型
	 * @return 返回结果
	 */
    @PostMapping("uploads")
    public ResultData[] uploads(MultipartFile[] files, Integer[] types, HttpServletRequest request, HttpServletResponse rep){
        rep.setHeader("Access-Control-Allow-Origin","*");
        ResultData[] rs = new ResultData[files.length];
        FileUp.upFiles(files,request,"resources/upload",types,true,rs);
        return rs;
    }

}
