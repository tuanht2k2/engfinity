package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.CommonDeleteRequest;
import com.connectify.connectify.DTO.request.EditRelationshipRequest;
import com.connectify.connectify.DTO.request.SearchRelationshipRequest;
import com.connectify.connectify.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/relationships")
public class RelationshipController {
    @Autowired
    RelationshipService relationshipService;

    @PostMapping("")
    public ResponseEntity<?> create (@RequestBody EditRelationshipRequest request) {
        return relationshipService.create(request);
    }

    @PostMapping("delete-by-ids")
    public ResponseEntity<?> delete (@RequestBody CommonDeleteRequest request) {
        return relationshipService.delete(request.getIds());
    }

    @PostMapping("accept")
    public ResponseEntity<?> accept (@RequestBody EditRelationshipRequest request) {
        return relationshipService.accept(request.getId());
    }

    @GetMapping("get-friend-requests")
    public ResponseEntity<?> getFriendRequest () {
        return relationshipService.getFriendRequests();
    }
}
