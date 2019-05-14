package model.structure;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class S3Bucket extends Node {

    private String bucketName = "";

    public S3Bucket() {
    }

    public S3Bucket(String nameOfNode) {
        this.setName(nameOfNode);
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public String toString() {
        String result = super.toString();
        result += "Bucket name: " + this.bucketName + System.lineSeparator();
        return result;
    }


}
