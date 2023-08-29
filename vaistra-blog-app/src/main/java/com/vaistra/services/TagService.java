package com.vaistra.services;

import com.vaistra.dto.TagDTO;

import java.util.List;

public interface TagService {

    public TagDTO addTag(TagDTO tagDTO);

    public TagDTO getTagById(int id);

    public TagDTO getTrashedTagById(int id);

    public TagDTO getInActiveTagById(int id);

//    public List<TagDTO> getAllTags(int pageNo, int pageSize, String sortBy, String sortOrder);

    public List<TagDTO> getAllTags();

    public List<TagDTO> getAllTagsSortByField(String field);

    public List<TagDTO> getAllTrashedTags();

    public List<TagDTO> getAllInActiveTags();

    public TagDTO updateTagById(int id, TagDTO tagDTO);

    TagDTO updateTagStatusById(int id, boolean status);

    public int deleteTagById(int id);

    public int trashTagById(int id);

    public int restoreTagById(int id);
}
