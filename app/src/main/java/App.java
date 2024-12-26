import com.recipeapp.datahandler.CSVDataHandler;
import com.recipeapp.datahandler.DataHandler;
import com.recipeapp.datahandler.JSONDataHandler;
import com.recipeapp.ui.RecipeUI;
import java.io.*;

public class App {

    public static void main(String[] args) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Choose the file format:");
            System.out.println("1. CSV");
            System.out.println("2. JSON");
            System.out.print("Select (1/2): ");
            String choice = reader.readLine();
            if(choice.equals("2")){
                DataHandler json = new JSONDataHandler();
                RecipeUI ui = new RecipeUI(json);
                ui.displayMenu();
            }else{
                DataHandler csv = new CSVDataHandler();
                RecipeUI ui = new RecipeUI(csv);
                ui.displayMenu();
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}