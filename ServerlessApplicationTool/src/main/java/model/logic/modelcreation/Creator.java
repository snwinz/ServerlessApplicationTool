package model.logic.modelcreation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.structure.*;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

public class Creator {

    public static void main(String[] args) {
        String pathOfResourceFile = "../ExampleForPaper/serverless.template";
        String[] pathesToSourceCodeOfFunctions = {
                "../ExampleForPaperAdapted/src/main/java/com/serverless/demo/function/HashFiles.java",
                "../ExampleForPaperAdapted/src/main/java/com/serverless/demo/function/InformClient.java",
                "../ExampleForPaperAdapted/src/main/java/com/serverless/demo/function/ProcessS3Zip.java"};
        String[] pathsToLogFile = {"logData/ProcessS3ZipFunction"};
        Graph graph = new Graph();
        createBasicModel(pathOfResourceFile, graph);

        for (String code : pathesToSourceCodeOfFunctions) {
            addRelationsFromSource(code, graph);
        }
        // reduceModel(graph);
        addLogData(pathsToLogFile, graph);

        System.out.println("Graph:" + System.lineSeparator());
        graph.print();

    }

    private static Graph createGraph(String pathOfResourceFile, String[] pathesToSourceCodeOfFunctions,
                                     String[] pathsToLogFile) {
        Graph graph = new Graph();
        createBasicModel(pathOfResourceFile, graph);

        for (String code : pathesToSourceCodeOfFunctions) {
            addRelationsFromSource(code, graph);
        }
        // reduceMode(graph);
        addLogData(pathsToLogFile, graph);

        System.out.println("Graph:" + System.lineSeparator());
        graph.print();
        return graph;
    }

    public static Graph createGraph(String pathOfResourceFile, String directoryToSourceCodeOfFunctions,
                                    String directoryToLogFile) {
        List<String> pathsToFunctions = new ArrayList<>();
        List<String> pathsToLogs = new ArrayList<>();
        try (DirectoryStream<Path> directoryStreamFunctions = Files
                .newDirectoryStream(Paths.get(directoryToSourceCodeOfFunctions));
             DirectoryStream<Path> directoryStreamLogs = Files
                     .newDirectoryStream(Paths.get(directoryToSourceCodeOfFunctions))
        ) {
            for (Path path : directoryStreamFunctions) {
                pathsToFunctions.add(path.toString());
            }
            for (Path path : directoryStreamLogs) {
                pathsToLogs.add(path.toString());
            }
        } catch (IOException ex) {
            System.err.println("Graph could not be created");
        }

        String[] functionsPathsArray = pathsToFunctions.toArray(new String[pathsToFunctions.size()]);
        String[] logPathsArray = pathsToLogs.toArray(new String[pathsToLogs.size()]);

        return createGraph(pathOfResourceFile, functionsPathsArray, logPathsArray);
    }

