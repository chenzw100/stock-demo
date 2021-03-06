package com.example.stockdemo.mail;

import com.example.stockdemo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
@Configuration
public class MailSendUtil {
    //https://www.cnblogs.com/V1haoge/p/7183408.html
    private  static String formName = "";//你的邮箱
    private  static String password = ""; //授权码
    private final static String host = "smtp.163.com"; //163的服务器
    private  static String toAddress = formName;
    private  static String replayAddress = formName; //你的邮箱

    public static void sendHtmlMail(MailInfo info)throws Exception{
        info.setHost(host);
        info.setFormName(formName);
        info.setFormPassword(password);   //网易邮箱的授权码~不一定是密码
        info.setReplayAddress(replayAddress);
        info.setToAddress(toAddress);
        Message message = getMessage(info);
        // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
        Multipart mainPart = new MimeMultipart();
        // 创建一个包含HTML内容的MimeBodyPart
        BodyPart html = new MimeBodyPart();
        // 设置HTML内容
        html.setContent(info.getContent(), "text/html; charset=utf-8");
        mainPart.addBodyPart(html);
        // 将MiniMultipart对象设置为邮件内容
        message.setContent(mainPart);
        Transport.send(message);
    }

    public static void sendTextMail(MailInfo info) throws Exception {

        info.setHost(host);
        info.setFormName(formName);
        info.setFormPassword(password);   //网易邮箱的授权码~不一定是密码
        info.setReplayAddress(replayAddress);
        info.setToAddress(toAddress);
        Message message = getMessage(info);
        //消息发送的内容
        message.setText(info.getContent());
        Transport.send(message);
    }

    private static Message getMessage(MailInfo info) throws Exception{
        final Properties p = System.getProperties() ;
        p.setProperty("mail.smtp.host", info.getHost());
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.user", info.getFormName());
        p.setProperty("mail.smtp.pass", info.getFormPassword());

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = Session.getInstance(p, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(p.getProperty("mail.smtp.user"),p.getProperty("mail.smtp.pass"));
            }
        });
        session.setDebug(true);
        Message message = new MimeMessage(session);
        //消息发送的主题
        message.setSubject(info.getSubject());
        //接受消息的人
        message.setReplyTo(InternetAddress.parse(info.getReplayAddress()));
        //消息的发送者
        message.setFrom(new InternetAddress(p.getProperty("mail.smtp.user"),"选股"));
        // 创建邮件的接收者地址，并设置到邮件消息中
        message.setRecipient(Message.RecipientType.TO,new InternetAddress(info.getToAddress()));
        // 消息发送的时间
        message.setSentDate(MyUtils.getCurrentDate());


        return message ;
    }

    public static void sendMail(String content){
        MailInfo info = new MailInfo();
        info.setContent(content);
        try {
            //MailSendUtil.sendTextMail(info);
            MailSendUtil.sendHtmlMail(info);
        } catch (Exception e) {
            System.out.print("'的邮件发送失败！");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        String content = "test-content";
        MailInfo info = new MailInfo();
        info.setContent(content);
        try {
            MailSendUtil.sendTextMail(info);
            //MailSendUtil.sendHtmlMail(info);
        } catch (Exception e) {
            System.out.print("'的邮件发送失败！");
            e.printStackTrace();
        }
    }


    @Value("${mail.formName}")
    public  void setFormName(String formName) {
        MailSendUtil.formName = formName;
    }

    @Value("${mail.password}")
    public  void setPassword(String password) {
        MailSendUtil.password = password;
    }

    public static String getFormName() {
        return formName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getToAddress() {
        return toAddress;
    }
    @Value("${mail.formName}")
    public  void setToAddress(String toAddress) {
        MailSendUtil.toAddress = toAddress;
    }

    public static String getReplayAddress() {
        return replayAddress;
    }
    @Value("${mail.formName}")
    public  void setReplayAddress(String replayAddress) {
        MailSendUtil.replayAddress = replayAddress;
    }
}
