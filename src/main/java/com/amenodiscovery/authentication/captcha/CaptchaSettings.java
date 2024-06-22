package com.amenodiscovery.authentication.captcha;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
@Setter
@Getter
public class CaptchaSettings {

    private String site;
    private String secret;
    
    //reCAPTCHA V3
    private String siteV3;
    private String secretV3;
    private float threshold;

    public CaptchaSettings() {
    }
}
