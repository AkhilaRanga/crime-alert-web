package com.crimealert.utils;

import java.util.HashMap;
import java.util.Map;

import com.crimealert.models.SmtpConfig;

public class EmailServiceProviderConfig {
    private Map<String, SmtpConfig> smtpConfigs;

    public EmailServiceProviderConfig() {
        smtpConfigs = new HashMap<>();

        // Add SMTP configurations for different email service providers
        // Gmail
        smtpConfigs.put("gmail", new SmtpConfig("smtp.gmail.com", 587));

        // Yahoo Mail
        smtpConfigs.put("yahoo", new SmtpConfig("smtp.mail.yahoo.com", 465));

        // Outlook.com (Hotmail)
        smtpConfigs.put("outlook", new SmtpConfig("smtp-mail.outlook.com", 587));

        // AOL Mail
        smtpConfigs.put("aol", new SmtpConfig("smtp.aol.com", 587));

        // Zoho Mail
        smtpConfigs.put("zoho", new SmtpConfig("smtp.zoho.com", 587));
        
        // Add more email service providers as needed
    }

    public SmtpConfig getSmtpConfig(String provider) {
        return smtpConfigs.get(provider.toLowerCase());
    }
}

