package pl.galuszka.mapchecker;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @PostConstruct
    private void init() {
        restTemplate = new RestTemplate();
        // sendSimpleMessage("m@galuszka.pl", "Update detected!", "Region " + region);
    }

    @Scheduled(fixedDelay = 4000l)
    public void checkForUpdates() {
        try {
            MapData mapData = restTemplate.getForObject(URI.create("https://www.volkswagen.com/content/medialib/vwd4/global/discovercare/files/configuration/_jcr_content/renditions/rendition.file/discovercare.xml"), MapData.class);

            Optional<MapArchiveFile> mapArchiveFileOpt = mapData.getMapArchive()
                    .stream()
                    .filter(e -> StringUtils.equals(e.getDevice(), device))
                    .filter(e -> StringUtils.equals(e.getMarket(), market))
                    .findFirst();

            if (mapArchiveFileOpt.isPresent()) {
                MapArchiveFile mapArchiveFile = mapArchiveFileOpt.get();
                logger.info("mapArchiveFile: {}", mapArchiveFile.toString());
                File file = mapArchiveFile
                        .getFiles()
                        .stream()
                        .filter(e -> StringUtils.equals(e.getRegionId(), region))
                        .findFirst()
                        .get();
                String md5 = file.getMd5();
                if (!StringUtils.equals(md5, lastMd5)) {
                    String message = MessageFormat.format("Version: {0}\nSystemName: {1}\nURL: {2}", mapArchiveFile.getVersion(), mapArchiveFile.getSystemName(), file.getUrl());
                    sendSimpleMessage("m@galuszka.pl", "New map availiable!", message);
                }
            } else {
                logger.warn("Map Archive not found.");
            }
        } catch (Exception e) {
            logger.error("Exception in checker: ", e);
        }
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
