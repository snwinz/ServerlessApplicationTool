package com.serverless.demo.function;



import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class InformClient implements RequestHandler<PayloadOfFilePath, String> {

	private Regions REGION = Regions.EU_CENTRAL_1;

	@Override
	public String handleRequest(PayloadOfFilePath input, Context context) {
		DynamoDB dynamoDb = new DynamoDB(REGION);
		Table hashTable = dynamoDb.getTable("hashes");
		Item item = hashTable.getItem("fileName", input.getPath());
		System.out.println("Information for client:" + item.toJSON());
		// Client can be informed here; e.g. via mail, SNS etc.
		return "Hello from ClientInformer!";
	}

}
