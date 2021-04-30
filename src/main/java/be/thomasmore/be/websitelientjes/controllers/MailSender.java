package be.thomasmore.be.websitelientjes.controllers;

import com.wildbit.java.postmark.Postmark;
import com.wildbit.java.postmark.client.ApiClient;
import com.wildbit.java.postmark.client.data.model.message.Message;
import com.wildbit.java.postmark.client.data.model.message.MessageResponse;
import org.springframework.stereotype.Component;

@Component
public class MailSender {

    ApiClient client = Postmark.getApiClient("20210430153530pm._domainkey.lientjes.be");

    public MailSender() {
    }

    public void deliverMail(){
        String htmlBody = "<!DOCTYPE html><html><body>" +
                "<p>Hello<a href=\"http://www.google.com\">world</a></p></body></html>";
        Message message = new Message("from@example.com", "dries.delanghe.1998@gmail.com", "Heelo from postmark!", htmlBody);
        message.setReplyTo("delanghe.dries.1998@gmail.com");

        try {
            MessageResponse response = client.deliverMessage(message);
            System.out.println(response.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
