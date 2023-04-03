package com.example.announcementProject.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Size;


@Data
public class AnnouncementDTO {
    private Long id;

    @Size(max = 200, message = "title 200 character limit\n")
    private String title;
    public void setTitle(String title){
        if(title.length() > 200){
            throw new ValidationException("title 200 character limit");
        }
        this.title = title;
    }

    @Size(max = 500 , message = "description 1000 character limit\n")
    private String description;

    public void setDescription(String description){
        if(description.length() > 1000){
            throw new ValidationException("desc 1000 character limit");
        }
        this.description = description;
    }

    @Valid
    @Size(max = 3, message = "no more than 3 links\n")
    private List<String> photos = new ArrayList<>();
    private int price;


    public void setMainPhoto(List<String> photos) {
        this.photos = photos;
    }
}
