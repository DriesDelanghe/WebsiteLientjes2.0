package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class TextFragment implements Comparable<TextFragment>{

    @Id
    private int id;
    @Column(length = 1500)
    @NotBlank
    private String textContent;
    private Integer orderReference;
    private boolean headerText;
    @ManyToOne(fetch = FetchType.LAZY)
    private Page page;

    public TextFragment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public Integer getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(Integer orderReference) {
        this.orderReference = orderReference;
    }

    public boolean isHeaderText() {
        return headerText;
    }

    public void setHeaderText(boolean headerText) {
        this.headerText = headerText;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }


    @Override
    public int compareTo(TextFragment t) {
        return this.getOrderReference().compareTo(t.getOrderReference());
    }
}
