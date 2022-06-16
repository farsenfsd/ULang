package com.example.projectoubi_41400;

import java.util.ArrayList;
import java.util.Objects;

public class Cathegory {
    String name;
    ArrayList<SubCathegory> subCathegories;

    public Cathegory() {
        this.name = "";
        this.subCathegories = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SubCathegory> getSubCathegories() {
        return subCathegories;
    }

    public void setSubCathegories(ArrayList<SubCathegory> subCathegories) {
        this.subCathegories = subCathegories;
    }

    public SubCathegory findSubCathegory(String name){
        for (SubCathegory subcat : subCathegories) {
            if(Objects.equals(subcat.name, name))
                return subcat;
        }
        return null;
    }

    public void addSub(SubCathegory aux){
        subCathegories.add(aux);
    }
}
