package com.api.gestion.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> list){
        SimpleMailMessage  message = new SimpleMailMessage();
        message.setFrom("ams212001test@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if (list != null && list.size() > 0){
            message.setCc(getCcArray(list));
        }
        javaMailSender.send(message);
    }

    // cc -> significa un acopia del mensaje
    private String[] getCcArray(List<String> cclist){
        String[] cc = new  String[cclist.size()];
        for (int i=0; i < cclist.size(); i++){
            cc[i]  = cclist.get(i);
        }
        return cc;
    }
}