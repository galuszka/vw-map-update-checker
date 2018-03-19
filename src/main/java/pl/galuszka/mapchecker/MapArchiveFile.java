package pl.galuszka.mapchecker;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.NONE)
public class MapArchiveFile {
    @XmlElement(name = "Version")
    private String     version;

    @XmlElement(name = "SystemName")
    private String     systemName;

    @XmlElement(name = "Device")
    private String     device;

    @XmlElement(name = "Market")
    private String     market;

    @XmlElementWrapper(name = "Files")
    @XmlElement(name = "File")
    private List<File> files;
}
