package com.recipeapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.recipeapp.datahandler.DataHandler;
import com.recipeapp.model.Ingredient;
import com.recipeapp.model.Recipe;

public class RecipeUI {
    private BufferedReader reader;
    private DataHandler dataHandler;

    public RecipeUI(DataHandler dataHandler) {
        reader = new BufferedReader(new InputStreamReader(System.in));
        this.dataHandler = dataHandler;
    }
    
    public void displayMenu() {

        System.out.println("Current mode: " + dataHandler.getMode());

        while (true) {
            try {
                System.out.println();
                System.out.println("Main Menu:");
                System.out.println("1: Display Recipes");
                System.out.println("2: Add New Recipe");
                System.out.println("3: Search Recipe");
                System.out.println("4: Exit Application");
                System.out.print("Please choose an option: ");

                String choice = reader.readLine();

                switch (choice) {
                    case "1":
                        try {
                            //レシピデータ呼び出し
                            ArrayList<Recipe> recipes = dataHandler.readData();

                            //レシピデータがない場合定型メッセージを返しbreak
                            if(recipes.size() == 0){
                                System.out.println("No recipes available.");
                                break;
                            }


                            //出力
                            System.out.println("Recipes:");
                            recipes.forEach(r -> {
                                //材料オブジェクトを名前ごとに配列に成形
                                List<String> ingredientsList = new ArrayList<>();
                                for(int i = 0; i < r.getIngredients().size(); i++){
                                    ingredientsList.add(r.getIngredients().get(i).getName());
                                }
                                //配列を出力用文字列に成形
                                String ingredients = String.join(", ", ingredientsList);
                                //出力
                                System.out.println("-----------------------------------");
                                System.out.println("Recipe Name: " + r.getName());
                                System.out.println("Main Ingredients: " + ingredients);
                            });
                            //最終行〆用
                            System.out.println("-----------------------------------");

                        } catch (IOException e) {
                            System.out.println("Error reading file: 例外のメッセージ");
                        }
                        break;
                    case "2":
                        addNewRecipe();
                        break;
                    case "3":
                        searchRecipe();
                        break;
                    case "4":
                        System.out.println("Exiting the application.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select again.");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Error reading input from user: " + e.getMessage());
            }
        }
    }

    private void addNewRecipe() throws IOException {
        //変数の宣言
        reader = new BufferedReader(new InputStreamReader(System.in));
        String line;

        //アナウンス
        System.out.println("Adding a new recipe.");

        //名前入力
        System.out.print("Enter recipe name: ");
        String name = reader.readLine();

        //材料入力
        System.out.println("Enter ingredients (type 'done' when finished):");
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        while (!(line = reader.readLine()).equals("done")) {
            Ingredient ingredient = new Ingredient(line);
            ingredients.add(ingredient);
        }

        //データ成形
        Recipe recipe = new Recipe(name, ingredients);

        //データ書き込み
        dataHandler.writeData(recipe);

        //完了アナウンス
        System.out.println("Recipe added successfully.");
    }

    private void searchRecipe() throws IOException {
        //変数の宣言
        reader = new BufferedReader(new InputStreamReader(System.in));

        //クエリ入力
        System.out.print("Enter search query (e.g., 'name=Tomato&ingredient=Garlic'): ");
        String query = reader.readLine();

        //検索
        ArrayList<Recipe> resultList = dataHandler.searchData(query);


        //※結果出力

        //結果がなかった場合定型メッセージを出力しreturn
        if(resultList.size() == 0){
            System.out.println("No matching recipes found.");
            return;
        }
        
        //アナウンス
        System.out.println("Matching Recipes:");

        //要素毎にforEach
        resultList.forEach(r -> {
            //材料オブジェクトを名前ごとに配列に成形
            List<String> ingredientsList = new ArrayList<>();
            for(int i = 0; i < r.getIngredients().size(); i++){
                ingredientsList.add(r.getIngredients().get(i).getName());
            }
            //配列を出力用文字列に成形
            String ingredients = String.join(", ", ingredientsList);

            //出力
            System.out.println("Name: " + r.getName());
            System.out.println("Ingredients: " + ingredients);
        });
    }
}
