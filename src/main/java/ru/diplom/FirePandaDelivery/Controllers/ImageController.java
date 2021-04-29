package ru.diplom.FirePandaDelivery.Controllers;

import com.ibm.icu.text.Transliterator;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.diplom.FirePandaDelivery.exception.ImageExtensionNotSupportedException;
import ru.diplom.FirePandaDelivery.model.Product;
import ru.diplom.FirePandaDelivery.model.Restaurant;
import ru.diplom.FirePandaDelivery.service.RestaurantService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


@RestController
@RequestMapping("/image")
public class ImageController {

    @Value("${image.upload.path}")
    private String imagesDirectory;

    private final RestaurantService restaurantService;

    private final Transliterator toLatinTrans = Transliterator.getInstance("Russian-Latin/BGN");

    @Autowired
    public ImageController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }


    @GetMapping("/restaurant/{fileName}")
    @ResponseBody
    public ResponseEntity<byte[]> getRestaurantImage(@PathVariable String fileName) throws Exception {
        File file = new File(imagesDirectory.trim() + "/restaurant/" + fileName.trim());

        if (!file.exists()) {
            throw new FileNotFoundException("image not found");
        }

        FileInputStream fileInputStream = new FileInputStream(file);

        MediaType mediaType;
        String fileExtension = getFileExtension(file.getName());

        if (fileExtension.isEmpty()){
            throw new ImageExtensionNotSupportedException("extension not found");
        }

        switch (fileExtension) {
            case "jpeg":
                mediaType = MediaType.IMAGE_JPEG;
                break;
            case "gif":
                mediaType = MediaType.IMAGE_GIF;
                break;
            case "png":
                mediaType = MediaType.IMAGE_PNG;
                break;
            default:
                throw new ImageExtensionNotSupportedException("extension " + fileExtension + " not supported", fileExtension);
        }


        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" +file.getName())
                .contentType(mediaType)
                .body(fileInputStream.readAllBytes());
    }


    @GetMapping("/product/{fileName}")
    @ResponseBody
    public ResponseEntity<byte[]> getProductImage(@PathVariable String fileName) throws Exception {
        File file = new File(imagesDirectory.trim() + "/product/" + fileName.trim());

        if (!file.exists()) {
            throw new FileNotFoundException("image not found");
        }

        FileInputStream fileInputStream = new FileInputStream(file);

        MediaType mediaType;
        String fileExtension = getFileExtension(file.getName()).trim();

        if (fileExtension.isEmpty()){
            throw new ImageExtensionNotSupportedException("extension not found");
        }

        switch (fileExtension) {
            case "jpeg":
                mediaType = MediaType.IMAGE_JPEG;
                break;
            case "gif":
                mediaType = MediaType.IMAGE_GIF;
                break;
            case "png":
                mediaType = MediaType.IMAGE_PNG;
                break;
            default:
                throw new ImageExtensionNotSupportedException("extension " + fileExtension + " not supported", fileExtension);
        }


        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" +file.getName())
                .contentType(mediaType)
                .body(fileInputStream.readAllBytes());
    }




    @PostMapping("/restaurant/{id}")
    public ResponseEntity<String> uploadRestaurantImage(@RequestBody MultipartFile file, @PathVariable long id)
            throws Exception {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw new NullPointerException("image not set");
        }

//        String extension = getFileExtension(file.getOriginalFilename());
//
//        if (!extension.equals("png") && !extension.equals("jpeg") && !extension.equals("gif")) {
//            throw new ImageExtensionNotSupportedException("extension " + extension + " not supported", extension);
//        }

        File dir = new File(imagesDirectory.trim() + "/restaurant");

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new FileUploadException("failed to create directory");
            }
        }

        Restaurant restaurant = restaurantService.getRestaurant(id);
        String fileName = toLatinTrans.transliterate(restaurant.getName().replace(' ', '_')) + id + ".png";

        convertToPng(file.getInputStream(), new File(imagesDirectory + "/restaurant/" + fileName));

        restaurant.setImg( "/image/restaurant/" + fileName);

        restaurantService.updateRestaurant(restaurant);

      return ResponseEntity.ok("/image/restaurant/" + fileName);
    }

    @PostMapping("/product/{id}")
    public ResponseEntity<String> uploadProductImage(@RequestBody MultipartFile file, @PathVariable long id)
            throws IOException, ImageExtensionNotSupportedException {

        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw new NullPointerException("image not set");
        }

//        String extension = getFileExtension(file.getOriginalFilename());
//
//        if (!extension.equals("png") && !extension.equals("jpeg") && !extension.equals("gif")) {
//            throw new ImageExtensionNotSupportedException("extension " + extension + " not supported", extension);
//        }

        File dir = new File(imagesDirectory.trim() + "/product");

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new FileUploadException("failed to create directory");
            }
        }

        Product product = restaurantService.getProduct(id);

        String fileName = toLatinTrans.transliterate(product.getName().replace(' ', '_')) + id +  ".png";

        convertToPng(file.getInputStream(), new File(imagesDirectory + "/product/" + fileName));

//        file.transferTo(new File(imagesDirectory + "/product/" + fileName));

        product.setImg("/image/product/" + fileName);

        restaurantService.updateProduct(product);

        return ResponseEntity.ok("/image/product/" + fileName);
    }

    private String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    private void convertToPng(InputStream inputStream, File file) throws IOException {
        BufferedImage bufferedImage;
        bufferedImage = ImageIO.read(inputStream);
        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.getColor("TRANSLUCENT"), null);
        ImageIO.write(newBufferedImage, "png", file);
    }

}
