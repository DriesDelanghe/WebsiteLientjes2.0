package be.thomasmore.be.websitelientjes.controllers.wrapperclass;

import be.thomasmore.be.websitelientjes.models.TextFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextWrapper {

    private ArrayList<TextFragment> headerText = new ArrayList<>();
    private ArrayList<TextFragment> paragraphText = new ArrayList<>();

    public TextWrapper() {
    }

    public void addHeaders(List<TextFragment> headerTexts){
        headerText.addAll(headerTexts);
    }

    public void addParagraphs(List<TextFragment> paragraphTexts){
        paragraphText.addAll(paragraphTexts);
    }

    public ArrayList<TextFragment> getHeaderText() {
        Collections.sort(headerText);
        return headerText;
    }

    public void setHeaderText(ArrayList<TextFragment> headerText) {
        this.headerText = headerText;
    }

    public ArrayList<TextFragment> getParagraphText() {
        Collections.sort(paragraphText);
        return paragraphText;
    }

    public void setParagraphText(ArrayList<TextFragment> paragraphText) {
        this.paragraphText = paragraphText;
    }
}
