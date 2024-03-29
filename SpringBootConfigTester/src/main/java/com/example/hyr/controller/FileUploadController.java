package com.example.hyr.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
public class FileUploadController {

	/* 되는데 인풋으로 받아온 데이터를 쓰는게 아니라 아웃풋을 새로 만들어서 씀 */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleUpload(HttpServletRequest request) {
        System.out.println("@@@ >> " + System.getProperty("java.io.tmpdir"));
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
        factory.setFileCleaningTracker(null);
        
        // Configure a repository (to ensure a secure temp location is used)
        ServletFileUpload upload = new ServletFileUpload(factory);
        
        try {
            // Parse the request
            List<FileItem> items = upload.parseRequest(request);
        
            // Process the uploaded items
            Iterator<FileItem> iter = items.iterator();
            
            while (iter.hasNext()) {
                FileItem item = iter.next();

                if (!item.isFormField()) {
                    try (InputStream uploadedStream = item.getInputStream();
                            OutputStream out = new FileOutputStream("file.mov");) {
                        IOUtils.copy(uploadedStream, out);
                        out.close();
                    }
                }
            }
            
            // Parse the request with Streaming API
            upload = new ServletFileUpload();
            FileItemIterator iterStream = upload.getItemIterator(request);
            while (iterStream.hasNext()) {
                FileItemStream item = iterStream.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (!item.isFormField()) {
                    //Process the InputStream
                } else {
                    //process form fields
                    String formFieldValue = Streams.asString(stream);
                }
            }
            return "success!";
        } catch (IOException | FileUploadException ex) {
            return "failed: " + ex.getMessage();
        }
    }
    
    /* 안됨 */
    @RequestMapping(value="/upload2", method=RequestMethod.POST)
	public String uploadTest(HttpServletRequest req, MultipartHttpServletRequest mre){
		
		String test = req.getParameter("username"); // jsp text name mapping
		MultipartFile mf = mre.getFile("file"); // jsp file name mapping
		String uploadPath = "";
		
		String path = "C:\\"+"yurim\\"+"javaUpload\\"; // ���� ���ε� ���
			
		String original = mf.getOriginalFilename(); // ���ε��ϴ� ���� name
			
		System.out.println("!!!!!!!!!!"+test);		// text value
		System.out.println("!!!!!!!!!!"+original);	// file original name
		System.out.println("!!!!!!!!!!"+mf.getSize());// file size
			
		uploadPath = path+original; // ���� ���ε� ��� + ���� �̸�
		
		
		try {
			mf.transferTo(new File(uploadPath)); // ������ ���� ���� ��η� ���ε�
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}

}