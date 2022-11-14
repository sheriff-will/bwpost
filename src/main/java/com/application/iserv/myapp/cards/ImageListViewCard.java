package com.application.iserv.myapp.cards;

import com.application.iserv.myapp.services.MyAppService;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class ImageListViewCard extends ListItem {

    // Services
    private final MyAppService myAppService;

    @Autowired
    public ImageListViewCard(String text, String url, MyAppService myAppService) {
        this.myAppService = myAppService;
        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");

        Div div = new Div();
        div.addClassNames("bg-contrast", "flex items-center", "justify-center", "mb-m", "overflow-hidden",
                "rounded-m w-full");
        div.setHeight("160px");

        byte[] decodeImg = Base64.getDecoder().decode(myAppService.getImage());

        StreamResource streamResource = new StreamResource(
                "logo.png",
                () -> new ByteArrayInputStream(decodeImg));

        Image image = new Image();
        image.setWidth("100%");
        image.setSrc(streamResource);
        image.setAlt(text);

        Image userImage = new Image();
        userImage.setWidth("25%");
        userImage.setHeight("25%");
        userImage.setSrc("images/avatar.png");
        userImage.setAlt(text);

        userImage.getElement().getStyle()
                .set("margin-top", "-8vh")
                .set("margin-right", "2vh");
        userImage.setClassName("image-list__avatar");

        div.add(image);

        Span header = new Span();
        header.addClassNames("text-xl", "font-semibold");
        header.setText("John Doe");

        Span subtitle = new Span();
        subtitle.addClassNames("text-s", "text-secondary");
        subtitle.setText("Mini Cooper - B 123 ABC");

        Paragraph description = new Paragraph(
                "Departing: Game City @17:00hrs");
        description.addClassName("my-m");

        Span badge = new Span();
        badge.getElement().setAttribute("theme", "badge");
        badge.setText("76448899");

        add(div, header, subtitle, description, badge);

    }
}
