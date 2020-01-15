package model.logic.modelcreation;

import model.DTO.GraphVisualisationDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

class Loader {


    public static GraphVisualisationDTO loadGraph(String pathToGraphXML) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(GraphVisualisationDTO.class);
        Unmarshaller um = context.createUnmarshaller();
        return (GraphVisualisationDTO) um.unmarshal(new File(pathToGraphXML));

    }
}
