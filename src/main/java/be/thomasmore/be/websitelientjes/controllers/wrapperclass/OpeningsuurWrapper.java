package be.thomasmore.be.websitelientjes.controllers.wrapperclass;

import be.thomasmore.be.websitelientjes.models.Openingsuur;

import java.util.List;

public class OpeningsuurWrapper {

    private List<Openingsuur> openingsuurListBistro;
    private List<Openingsuur>  openingsuurListBolo;

    public OpeningsuurWrapper() {
    }

    public List<Openingsuur> getOpeningsuurListBistro() {
        return openingsuurListBistro;
    }

    public void setOpeningsuurListBistro(List<Openingsuur> openingsuurListBistro) {
        this.openingsuurListBistro = openingsuurListBistro;
    }

    public List<Openingsuur> getOpeningsuurListBolo() {
        return openingsuurListBolo;
    }

    public void setOpeningsuurListBolo(List<Openingsuur> openingsuurListBolo) {
        this.openingsuurListBolo = openingsuurListBolo;
    }
}
