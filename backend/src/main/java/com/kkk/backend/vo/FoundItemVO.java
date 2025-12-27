package com.kkk.backend.vo;

import com.kkk.backend.entity.FoundItem;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FoundItemVO {
    private Long id;
    private String title;
    private String category;
    private String foundLocation;
    private LocalDateTime foundTime;
    private String description;
    private String imageUrl;
    private Integer status; // 0:待认领 1:已归还
    private Long userId;
    private LocalDateTime createTime;

    // 视图层额外字段
    private String publisherName;
    private Boolean isOwner;

    public static FoundItemVO from(FoundItem item, Long currentUserId, String publisherName) {
        FoundItemVO vo = new FoundItemVO();
        vo.setId(item.getId());
        vo.setTitle(item.getTitle());
        vo.setCategory(item.getCategory());
        vo.setFoundLocation(item.getFoundLocation());
        vo.setFoundTime(item.getFoundTime());
        vo.setDescription(item.getDescription());
        vo.setImageUrl(item.getImageUrl());
        vo.setStatus(item.getStatus());
        vo.setUserId(item.getUserId());
        vo.setCreateTime(item.getCreateTime());

        vo.setPublisherName(publisherName);
        vo.setIsOwner(currentUserId != null && currentUserId.equals(item.getUserId()));

        return vo;
    }
}