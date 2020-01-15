package model.logic.modelcreation;

import model.DTO.GraphVisualisationDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class Saver {

    public static void saveGraph(GraphVisualisationDTO graphDTO, String path) {
        try {
            JAXBContext context = JAXBContext.newInstance(GraphVisualisationDTO.class);
            Marshaller ms = context.createMarshaller();
            ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ms.marshal(graphDTO, new File(path));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
