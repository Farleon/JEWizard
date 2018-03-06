import java.io.InputStream;
import com.arangodb.ArangoDB;


public class Main {
    
    public static void main(String[] args){
        
        InputStream in = Main.class.getResourceAsStream("arangodb.properties");
        ArangoDB arangoDB = new ArangoDB.Builder().loadProperties(in).build();
        
    }
}
