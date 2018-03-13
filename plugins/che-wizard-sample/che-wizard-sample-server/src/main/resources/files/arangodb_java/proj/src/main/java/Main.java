import java.io.InputStream;
import com.arangodb.ArangoDB;


public class Main {
    
    public static void main(String[] args){
        
        InputStream in = Main.class.getResourceAsStream("arangodb.properties");
        ArangoDB arangoDB = new ArangoDB.Builder().loadProperties(in).build();
        
        //Right click on the 'proj' folder and click on convert project. Convert the project to maven
        
        
        //your code here
        
        
        
        /*
         * Run command
         * cd ${current.project.path}
            mvn package -B
            cd target
            java -jar $(ls|grep *-dependencies.jar)
         */
    }
}