    private static void addLogData(String[] logFiles, Graph graph) {

        for (String fileName : logFiles) {
            Path path = Paths.get(fileName);
            System.out.println("Here: " + path.getFileName());
            Function function = graph.getFunctionByName(path.getFileName().toString());
            if (function != null) {
                try {
                    List<String> allLogs = Files.readAllLines(path);
                    function.addLog(allLogs);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
    /*
    private static void reduceModel(Graph graph) {
        List<Node> nodes = new LinkedList<>();
        for (Node node : graph.getNodes()) {

            if (node instanceof Function) {
                List<Arrow> outgoingArrow = node.getOutgoingArrows();
                List<Arrow> updatedOutgoingArrows = new LinkedList<>();

                for (Arrow arrow : outgoingArrow) {
                    Node successor = arrow.getSuccessor();
                    if (successor instanceof Function) {
                        Arrow updatedArrow = new Arrow(node, successor);
                        updatedOutgoingArrows.add(updatedArrow);
                    }
                }
                node.setOutgoingArrows(updatedOutgoingArrows);
                nodes.add(node);
            }
            graph.setNodes(nodes);
        }

    }
	*/
    
    private static void createBasicModel(String filePathStackFile, Graph graph) {
        Path path = Paths.get(filePathStackFile);
        if (Files.exists(path)) {
            try {

                byte[] jsonData = Files.readAllBytes(path);

                // create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                // read JSON like DOM Parser
                JsonNode rootNode = objectMapper.readTree(jsonData);
                JsonNode descriptionNode = rootNode.path("Description");
                System.out.println("Description = " + descriptionNode.toString());

                JsonNode resourcesNode = rootNode.path("Resources");
                Iterator<Entry<String, JsonNode>> resources = resourcesNode.fields();

                // Create nodes first
                while (resources.hasNext()) {
                    Entry<String, JsonNode> resource = resources.next();
                    String nameOfNode = resource.getKey();
                    JsonNode resourceNode = resource.getValue();
                    JsonNode type = resourceNode.path("Type");
                    if (isServerlessFunction(type)) {
                        JsonNode properties = resourceNode.path("Properties");
                        Function lambda = new Function(nameOfNode);
                        setFunctionName(lambda, properties);
                        setFunctionHandler(lambda, properties);
                        setFunctionRuntime(lambda, properties);
                        setFunctionPolicies(lambda, properties);
                        graph.addNode(lambda);
                    } else if (isS3Instance(type)) {
                        S3Bucket s3 = new S3Bucket(nameOfNode);
                        JsonNode properties = resourceNode.path("Properties");
                        setBucketName(s3, properties);
                        graph.addNode(s3);
                    } else if (isDynamoDB(type)) {
                        DynamoDB dynamo = new DynamoDB(nameOfNode);
                        JsonNode properties = resourceNode.path("Properties");
                        setDynamoName(dynamo, properties);
                        graph.addNode(dynamo);
                    }
                }

                // Check for relations (e.g. triggers) saved in file)
                resources = resourcesNode.fields();
                while (resources.hasNext()) {
                    Entry<String, JsonNode> resource = resources.next();
                    String nameOfNode = resource.getKey();
                    JsonNode resourceNode = resource.getValue();
                    JsonNode type = resourceNode.path("Type");
                    if (isServerlessFunction(type)) {
                        JsonNode properties = resourceNode.path("Properties");
                        if (properties.has("Events")) {
                            JsonNode eventNode = properties.path("Events");
                            Iterator<JsonNode> eventIterator = eventNode.elements();
                            while (eventIterator.hasNext()) {
                                JsonNode event = eventIterator.next();
                                JsonNode typeNode = event.path("Type");
                                if (typeNode.asText().contains("S3")) {
                                    JsonNode propertiesOfEvent = event.path("Properties");
                                    JsonNode bucketOfEvent = propertiesOfEvent.path("Bucket");
                                    JsonNode triggerDB = bucketOfEvent.path("Ref");
                                    String nameOfTriggerDB = triggerDB.asText();
                                    Node dbNode = graph.getNodeByName(nameOfTriggerDB);
                                    Node triggeredNode = graph.getNodeByName(nameOfNode);
                                    Arrow arrow = new Arrow(dbNode, triggeredNode);
                                    dbNode.addOutgoingArrow(arrow);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("File could not be read");
                e.printStackTrace();
            }
        }
    }

    private static void setDynamoName(DynamoDB dynamo, JsonNode properties) {
        JsonNode node = properties.path("TableName");
        dynamo.setTableName(node.toString().replaceAll("\"", "").trim());
    }

    private static void setBucketName(S3Bucket s3, JsonNode properties) {
        JsonNode node = properties.path("BucketName");
        s3.setBucketName(node.toString().replaceAll("\"", "").trim());
    }

    private static void setFunctionPolicies(Function lambda, JsonNode properties) {
        JsonNode node = properties.path("Policies");
        lambda.setPolicies(node.toString());

    }

    private static void setFunctionRuntime(Function lambda, JsonNode properties) {
        JsonNode node = properties.path("Runtime");
        lambda.setRuntime(node.toString().replaceAll("\"", "").trim());
    }

    private static void setFunctionHandler(Function lambda, JsonNode properties) {
        JsonNode node = properties.path("Handler");
        lambda.setHandler(node.toString().replaceAll("\"", "").trim());
    }

    private static void setFunctionName(Function lambda, JsonNode properties) {
        JsonNode node = properties.path("FunctionName");
        lambda.setFunctionName(node.toString().replaceAll("\"", "").trim());
    }

    private static boolean isDynamoDB(JsonNode type) {
        return type.toString().contains("AWS::DynamoDB::Table");
    }

    private static boolean isS3Instance(JsonNode type) {
        return type.toString().contains("AWS::S3::Bucket");
    }

    private static boolean isServerlessFunction(JsonNode type) {
        return type.toString().contains("AWS::Serverless::Function");
    }

    private static void addRelationsFromSource(String filePath, Graph graph) {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            List<String> allLines = new ArrayList<>();
            try {
                allLines = Files.readAllLines(path);
            } catch (IOException e) {
                System.err.println("Source code file could not be reade");
                e.printStackTrace();
            }
            String handlerName = getHandlerName(allLines);
            lookForRelations(allLines, graph, handlerName);
        }
    }

    private static void lookForRelations(List<String> allLines, Graph graph, String handlerName) {

        Function predecessor = graph.getFunctionByHandler(handlerName);

        List<Arrow> arrows = new LinkedList<>();
        HashMap<String, String> variables = new HashMap<>();
        int order = 1;
        int depthOfCondition = 0;
        for (String line : allLines) {
            checkForVariables(variables, line);
            // S3 read
            if (line.contains("GetObjectRequest(")) {
                String parameter = line.substring(line.lastIndexOf("GetObjectRequest(") + "GetObjectRequest(".length());
                String dbName = parameter.split(",")[0].trim();
                if (variables.containsKey(dbName)) {
                    dbName = variables.get(dbName);
                }
                S3Bucket successor = graph.getS3ByName(dbName);
                Arrow arrowTmp = new Arrow(predecessor, successor);
                arrowTmp.setAccessMode(DBAccessMode.READ);
                if (depthOfCondition == 0) {
                    arrowTmp.setCondition("");
                }
                arrowTmp.setOrder(order);
                if (arrows.size() > 0) {
                    Arrow lastArrow = arrows.get(arrows.size() - 1);
                    if (lastArrow.isUnequal(arrowTmp)) {

                        if ((lastArrow.getAccessMode() == DBAccessMode.WRITE
                                || lastArrow.getAccessMode() == DBAccessMode.READWRITE)
                                && lastArrow.getSuccessor() == successor) {
                            lastArrow.setAccessMode(DBAccessMode.READWRITE);
                        } else {
                            arrows.add(arrowTmp);
                            order++;
                            predecessor.addOutgoingArrow(arrowTmp);
                        }
                    }
                } else {
                    arrows.add(arrowTmp);
                    order++;
                    predecessor.addOutgoingArrow(arrowTmp);

                }
            }
            // s3 write
            if (line.contains("putObject(")) {
                String parameter = line.substring(line.lastIndexOf("putObject(") + "putObject(".length());
                String dbName = parameter.split(",")[0].trim();
                if (variables.containsKey(dbName)) {
                    dbName = variables.get(dbName);
                }
                S3Bucket successor = graph.getS3ByName(dbName);
                Arrow arrowTmp = new Arrow(predecessor, successor);
                arrowTmp.setAccessMode(DBAccessMode.WRITE);
                if (depthOfCondition == 0) {
                    arrowTmp.setCondition("");
                }
                arrowTmp.setOrder(order);

                if (arrows.size() > 0) {
                    Arrow lastArrow = arrows.get(arrows.size() - 1);
                    if (lastArrow.isUnequal(arrowTmp)) {
                        if ((lastArrow.getAccessMode() == DBAccessMode.READ
                                || lastArrow.getAccessMode() == DBAccessMode.READWRITE)
                                && lastArrow.getSuccessor() == successor) {
                            lastArrow.setAccessMode(DBAccessMode.READWRITE);
                        } else {
                            arrows.add(arrowTmp);
                            order++;
                            predecessor.addOutgoingArrow(arrowTmp);
                        }
                    }

                } else {
                    arrows.add(arrowTmp);
                    order++;
                    predecessor.addOutgoingArrow(arrowTmp);

                }
            }
            // dynamo write
            if (line.contains("putItem(")) {
                String dynamoDBTable = line.split("\\.")[0].trim();
                String valueOfDynamo = variables.get(dynamoDBTable);
                String dbname = valueOfDynamo.split("\\(")[1].replaceAll("\"", "").replaceAll(";", "")
                        .replaceAll("\\)", "").trim();
                DynamoDB successor = graph.getDynamoDBbyName(dbname);
                Arrow arrowTmp = new Arrow(predecessor, successor);
                arrowTmp.setAccessMode(DBAccessMode.WRITE);
                if (depthOfCondition == 0) {
                    arrowTmp.setCondition("");
                }
                arrowTmp.setOrder(order);

                if (arrows.size() > 0) {
                    Arrow lastArrow = arrows.get(arrows.size() - 1);
                    if (lastArrow.isUnequal(arrowTmp)) {
                        arrows.add(arrowTmp);
                        order++;
                        predecessor.addOutgoingArrow(arrowTmp);
                    }
                } else {
                    arrows.add(arrowTmp);
                    order++;
                    predecessor.addOutgoingArrow(arrowTmp);

                }

            }

            // dynamo read
            if (line.contains("getItem(")) {
                String dynamoDBTable = line.split("\\.")[0].split("=")[1].trim();
                String varibleNameOfUsedObject = variables.get(dynamoDBTable);
                String dbname = varibleNameOfUsedObject.split("\\(")[1].replaceAll("\"", "").replaceAll(";", "")
                        .replaceAll("\\)", "").trim();
                DynamoDB successor = graph.getDynamoDBbyName(dbname);
                Arrow arrowTmp = new Arrow(predecessor, successor);
                arrowTmp.setAccessMode(DBAccessMode.READ);
                if (depthOfCondition == 0) {
                    arrowTmp.setCondition("");
                }
                arrowTmp.setOrder(order);

                if (arrows.size() > 0) {
                    Arrow lastArrow = arrows.get(arrows.size() - 1);
                    if (lastArrow.isUnequal(arrowTmp)) {
                        arrows.add(arrowTmp);
                        order++;
                        predecessor.addOutgoingArrow(arrowTmp);
                    }
                } else {
                    arrows.add(arrowTmp);
                    order++;
                    predecessor.addOutgoingArrow(arrowTmp);
                }
            }

            // lambda invocation
            if (line.contains("invoke(")) {
                String parameter = line.substring(line.lastIndexOf("invoke(") + "invoke(".length());
                String lambdaRequestName = parameter.split(",")[0].replaceAll("\\)", "").replaceAll(";", "").trim();
                if (variables.containsKey(lambdaRequestName)) {
                    lambdaRequestName = variables.get(lambdaRequestName);
                }
                String functionName = lambdaRequestName
                        .substring(lambdaRequestName.lastIndexOf("withFunctionName(") + "withFunctionName(".length())
                        .split("\\)")[0];
                Function successor = graph.getFunctionByName(functionName);
                Arrow arrowTmp = new Arrow(predecessor, successor);
                if (depthOfCondition == 0) {
                    arrowTmp.setCondition("");
                }
                arrowTmp.setOrder(order);
                if (lambdaRequestName.contains("RequestResponse")) {
                    arrowTmp.setSynchronizedCall(true);
                }
                if (arrows.size() > 0) {
                    Arrow lastArrow = arrows.get(arrows.size() - 1);
                    if (lastArrow.isUnequal(arrowTmp)) {
                        arrows.add(arrowTmp);
                        order++;
                        predecessor.addOutgoingArrow(arrowTmp);
                    }
                } else {
                    arrows.add(arrowTmp);
                    order++;
                    predecessor.addOutgoingArrow(arrowTmp);

                }
            }

        }
    }

    private static void checkForVariables(HashMap<String, String> variables, String line) {
        if (line.contains("=")) {
            String[] splittedLine = line.split("=");
            String[] splittedLeftPart = splittedLine[0].split(" ");

            if (splittedLeftPart.length > 0) {
                String key;
                if (splittedLeftPart.length == 1) {
                    key = splittedLeftPart[0].replaceAll(" ", "").trim();
                } else {
                    key = splittedLeftPart[splittedLeftPart.length - 1].replaceAll(" ", "").trim();
                }

                String value = splittedLine[1].replaceAll(";", "").replaceAll(" ", "").replaceAll("\"", "").trim();
                addKeyWithValue(variables, key, value);
            }
        }
    }

    private static void addKeyWithValue(HashMap<String, String> variables, String key, String value) {
        variables.put(key, variables.getOrDefault(value, value));
    }

    private static String getHandlerName(List<String> allLines) {
        for (String line : allLines) {
            if (line.contains("public class")) {
                return line.split(" ")[2];
            }
        }
        return "";
    }


}
