package com.example.announcementProject.controller;

import com.example.announcementProject.dto.AnnouncementDTO;
import com.example.announcementProject.entity.Announcement;
import com.example.announcementProject.repository.AnnouncementRepo;
import com.example.announcementProject.service.AnnouncementService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
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
    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDTO> getAnnouncementById(@PathVariable Long id,
                                                               @RequestParam(defaultValue = "false") boolean allDescription,
                                                               @RequestParam(defaultValue = "false") boolean allPhotos){
        AnnouncementDTO announcementDTO = announcementService.getAnnouncementById(id, allDescription, allPhotos);

        if (announcementDTO != null){
            return ResponseEntity.ok(announcementDTO);
        }else {
            return ResponseEntity.notFound().build();
        }

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
        try {
            Announcement savedAnnouncement = announcementService.createAnnouncements(announcementDTO);
            return ResponseEntity.ok(savedAnnouncement.getId() + " объявление добавлено");
        }catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAnnouncement(@PathVariable Long id, @Valid @RequestBody AnnouncementDTO announcementDTO) {
        announcementService.validatePhotosCount(announcementDTO.getPhotos());
        Announcement updatedAnnouncement = announcementService.updateAnnouncement(id, announcementDTO);
        return ResponseEntity.ok(updatedAnnouncement);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAnnouncement(@PathVariable Long id){
        announcementService.deleteAnnouncement(id);
    }
}
