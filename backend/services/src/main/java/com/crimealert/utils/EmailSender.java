package com.crimealert.utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public static boolean sendOTP(String recipientEmail, String otp, String provider) {
        // Sender's email and password - created
        String senderEmail = "webalertproject@gmail.com";
        String senderPassword = "gwxiujlgokshpuah";
        
        //Get SMTP server details
        EmailServiceProviderConfig emailServiceProviderConfig = new EmailServiceProviderConfig();
        

        // SMTP server details
        String smtpHost = emailServiceProviderConfig.getSmtpConfig(provider).getHost();
        int smtpPort = emailServiceProviderConfig.getSmtpConfig(provider).getPort();

        // Email subject and body
        String emailSubject = "OTP Verification";
        String emailBody = "Your OTP is: " + otp;

        try {
            // Set properties for the SMTP server
            Properties properties = new Properties();
            properties.put("mail.smtp.host", smtpHost);
            properties.put("mail.smtp.port", smtpPort);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // Create a session with authentication
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(emailSubject);
            message.setText(emailBody);

            // Send the email
            Transport.send(message);

            System.out.println("OTP sent successfully!");
            
            return true;
        } catch (MessagingException e) {
            System.out.println("Failed to send OTP: " + e.getMessage());
            return false;
        }
        catch (Exception e) {
            System.out.println("Other exception: " + e.getMessage());
            return false;
        }
    }
}

