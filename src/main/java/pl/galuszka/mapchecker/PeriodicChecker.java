package pl.galuszka.mapchecker;

import java.net.URI;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PeriodicChecker {
    private static final Logger logger = LoggerFactory.getLogger(PeriodicChecker.class);

    private RestTemplate        restTemplate;

    @Autowired
    public JavaMailSender       emailSender;

    @Value("${mapupdate.region}")
    private String              region;

    @Value("${mapupdate.market}")
    private String              market;

    @Value("${mapupdate.device}")
    private String              device;

    @Value("${spring.mail.username}")
    private String              fromEmail;

    @Value("${mapupdate.lastKnownMD5}")
    private String              lastMd5;

    @Value("${mapupdate.url}")
    private String              url;

    @Value("${notify.email}")
    private String              notifyEmail;

    public PeriodicChecker() {
        restTemplate = new RestTemplate();
    }

    @PostConstruct
    private void init() {
        logger.info("Initializing... spring.mail.username: {}, notify.email: {}", fromEmail, notifyEmail);
    }

    @Scheduled(fixedDelay = 3600000l, initialDelay = 1000)
    public void checkForUpdates() {
        try {
            URI uri = URI.create(url);

            if (logger.isDebugEnabled()) {
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
                logger.debug(responseEntity.toString());
            }

            MapData mapData = restTemplate.getForObject(uri, MapData.class);

            MapArchiveFile mapArchiveFile = mapData.getMapArchive()
                    .stream()
                    .filter(e -> StringUtils.equals(e.getDevice(), device))
                    .filter(e -> StringUtils.equals(e.getMarket(), market))
                    .findFirst()
                    .get();

            logger.debug("mapArchiveFile: {}", mapArchiveFile.toString());

            File file = mapArchiveFile
                    .getFiles()
                    .stream()
                    .filter(e -> StringUtils.equals(e.getRegionId(), region))
                    .findFirst()
                    .get();

            String md5 = file.getMd5();

            logger.info("Version: {}, systemName: {}, url: {}, md5: {}", mapArchiveFile.getVersion(), mapArchiveFile.getSystemName(), file.getUrl(), md5);

            if (!StringUtils.equals(md5, lastMd5)) {
                String message = MessageFormat.format("Version: {0}\nSystemName: {1}\nURL: {2}", mapArchiveFile.getVersion(), mapArchiveFile.getSystemName(), file.getUrl());
                sendSimpleMessage(notifyEmail, "New map availiable!", message);
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled())
                logger.error("Exception in checker: ", e);
            else
                logger.error("Exception in checher: {}, {}", e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        logger.info("Sending email message to '{}', subject: '{}', body: '{}'", to, subject, text);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
