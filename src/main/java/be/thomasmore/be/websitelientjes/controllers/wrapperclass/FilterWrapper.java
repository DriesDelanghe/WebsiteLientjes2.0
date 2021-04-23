package be.thomasmore.be.websitelientjes.controllers.wrapperclass;

import be.thomasmore.be.websitelientjes.models.Allergie;
import be.thomasmore.be.websitelientjes.models.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class FilterWrapper {

    private ArrayList<Integer> categoryIdList = new ArrayList<>();
    private ArrayList<Integer> allergyIdList = new ArrayList<>();
    private List<ProductCategory> categoryList;
    private List<Allergie> allergieList;
    private String productSearch;

    public FilterWrapper() {
    }

    public ArrayList<Integer> getCategoryIdList() {
        return categoryIdList;
    }

    public void setCategoryIdList(ArrayList<Integer> categoryIdList) {
        this.categoryIdList = categoryIdList;
    }

    public ArrayList<Integer> getAllergyIdList() {
        return allergyIdList;
    }

    public void setAllergyIdList(ArrayList<Integer> allergyIdList) {
        this.allergyIdList = allergyIdList;
    }

    public String getProductSearch() {
        return productSearch;
    }

    public void setProductSearch(String productSearch) {
        this.productSearch = productSearch;
    }

    public List<ProductCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<ProductCategory> categoryList) {
        this.categoryList = categoryList;
        for(ProductCategory productCategory : categoryList){
            categoryIdList.add(productCategory.getId());
        }
    }

    public List<Allergie> getAllergieList() {
        return allergieList;
    }

    public void setAllergieList(List<Allergie> allergieList) {
        this.allergieList = allergieList;
    }
}
