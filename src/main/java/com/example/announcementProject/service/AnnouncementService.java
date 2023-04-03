package com.example.announcementProject.service;

import com.example.announcementProject.dto.AnnouncementDTO;
import com.example.announcementProject.entity.Announcement;
import com.example.announcementProject.repository.AnnouncementRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
            announcementDTO.setId(announcement.getId());
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

    public Announcement createAnnouncements(AnnouncementDTO announcementDTO) throws ValidationException{
        validatePhotosCount(announcementDTO.getPhotos());

        Announcement announcement = new Announcement();
        announcement.setTitle(announcementDTO.getTitle());
        announcement.setPhotos(announcementDTO.getPhotos());
        announcement.setDescription(announcementDTO.getDescription());
        announcement.setPrice(announcementDTO.getPrice());
        announcement.setCreatedAt(LocalDateTime.now());
        return announcementRepo.save(announcement);
    }


    public void validatePhotosCount(List<String> photos){
        if (photos.size() > 3){
            throw new ValidationException("Можно добавить только 3 ссылки");
        }
    }
    public AnnouncementDTO getAnnouncementById(Long id, boolean allDescription, boolean allPhotos){
        Optional<Announcement> announcementOptional = announcementRepo.findById(id);

        if(announcementOptional.isPresent()){
            Announcement announcement = announcementOptional.get();
                AnnouncementDTO announcementDTO = new AnnouncementDTO();
                announcementDTO.setId(announcement.getId());
                announcementDTO.setTitle(announcement.getTitle());
                announcementDTO.setPrice(announcement.getPrice());

                List<String> mainPhoto = new ArrayList<>();
                mainPhoto.add(announcement.getPhotos().get(0));
                announcementDTO.setMainPhoto(mainPhoto);

                if(allDescription){
                    announcementDTO.setDescription(announcement.getDescription());
                }
                if(allPhotos){
                    announcementDTO.setPhotos(announcement.getPhotos());
                }
                return announcementDTO;
        }
        return null;
    }
    public Announcement updateAnnouncement(Long id, AnnouncementDTO announcementDTO){
        Optional<Announcement> announcementOptional = announcementRepo.findById(id);
        if(announcementOptional.isPresent()){
            Announcement announcement = announcementOptional.get();
            announcement.setTitle(announcementDTO.getTitle());
            announcement.setPhotos(announcementDTO.getPhotos());
            announcement.setDescription(announcementDTO.getDescription());
            announcement.setPrice(announcementDTO.getPrice());
            announcement.setCreatedAt(LocalDateTime.now());
            return announcementRepo.save(announcement);
        }else {
            throw new EntityNotFoundException("Announcement not found with id: " + id);
        }
    }

    public Long deleteAnnouncement(Long id){
        announcementRepo.deleteById(id);
        return id;
    }
}