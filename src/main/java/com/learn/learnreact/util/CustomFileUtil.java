package com.learn.learnreact.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 파일 데이터의 입출력을 담당한다.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomFileUtil {

  @Value("${com.learn.learnreact.upload.path}")
  private String uploadPath;

  @PostConstruct
  public void init() {
    File tempFolder = new File(uploadPath);

    if (tempFolder.exists() == false) {
      tempFolder.mkdir();
    }

    uploadPath = tempFolder.getAbsolutePath();

    log.info("===============================");
    log.info("upload path => {}", uploadPath);
  }

  public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {
    if (files == null || files.size() == 0) {
      return List.of(); // 예외 처리를 안하고 그냥 빈 리스트를 반환하네?
    }

    List<String> uploadName = new ArrayList();

    for (MultipartFile file : files) {
      String saveName = UUID.randomUUID().toString().substring(0, 8) + "_" + file.getOriginalFilename();
      Path savePath = Paths.get(uploadPath, saveName);
      try {
        Files.copy(file.getInputStream(), savePath);

        // 파일의 형태 확인
        String contentType = file.getContentType();
        if (contentType != null && contentType.startsWith("image")) {
          Path thumbnailPath = Paths.get(uploadPath, "s_" + saveName);

          Thumbnails.of(savePath.toFile())
                  .size(250, 250)
                  .toFile(thumbnailPath.toFile());

        }

        uploadName.add(saveName);
      } catch (IOException e) {
        throw new RuntimeException(e.getMessage());
      }
    }
    return uploadName;
  }
}
