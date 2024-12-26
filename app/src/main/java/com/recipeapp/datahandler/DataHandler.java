package com.recipeapp.datahandler;

import java.io.IOException;
import java.util.ArrayList;

import com.recipeapp.model.Recipe;

public interface DataHandler {

    public String getMode();

    public ArrayList<Recipe> readData() throws IOException ;

    public void writeData(Recipe recipe) throws IOException ;

    public ArrayList<Recipe> searchData(String keyword) throws IOException ;
}