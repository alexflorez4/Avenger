package com.shades.utilities;


import com.shades.exceptions.ShadesException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class Utils {

    public static File multipartToFile(MultipartFile multipart) throws ShadesException {

        try {
            File newFile = new File(multipart.getOriginalFilename());
            multipart.transferTo(newFile);
            return newFile;
        } catch (IOException e) {
            throw new ShadesException("Error at multipart file conversion to file. Message: " + e.getMessage());
        }

    }
}
