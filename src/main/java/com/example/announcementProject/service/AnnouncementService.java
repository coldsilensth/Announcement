package com.example.announcementProject.service;

import com.example.announcementProject.dto.AnnouncementDTO;
import com.example.announcementProject.entity.Announcement;
import com.example.announcementProject.repository.AnnouncementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private AnnouncementRepo announcementRepo;

    public List<AnnouncementDTO> getListAnonnouncement(int page, String sortBy, String sortOrder) {
        PageRequest pageRequest = PageRequest.of(0, 6, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
        Page<Announcement> announcementPage = announcementRepo.findAll(pageRequest);
        List<Announcement> announcements = announcementPage.getContent();
        List<AnnouncementDTO> announcementDTOS = new ArrayList<>();
        for (Announcement announcement : announcements) {
            AnnouncementDTO announcementDTO = new AnnouncementDTO();
            announcementDTO.setTitle(announcement.getTitle());
            List<String> photos = announcement.getPhotos();
            announcementDTO.setPrice(announcement.getPrice());
            if (!photos.isEmpty()) {
                announcementDTO.setMainPhoto(Collections.singletonList(photos.get(0)));
            }
            announcementDTO.setPhotos(photos);
            announcement.setCreatedAt(LocalDateTime.now());
            announcementDTOS.add(announcementDTO);
        }
        return announcementDTOS;
    }

    public Announcement createAnnoun(Announcement announcement){
        return announcementRepo.save(announcement);
    }
}