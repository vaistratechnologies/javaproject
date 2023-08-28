package com.vaistra.services.impl;

import com.vaistra.dto.TagDTO;
import com.vaistra.services.TagService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final AppUtils appUtilsComment;

    @Autowired
    public TagServiceImpl(AppUtils appUtilsComment){
        this.appUtilsComment = appUtilsComment;
    }

    @Override
    public TagDTO addTag(TagDTO tagDTO) {
        return null;
    }

    @Override
    public TagDTO getTagById(int id) {
        return null;
    }

    @Override
    public TagDTO getTrashedTagById(int id) {
        return null;
    }

    @Override
    public TagDTO getInActiveTagById(int id) {
        return null;
    }

    @Override
    public List<TagDTO> getAllTags() {
        return null;
    }

    @Override
    public List<TagDTO> getAllTagsSortByField(String field) {
        return null;
    }

    @Override
    public List<TagDTO> getAllTrashedTags() {
        return null;
    }

    @Override
    public List<TagDTO> getAllInActiveTags() {
        return null;
    }

    @Override
    public TagDTO updateTagById(int id, TagDTO tagDTO) {
        return null;
    }

    @Override
    public TagDTO updateTagStatusById(int id, boolean status) {
        return null;
    }

    @Override
    public int deleteTagById(int id) {
        return 0;
    }

    @Override
    public int trashTagById(int id) {
        return 0;
    }

    @Override
    public int restoreTagById(int id) {
        return 0;
    }
}
