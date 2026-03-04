package com.group7.jobTrackerApplication.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateApplicationNoteRequest {

    @NotBlank(message = "Content can not be blank")
    @Size(max = 2000, message = "Content must be less than 2000 characters")
    private String content;

    public UpdateApplicationNoteRequest(){
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }
}
