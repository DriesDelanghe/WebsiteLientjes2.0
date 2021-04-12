package be.thomasmore.be.websitelientjes.controllers.wrapperclass;

import be.thomasmore.be.websitelientjes.models.ContactType;

import java.util.List;

public class ContactTypeWrapper {

    private List<ContactType> contactTypeList;

    public ContactTypeWrapper() {
    }

    public List<ContactType> getContactTypeList() {
        return contactTypeList;
    }

    public void setContactTypeList(List<ContactType> contactTypeList) {
        this.contactTypeList = contactTypeList;
    }
}
