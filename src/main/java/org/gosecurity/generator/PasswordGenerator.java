package org.gosecurity.generator;

import com.google.common.hash.Hashing;
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

public class PasswordGenerator {
    private List<Agent> listAgents;
    private String websitePath;
    private String basePath;

    public PasswordGenerator(List<Agent> listAgents, String websitePath, String basePath){
        this.listAgents = listAgents;
        this.websitePath = websitePath;
        this.basePath = basePath;
    }

    public File generateAgentPassword(){
        File indexFile = null;
        try {
            //Création du fichier htaccess
            Path sourceJPGFile = Paths.get(basePath + ".htaccess");
            Path copiedJPGFile = Paths.get(this.websitePath + "/.htaccess");
            Files.copy(sourceJPGFile, copiedJPGFile, StandardCopyOption.REPLACE_EXISTING);

            indexFile = new File(this.websitePath + "/.htpasswd");

            String content = this.createAgentPasswordLine(this.listAgents);

            indexFile.createNewFile();
            Files.write(indexFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
            System.out.println("File created");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return indexFile;
    }

    public String createAgentPasswordLine(List<Agent> listAgents){
        String content = "";
            for (Agent agent:
                    listAgents) {
                content += agent.getId() + ":" + agent.getPassword() + "\n";
            }
        return content;
    }

}
