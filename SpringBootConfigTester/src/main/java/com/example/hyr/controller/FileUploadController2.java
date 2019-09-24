package com.example.hyr.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.hyr.model.response.Response;

@Controller
public class FileUploadController2 {
	
	/* 됨. 받아온 파일 그대로 읽어서 씀 */
    @RequestMapping(value="/upload3", method=RequestMethod.POST)
    public @ResponseBody Response<String> upload(HttpServletRequest request) {
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                // Inform user about invalid request
                Response<String> responseObject = new Response<String>(false, "Not a multipart request.", "");
                return responseObject;
            }

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload();

            // Parse the request
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (!item.isFormField()) {
                    String filename = item.getName();
                    // Process the input stream
                    OutputStream out = new FileOutputStream(filename);
                    IOUtils.copy(stream, out);
                    stream.close();
                    out.close();
                }
            }
        } catch (FileUploadException e) {
            return new Response<String>(false, "File upload error", e.toString());
        } catch (IOException e) {
            return new Response<String>(false, "Internal server IO error", e.toString());
        }

        return new Response<String>(true, "Success", "");
    }

    @RequestMapping(value = "/uploader", method = RequestMethod.GET)
    public ModelAndView uploaderPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("uploader");
        return model;
    }
}