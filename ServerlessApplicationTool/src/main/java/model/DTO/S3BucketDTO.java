package model.DTO;

import model.structure.Node;
import model.structure.S3Bucket;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class S3BucketDTO extends NodeDTO {

    public String bucketName = "";

    public S3BucketDTO() {
    }

    public S3BucketDTO(String nameOfNode) {
        this.setName(nameOfNode);
    }

    private String getBucketName() {
        return bucketName;
    }

    private void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public Node createNode() {
        S3Bucket node = new S3Bucket();
        node.setName(getName());
        node.setBucketName(getBucketName());
        return node;
    }

}
