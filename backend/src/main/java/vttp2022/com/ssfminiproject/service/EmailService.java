package vttp2022.com.ssfminiproject.service;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import vttp2022.com.ssfminiproject.config.JwtTokenUtil;

@Service
public class EmailService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenUtil jUtil;

    @Autowired
    private JavaMailSender jMail;

    @Value("${spring.mail.password}")
    private String mailPassword;
    
    public String getEmail(HttpServletRequest request){

        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        String email = null;
        jwtToken = requestTokenHeader.substring(7);
        username = jUtil.getUsernameFromToken(jwtToken);

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select email from contactinfo inner join users on users.userid = contactinfo.user_id where username = ?", username);
        
        if (!rs.next())
            return null;
        else
            email = rs.getString("email");
        return email;
    }

    public Boolean sendEmail(File convFile, String email) throws MessagingException{
        MimeMessage message = jMail.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("noreply@baeldung.com");
        helper.setTo(email);
        //System.out.println(email);
        helper.setSubject("Tried Recipes");
        helper.setText("Hi, please find the list you've requested.");

        // FileSystemResource filetosend = new FileSystemResource(file);
        helper.addAttachment("myList.pdf", convFile);

        try{
           jMail.send(message);
            return true;
        }catch(Exception e){
            return false;
        }

    }
    
}
