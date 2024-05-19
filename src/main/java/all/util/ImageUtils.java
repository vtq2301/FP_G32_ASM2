package all.service;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageUtils {

    private static final String UPLOADS_DIR = "uploads/";

    static {
        try {
            Files.createDirectories(Paths.get(UPLOADS_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<ImageView> renderUploadViews(String claimId) {
        List<ImageView> imageViews = new ArrayList<>();
        File dir = new File(UPLOADS_DIR + claimId);
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageViews.add(imageView);
                }
            }
        }
        return imageViews;
    }

    public static void uploadImages(Stage stage, String claimId) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Images");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        if (files != null) {
            for (File file : files) {
                try (InputStream inputStream = new FileInputStream(file)) {
                    saveImage(claimId, file.getName(), inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveImage(String claimId, String docName, InputStream inputStream) throws IOException {
        File dir = new File(UPLOADS_DIR + claimId);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, docName);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    public static List<String> fetchImages(String claimId) {
        File dir = new File("uploads/" + claimId);
        List<String> documents = new ArrayList<>();
        if (dir.exists() && dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.isFile()) {
                    documents.add(file.getName());
                }
            }
        }
        return documents;
    }
}
