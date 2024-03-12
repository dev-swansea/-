package com.learn.learnreact.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;

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

  /**
   * @param files FilePart
   * @return 저장되는 파일의 이름
   * @throws RuntimeException
   */
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

      // 파일의 형태 확인
      //if (file.headers().getContentType() != null && file.headers().getContentType().toString().startsWith("image")) {
      if (file.headers().getContentType().toString().startsWith("image")) {
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

  public ResponseEntity<Resource> getFile(String fileName) {
    Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
    if (!resource.isReadable()) { // 읽을 사진이 없다면 기본 사진 보여주기
      resource = new FileSystemResource(uploadPath + File.separator + "default.jpg");
    }

    HttpHeaders headers = new HttpHeaders();

    try {
      // getFile과 toPath의 반환값은 같다.
      // C:\Users\swans\vscode\practice\구멍가게리액트\suwan-back\learn-react\\upload\s_b2117d98_harbour-4104066_1920.jpg
      headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
    return ResponseEntity.ok().headers(headers).body(resource);
  }

  public void deleteFile(List<String> fileNames) {
    if (fileNames == null || fileNames.size() == 0) {
      return;
    }

    fileNames.forEach(fileName -> {
      // 썸네일 확인하고, 있으면 삭제라는데, 있어도 없어도 그냥 삭제해보는거아님?
      // 그냥 filePath만 구하고 앞에 s_를 붙여줘도 될꺼같은데, String.join 써서..
      String thumbnailName = "s_" + fileName;
      Path thumbnailPath = Paths.get(uploadPath, thumbnailName);

      Path filePath = Paths.get(uploadPath, fileName);
      try {
        Files.deleteIfExists(thumbnailPath);
        Files.deleteIfExists(filePath);
      } catch (IOException e) {
        throw new RuntimeException(e.getMessage());
      }
    });
  }

}
