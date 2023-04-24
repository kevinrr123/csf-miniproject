package vttp2022.com.ssfminiproject.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.com.ssfminiproject.service.EmailService;

@RestController
@RequestMapping(path = "/api/email")
public class EmailRESTController {

    @Autowired  
    EmailService eSvc;
    
    @PostMapping(path = "/send")
    public ResponseEntity<String> emailList(@RequestPart MultipartFile file, HttpServletRequest request)
            throws IOException, MessagingException {

        String email = eSvc.getEmail(request); 
        File newFile = new File("myList.pdf");
        newFile.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            fos.write(file.getBytes());
            System.out.println(">>>>>> body:" + fos);
        }
        File nfile = new File("myList.pdf");
        file.transferTo(nfile);

        try {
            Boolean send = eSvc.sendEmail(newFile, email);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(send.toString());
        } catch (Exception ex) {
            JsonObject body = Json.createObjectBuilder().add("error", ex.getMessage()).build();
            return ResponseEntity.internalServerError().body(body.toString());
        }
    }
}
