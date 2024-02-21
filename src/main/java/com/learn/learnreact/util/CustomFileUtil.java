package com.learn.learnreact.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

  public List<String> saveFiles(List<FilePart> files) throws RuntimeException {
    if (files == null || files.size() == 0) {
      return List.of(); // 예외 처리를 안하고 그냥 빈 리스트를 반환하네?
    }

    List<String> uploadName = new ArrayList();

    for (FilePart file : files) {
      String saveName = UUID.randomUUID().toString().substring(0, 8) + "_" + file.filename();
      Path savePath = Paths.get(uploadPath, saveName);

      file.transferTo(savePath).block();

      int idx = file.filename().lastIndexOf(".");
      String fileExtension = file.filename().substring(idx + 1);

      // 파일의 형태 확인
      if (file.headers().getContentType() != null && file.headers().getContentType().toString().startsWith("image")) {
        Path thumbnailPath = Paths.get(uploadPath, "s_" + saveName);

        try {
          Thumbnails.of(savePath.toFile())
                  .size(250, 250)
                  .toFile(thumbnailPath.toFile());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

      }

      uploadName.add(saveName);
    }
    return uploadName;
  }
}
