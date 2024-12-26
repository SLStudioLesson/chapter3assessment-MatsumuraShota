package com.recipeapp.datahandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.recipeapp.model.Ingredient;
import com.recipeapp.model.Recipe;

public class CSVDataHandler implements DataHandler{
    private String filePath;

    public CSVDataHandler(){
        this.filePath = "app/src/main/resources/recipes.csv";
    }

    public CSVDataHandler(String filePath){
        this.filePath = filePath;
    }


    @Override
    public String getMode(){
        return "CSV";
    }

    @Override
    public ArrayList<Recipe> readData() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            //変数の宣言
            ArrayList<Recipe> recipeList = new ArrayList<>();
            String line;

            //ファイル1行ずつループ呼び出し
            while ((line = reader.readLine()) != null) {
                //読み取りデータsplit格納
                List<String> value = new ArrayList<>(Arrays.asList(line.split(",")));

                //Ingredient代入用成形
                ArrayList<Ingredient> ingredientList = new ArrayList<>();
                for(int i = 0; i < value.size(); i++){
                    if(i != 0){
                        Ingredient ingredient = new Ingredient(value.get(i));
                        ingredientList.add(ingredient);
                    }
                }

                //代入
                Recipe recipe = new Recipe(
                    value.get(0),
                    ingredientList);

                //返却
                recipeList.add(recipe);
            }

            return recipeList;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void writeData(Recipe recipe) throws IOException {
        //変数の宣言
        List<Recipe> recipeList = readData();
        List<String> textList = new ArrayList<>();

        //引数のレシピを配列に格納
        recipeList.add(recipe);

        //ファイルに書き込み
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            //
            recipeList.forEach(r -> {
                //材料オブジェクトを名前ごとに配列に成形
                List<String> ingredientsList = new ArrayList<>();
                for(int i = 0; i < r.getIngredients().size(); i++){
                    ingredientsList.add(r.getIngredients().get(i).getName());
                }
                //配列を出力用文字列に成形
                String ingredients = String.join(",  ", ingredientsList);

                //文字列に成形
                textList.add(r.getName() + ",  " + ingredients);
            });

            //出力用に成形\nでjoin
            String text = String.join("\n", textList);
            
            //書き込み
            writer.write(text);
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public ArrayList<Recipe> searchData(String keyword) throws IOException {
        //変数の宣言
        ArrayList<String> query = new ArrayList<>(Arrays.asList(keyword.split("&")));
        ArrayList<Recipe> recipeList = readData();
        ArrayList<Recipe> resultList = new ArrayList<>();

        //レシピ毎にforEach
        recipeList.forEach(r -> {
            //結果保持用
            List<Boolean> flg = new ArrayList<>();

            //クエリ毎にforEach
            query.forEach(q -> {
                //クエリ内容でswitch
                switch (q.split("=")[0]) {
                    case "name":
                        if(r.getName().contains(q.split("=")[1])){
                            flg.add(true);
                            break;
                        }
                        flg.add(false);
                        break;
                    case "ingredient":
                        Boolean ingFlg = false;
                        List<String> ingredientList = new ArrayList<>();
                        r.getIngredients().forEach(i -> {
                            ingredientList.add(i.getName());
                        });
                        ingredientList.forEach(i -> {
                            if(i.contains(q.split("=")[1])){
                                //ingFlg = true;　　ここでエラー
                            }
                        });
                        if(ingFlg){
                            flg.add(true);
                        }else{
                            flg.add(false);
                        }
                        break;
                    default:
                        break;
                }
            });

            if(Collections.frequency(flg, true) == flg.size())resultList.add(r);
        });

        return resultList;
    }
}