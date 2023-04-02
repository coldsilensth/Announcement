package com.example.announcementProject.controller;

import com.example.announcementProject.dto.AnnouncementDTO;
import com.example.announcementProject.entity.Announcement;
import com.example.announcementProject.repository.AnnouncementRepo;
import com.example.announcementProject.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/announcements")

@RestController
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private AnnouncementRepo announcementRepo;

    @GetMapping("/list10")
    public List<AnnouncementDTO> getAllAnnouncements(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "createdAt") String sortBy,
                                                     @RequestParam(defaultValue = "desc") String sortOrder) {
        return announcementService.getListAnonnouncement(page, sortBy, sortOrder);
    }


    @PostMapping("/create")
    public ResponseEntity<?> createAnnouncements(@Valid @RequestBody AnnouncementDTO announcementDTO, BindingResult result) {

        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                String errorMessage = error.getDefaultMessage();
                errors.add(errorMessage);
            }
            return ResponseEntity.badRequest().body(errors);
        }

        Announcement announcement = new Announcement();
        announcement.setTitle(announcementDTO.getTitle());
        announcement.setPhotos(announcementDTO.getPhotos());
        announcement.setDescription(announcementDTO.getDescription());
        announcement.setPrice(announcementDTO.getPrice());
        announcement.setCreatedAt(LocalDateTime.now());
        Announcement savedAnnouncement = announcementRepo.save(announcement);
//        return ResponseEntity.ok(savedAnnouncement);
        return ResponseEntity.ok(savedAnnouncement.getId()+ " объявление добавлено");

    }
}
