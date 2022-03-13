package org.gosecurity.generator;

import org.gosecurity.src.model.Agent;
import org.gosecurity.src.model.Tool;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class IndexGenerator {
    private List<Agent> listAgents;
    private List<Tool> listTools;
    private String websitePath;

    public IndexGenerator(List<Agent> listAgents, List<Tool> listTools, String websitePath){
        this.listAgents = listAgents;
        this.listTools = listTools;
        this.websitePath = websitePath;
    }

    private String setValue(String content, String varName, String value){
        return content.replaceAll("\\$\\{" + varName +"?}", value);
    }

    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public File generateIndex(){
        File indexFile = null;
        try {
            indexFile = new File(this.websitePath + "/index.html");

            InputStream is = this.getFileFromResourceAsStream("template/indexMainGenerator.html");
            StringBuilder sb = new StringBuilder();
            for (int ch; (ch = is.read()) != -1; ) {
                sb.append((char) ch);
            }

            String content = sb.toString();
            content = this.setValue(content, "agentNumber", String.valueOf(listAgents.size()));

            content = this.setValue(content, "agentTable", this.createAgentRowDataTable(listAgents));
            content = this.setValue(content, "materielTable", this.createMaterielRowDataTable(listTools));

            indexFile.createNewFile();
            Files.write(indexFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
            System.out.println("File created");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return indexFile;
    }

    public String createAgentRowDataTable(List<Agent> listAgents){
        String dataTableRowHTML = "";
        try {
            InputStream is = this.getFileFromResourceAsStream("template/indexAgentTableGenerator.html");
            StringBuilder sb = new StringBuilder();
            for (int ch; (ch = is.read()) != -1; ) {
                sb.append((char) ch);
            }
            for (Agent agent:
                    listAgents) {
                String content = sb.toString();
                content = this.setValue(content, "agentLastname", agent.getLastname());
                content = this.setValue(content, "agentFirstname", agent.getFirstname());
                content = this.setValue(content, "agentPageId", agent.getId());

                dataTableRowHTML = dataTableRowHTML + content;
            }

        } catch (java.io.IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return dataTableRowHTML;
    }

    public String createMaterielRowDataTable(List<Tool> listTools){
        String dataTableRowHTML = "";
        try {
            InputStream is = this.getFileFromResourceAsStream("template/indexMaterielTableGenerator.html");
            StringBuilder sb = new StringBuilder();
            for (int ch; (ch = is.read()) != -1; ) {
                sb.append((char) ch);
            }

            for (Tool tool:
                    listTools) {
                String content = sb.toString();
                content = this.setValue(content, "materielName", tool.getLabel());

                dataTableRowHTML = dataTableRowHTML + content;
            }

        } catch (java.io.IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return dataTableRowHTML;
    }
}
