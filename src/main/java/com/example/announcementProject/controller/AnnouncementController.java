package com.example.announcementProject.controller;

import com.example.announcementProject.dto.AnnouncementDTO;
import com.example.announcementProject.entity.Announcement;
import com.example.announcementProject.service.AnnouncementService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/announcements")
@RestController
public class AnnouncementController {

    private AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<AnnouncementDTO>> getAllAnnouncements(@RequestParam(defaultValue = "createdAt") String sortBy,
                                                     @RequestParam(defaultValue = "desc") String sortOrder) {
        return announcementService.getListAnnouncement(sortBy, sortOrder);
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
