package com.serverless.demo.function;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class HashFiles implements RequestHandler<PayloadOfFilePath, String> {

	private Regions REGION = Regions.EU_CENTRAL_1;

	@Override
	public String handleRequest(PayloadOfFilePath input, Context context) {
		String fileName = input.getPath();
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
		String srcBucket = "my-object-storage-for-the-paper";
		S3Object s3ObjectOfOriginalFile = s3Client.getObject(new GetObjectRequest(srcBucket, fileName));
		S3Object s3ObjectOfCompressedFile = s3Client.getObject(new GetObjectRequest(srcBucket, fileName + ".zip"));

		String md5sumOriginal = "";
		try (InputStream inputStream = s3ObjectOfOriginalFile.getObjectContent()) {

			md5sumOriginal = DigestUtils.md5Hex(inputStream);

		} catch (IOException e1) {
			md5sumOriginal = "could not be calculated";
			e1.printStackTrace();
		}

		String md5sumCompressed = "";
		try (InputStream inputStream = s3ObjectOfCompressedFile.getObjectContent()) {

			md5sumCompressed = DigestUtils.md5Hex(inputStream);

		} catch (IOException e1) {
			md5sumCompressed = "could not be calculated";
			e1.printStackTrace();
		}

		// put hash to DynamoDB

		DynamoDB dynamoDb = new DynamoDB(REGION);
		Table hashTable = dynamoDb.getTable("hashes");
		Item newValue = new Item().with("fileName", fileName).with("md5sumOriginalFile", md5sumOriginal)
				.with("md5sumCompressed", md5sumCompressed);
		hashTable.putItem(newValue);

		return "Hello from Hasher!";
	}

}
