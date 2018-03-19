package pl.galuszka.mapchecker;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.NONE)
public class File {

    @XmlAttribute(name = "regionId")
    private String regionId;

    @XmlElement(name = "Url")
    private String url;

    @XmlElement(name = "Md5")
    private String md5;

    @XmlElement(name = "Size")
    private long   size;

}
