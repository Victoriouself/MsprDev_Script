package org.gosecurity.generator;

import org.gosecurity.src.model.Agent;
import org.gosecurity.src.model.Tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class AgentGenerator {
    private Agent agent;
    private String websitePath;
    private String basePath;

    public AgentGenerator(Agent agent, String websitePath, String basePath){
        this.agent = agent;
        this.websitePath = websitePath;
        this.basePath = basePath;
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

    public File generateAgent(){
        File indexFile = null;
        try {
            indexFile = new File(this.websitePath + "/" + this.agent.getId() + ".html");

            InputStream is = this.getFileFromResourceAsStream("template/agentMainGenerator.html");
            StringBuilder sb = new StringBuilder();
            for (int ch; (ch = is.read()) != -1; ) {
                sb.append((char) ch);
            }

            String content = sb.toString();
            content = this.setValue(content, "agentLastname", agent.getLastname());
            content = this.setValue(content, "agentFirstname", agent.getFirstname());

            content = this.setValue(content, "agentMission", agent.getMission());
            content = this.setValue(content, "materielTable", this.createMaterielRowDataTable(agent.getTools()));

            indexFile.createNewFile();
            Path sourceJPGFile = Paths.get(basePath + this.agent.getId() + ".jpg");
            Path copiedJPGFile = Paths.get(this.websitePath + "/img/" + this.agent.getId() + ".jpg");
            Files.copy(sourceJPGFile, copiedJPGFile, StandardCopyOption.REPLACE_EXISTING);
            Files.write(indexFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
            System.out.println("File create");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return indexFile;
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

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return dataTableRowHTML;
    }
}
