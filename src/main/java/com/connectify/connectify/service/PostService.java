package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.EditFileRequest;
import com.connectify.connectify.DTO.request.EditPostRequest;
import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.entity.File;
import com.connectify.connectify.entity.Post;
import com.connectify.connectify.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    FileService fileService;

    @Autowired
    ModelMapper mapper;

    public ResponseEntity<?> create (EditPostRequest request) {
        CommonResponse<String> response;
        try {
            Post post = mapper.map(request, Post.class);
            Post createdPost = postRepository.save(post);
            List<MultipartFile> multipartFiles = request.getFiles();

            if (!multipartFiles.isEmpty()) {
                for (MultipartFile multipartFile : multipartFiles) {
                    EditFileRequest editFileRequest = new EditFileRequest();
                    editFileRequest.setFile(multipartFile);
                    File hasUrlFile = fileService.setFileUrl(editFileRequest);
                    hasUrlFile.setPost(createdPost);
                    fileService.create(hasUrlFile);
                }
            }
            response = new CommonResponse<>(200, "", "Create successfully!");
        } catch (Exception e) {
            response = new CommonResponse<>(403, "", "Bad request!");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
