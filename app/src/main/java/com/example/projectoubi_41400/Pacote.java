package com.example.projectoubi_41400;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Objects;

public class Pacote {

    String title; // Package Name
    String author; // Author of the package
    String authorID;
    String packageID;
    String dateUpdated;
    ArrayList<Cathegory> cathegories;
    int numberPages;
    String description;
    int numberDownloads;
    double rating;

    public Pacote() {
        this.title = "";
        this.cathegories = new ArrayList<>();
        this.author = "";
        this.numberPages = 0;
        this.dateUpdated = "";
        this.authorID = "";
        this.packageID = "";
        this.description = "";
        this.numberDownloads = 0;
        this.rating = 0.0;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getPackageID() {
        return packageID;
    }

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberDownloads() {
        return numberDownloads;
    }

    public void setNumberDownloads(int numberDownloads) {
        this.numberDownloads = numberDownloads;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumberPages() {
        return numberPages;
    }

    public void setNumberPages(int numberPages) {
        this.numberPages = numberPages;
    }

    public void increasePageNumber(){
        this.numberPages++;
    }

    public void decreasePageNumber(){
        this.numberPages--;
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

    public SubCathegory findSubCathegory(String subcatName, String catName){
        for (Cathegory cat : cathegories) {
            if(Objects.equals(cat.name, catName))
                for(SubCathegory subcat : cat.subCathegories){
                    if(Objects.equals(subcat.name, subcatName)){
                        return subcat;
                    }
                }
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

    public void setContent(String content, String subcatName, String catName){
        for (Cathegory cat : cathegories) {
            if(Objects.equals(cat.name, catName)) {
                for(SubCathegory subcat : cat.subCathegories){
                    if(Objects.equals(subcat.name,subcatName)){
                        subcat.setContent(content);
                    }
                }
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

    public void setAlignment(String alignment, String subcatName, String catName){
        for (Cathegory cat : cathegories) {
            if(Objects.equals(cat.name, catName)) {
                for(SubCathegory subcat : cat.subCathegories){
                    if(Objects.equals(subcat.name,subcatName)){
                        subcat.setAlignment(alignment);
                    }
                }
            }
        }
    }

    public String getAlignment(String subcatName, String catName){
        for (Cathegory cat : cathegories) {
            if(Objects.equals(cat.name, catName)) {
                for(SubCathegory subcat : cat.subCathegories){
                    if(Objects.equals(subcat.name,subcatName)){
                        return subcat.getAlignment();
                    }
                }
            }
        }
        return null;
    }

    @NonNull
    @Override
    public String toString(){
        return title;
    }
}
