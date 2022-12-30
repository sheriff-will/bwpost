package com.application.iserv;

import com.application.iserv.tests.MainLayout;
import com.opencsv.CSVWriter;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import javax.annotation.security.PermitAll;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@PageTitle("iServ | Download")
@Route(value = "DownloadView", layout = MainLayout.class)
@PermitAll
public class DownloadView extends VerticalLayout {

    public DownloadView() {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm - dd MMMM yyyy");

        String path = "exported-participants-files/participants " + LocalDateTime.now().format(dateFormatter)+".csv";

        File participantsFolder = getUploadFolder();
        UploadArea participantsArea = new UploadArea(participantsFolder);
        DownloadLinksArea participantsLinksArea = new DownloadLinksArea(participantsFolder);

        Anchor link;

        try {
            //   Path path1 = Paths.get(ClassLoader.getSystemResource("csv/participants.csv").toURI());

            File file = new File(path);

            FileWriter fileWriter = new FileWriter(file);

            CSVWriter csvWriter = new CSVWriter(fileWriter);

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"Name", "Mark", "Pass/Fail"});
            data.add(new String[]{"Stacy Hart", "80", "Pass"});
            data.add(new String[]{"John Doe", "40", "Fail"});

            csvWriter.writeAll(data);

            csvWriter.close();

            StreamResource streamResource = new StreamResource(file.getName(), () -> getStream(file));
            link = new Anchor(streamResource, String.format("%s (%d KB)", file.getName(),
                    (int) file.length() / 102400));
            link.getElement().setAttribute("download", true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }






        File uploadFolder = getUploadFolder();
        UploadArea uploadArea = new UploadArea(uploadFolder);
        DownloadLinksArea linksArea = new DownloadLinksArea(uploadFolder);

        uploadArea.getUploadField().addSucceededListener(e -> {
            uploadArea.hideErrorField();
            linksArea.refreshFileLinks();
        });

        add(uploadArea, linksArea);
    }

    private static File getUploadFolder() {
        File folder = new File("exported-participants-files");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    private InputStream getStream(File file) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return stream;
    }

}