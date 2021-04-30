package be.thomasmore.be.websitelientjes.controllers.wrapperclass;

import be.thomasmore.be.websitelientjes.models.RedirectEmail;

import java.util.List;

public class RedirectEmailWrapper {

    private List<RedirectEmail> redirectEmailList;

    public RedirectEmailWrapper() {
    }

    public List<RedirectEmail> getRedirectEmailList() {
        return redirectEmailList;
    }

    public void setRedirectEmailList(List<RedirectEmail> redirectEmailList) {
        this.redirectEmailList = redirectEmailList;
    }
}
