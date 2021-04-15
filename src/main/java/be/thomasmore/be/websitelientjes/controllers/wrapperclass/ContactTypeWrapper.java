package be.thomasmore.be.websitelientjes.controllers.wrapperclass;

import be.thomasmore.be.websitelientjes.models.ContactType;

import java.util.List;

public class ContactTypeWrapper {

    private List<ContactType> contactTypeListBistro;
    private List<ContactType> contactTypeListBolo;


    public ContactTypeWrapper() {
    }

    public List<ContactType> getContactTypeListBistro() {
        return contactTypeListBistro;
    }

    public void setContactTypeListBistro(List<ContactType> contactTypeListBistro) {
        this.contactTypeListBistro = contactTypeListBistro;
    }

    public List<ContactType> getContactTypeListBolo() {
        return contactTypeListBolo;
    }

    public void setContactTypeListBolo(List<ContactType> contactTypeListBolo) {
        this.contactTypeListBolo = contactTypeListBolo;
    }
}
