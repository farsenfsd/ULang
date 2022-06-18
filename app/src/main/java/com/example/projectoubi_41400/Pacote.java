package com.example.projectoubi_41400;

import java.util.ArrayList;
import java.util.Objects;

public class Pacote {

    String name; // Package Name
    String author; // Author of the package
    ArrayList<Cathegory> cathegories;

    public Pacote() {
        this.name = "";
        this.cathegories = new ArrayList<>();
        this.author = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<Cathegory> getCathegories() {
        return cathegories;
    }

    public void setCathegories(ArrayList<Cathegory> cathegories) {
        this.cathegories = cathegories;
    }

    public Cathegory findCathegory(String name){
        for (Cathegory cat : cathegories) {
            if(Objects.equals(cat.name, name))
                return cat;
        }
        return null;
    }

    public void addCathegory(Cathegory a){
        cathegories.add(a);
    }

    public void remCathegory(String CatName) {
        for (Cathegory cat : cathegories) {
            if(Objects.equals(cat.name, CatName)) {
                cathegories.remove(cat);
                break;
            }
        }
    }

    public void addSubCathegory(SubCathegory a, String catName){
        for (Cathegory cat : cathegories) {
            if(Objects.equals(cat.name, catName)) {
                cat.addSub(a);
                break;
            }
        }
    }

    public void remSubCathegory(String subcatName, String catName){
        for (Cathegory cat : cathegories) {
            if(Objects.equals(cat.name, catName)) {
                cat.subCathegories.removeIf(subcat -> Objects.equals(subcat.name, subcatName));
                break;
            }
        }
    }
}
