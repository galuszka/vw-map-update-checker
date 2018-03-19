package pl.galuszka.mapchecker;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name = "MUTconfig")
@XmlAccessorType(XmlAccessType.NONE)
public class MapData {

    @XmlElementWrapper(name = "MapArchive")
    @XmlElement(name = "MapArchiveFile")
    private List<MapArchiveFile> mapArchive;

}
