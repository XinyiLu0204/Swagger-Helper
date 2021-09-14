package com.chunmiao.jsontotxt;

import com.chunmiao.jsontotxt.entity.Root;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.*;

@SpringBootApplication
public class JsonToTxtApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(JsonToTxtApplication.class, args);
        FileInputStream in = new FileInputStream("1.txt");
        JsonNode jsonNode = new ObjectMapper().readTree(in);

        List<Root> rootList = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> node = jsonNode.findValue("paths").fields();
        while (node.hasNext()){
            Map.Entry<String, JsonNode> jsonNodeEntry = node.next();
            String api = jsonNodeEntry.getKey();
            JsonNode nodeValue = jsonNodeEntry.getValue();
            Iterator<String> methodsName = nodeValue.fieldNames();
            while (methodsName.hasNext()) {
                String method = methodsName.next();
                JsonNode methods = nodeValue.findValue(method);
                String name = methods.findValue("tags").get(0).asText();
                String description = methods.findValue("description").asText();
                Root root = new Root(name, method, api, description);
                rootList.add(root);
            }
        }
        File file = new File("result.csv");
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "GBK"));
        for (Root root : rootList) {
            bufferedWriter.write(root.getMethod().toUpperCase(Locale.ROOT) + " " +
                    root.getApi());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        System.out.println("Convert Done");
    }
}
