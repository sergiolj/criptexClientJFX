package shared.config;

import com.sun.security.auth.login.ConfigFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Classe que define a configuração do servidor RMI.
 * Essas configurações serão usadas para determinar os parâmetros de inicialização do servidor e do cliente, mantendo o
 * arquivo de execução do serviço e do cliente desacoplados habilitando os mesmos para serem utilizados em outros
 * projetos com mínimas modificações.
 *
 * @author Bruna Brito Muniz Filgueiras
 * @author Laís de Assis Doria da Silva
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
public class Config {

    private static Config instance;

    private final String serverName = "CRIPTEX_RMI";
    private Integer serverPort;
    private String ipAddress;

    private final File configFile = new File("conf.cfg");
    private long lastModifiedTime = 0;

    private Config() throws IOException{
        if(!configFile.exists()){
            saveConfigToDisk("127.0.0.1", 1099);
        }
            updateData();
    }

    private void saveConfigToDisk(String ipAddress, int serverPort) throws IOException {
        String server = ipAddress + ";" + serverPort;
        Files.writeString(configFile.toPath(), server,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void checkFileChanges() {
        if(configFile.exists() && configFile.lastModified() > this.lastModifiedTime){
            try{
                updateData();
            }catch (IOException e){
                System.out.println("Erro ao tentar recarregar arquivo modificado.");
            }
        }
    }

    public static synchronized Config getInstance(){
        if(instance == null){
            try {
                instance = new Config();
            } catch (IOException e) {
                System.out.println("Erro ao ler arquivo" + e.getMessage());
            }
        }else{
            instance.checkFileChanges();
        }
        return instance;
    }

    public String getServerName() {
        return serverName;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void updateData() throws IOException {
        if(!configFile.exists()) {
            throw new FileNotFoundException("Arquivo conf.cfg não encontrado no disco.");
        }
            String fileContent = Files.readString(configFile.toPath());

            String[] server = fileContent.split(";");
            if (server.length == 2) {
                String IP = server[0].trim();
                int port = Integer.parseInt(server[1].trim());

                ipAddress = IP;
                serverPort = port;
                this.lastModifiedTime = configFile.lastModified();
            }else{
                throw new IOException("Formato de arquivo inválido." + fileContent);
            }
    }
}
