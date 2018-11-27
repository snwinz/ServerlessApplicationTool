package com.serverless.demo.function;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.amazonaws.services.lambda.AWSLambdaAsync;
import com.amazonaws.services.lambda.AWSLambdaAsyncClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class ProcessS3Zip implements RequestHandler<S3Event, String> {

	public String handleRequest(S3Event s3event, Context context) {
		String localFilePath = "/tmp/tmpFile";
		try {
			S3EventNotificationRecord record = s3event.getRecords().get(0);
			String srcBucket = "my-object-storage-for-the-paper";
			String srcKey = record.getS3().getObject().getKey().replace('+', ' ');
			srcKey = URLDecoder.decode(srcKey, "UTF-8");

			String dstBucket = srcBucket;
			String dstKey = srcKey + ".zip";

			AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
			S3Object s3Object = s3Client.getObject(new GetObjectRequest(srcBucket, srcKey));

			File targetFile = new File(localFilePath);
			try (OutputStream outStream = new FileOutputStream(targetFile);
					InputStream dis = s3Object.getObjectContent()) {
				byte[] buffer = new byte[1024];

				int length = 0;
				System.out.println("Read");
				while ((length = dis.read(buffer)) >= 0F) {
					outStream.write(buffer, 0, length);
				}
			}
			String zippedFilePath = "/tmp/compressed.zip";
			File zippedFile1 = new File(zippedFilePath);
			// zippen
			FileOutputStream fos = new FileOutputStream(zippedFile1);
			ZipOutputStream zipOut = new ZipOutputStream(fos);

			File fileToZip = new File(localFilePath);
			InputStream fis = new FileInputStream(fileToZip);
			ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
			zipOut.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			zipOut.close();
			fis.close();
			fos.close();

			File zippedFile = zippedFile1;

			s3Client.putObject(dstBucket, dstKey, zippedFile);
			System.out.println("Successfully transferred file " + srcBucket + "/" + srcKey + " and uploaded to "
					+ dstBucket + "/" + dstKey);

			AWSLambdaAsync lambda = AWSLambdaAsyncClientBuilder.defaultClient();
			String fileNameInJSON = "{\"path\":\"" + srcKey + "\"}";
			System.out.println(fileNameInJSON);
			InvokeRequest hashRequest = new InvokeRequest().withFunctionName("HashFiles").withInvocationType("RequestResponse").withPayload(ByteBuffer.wrap(fileNameInJSON.getBytes()));
			lambda.invoke(hashRequest);
			InvokeRequest informationRequest = new InvokeRequest().withFunctionName("InformClient").withInvocationType("Event").withPayload(ByteBuffer.wrap(fileNameInJSON.getBytes()));
			lambda.invoke(informationRequest);
			return "Ok";
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
