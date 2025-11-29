package com.isep.ffa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.isep.ffa.entity.Announcement;
import com.isep.ffa.mapper.AnnouncementMapper;
import com.isep.ffa.service.AnnouncementService;
import com.isep.ffa.security.SecurityUtils;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * Announcement Service Implementation
 */
@Service
public class AnnouncementServiceImpl extends BaseServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Override
    public BaseResponse<PagedResponse<Announcement>> searchAnnouncements(String keyword, String status, int page, int size) {
        Page<Announcement> pageParam = new Page<>(Math.max(page, 1), size <= 0 ? 10 : size);
        QueryWrapper<Announcement> query = new QueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            query.like("title", keyword);
        }
        if (StringUtils.hasText(status)) {
            query.eq("status", status);
        }

        query.eq("is_deleted", false);
        // Order by creation time descending (newest first)
        query.orderByDesc("create_time");

        Page<Announcement> result = this.page(pageParam, query);

        // Construct paged response
        PagedResponse<Announcement> response = PagedResponse.of(
                result.getRecords(),
                (int) result.getCurrent() - 1,
                (int) result.getSize(),
                result.getTotal(),
                (int) result.getPages()
        );

        return BaseResponse.success("Fetched successfully", response);
    }

    @Override
    public BaseResponse<Announcement> createAnnouncement(Announcement announcement) {
        // Get current user ID
        Long currentUserId = SecurityUtils.getCurrentUserId();
        announcement.setAuthorId(currentUserId);

        // For simplicity, hardcoding Admin. ideally fetch from PersonService
        announcement.setAuthorName("Admin");

        // Default to DRAFT if not specified
        if (!StringUtils.hasText(announcement.getStatus())) {
            announcement.setStatus("DRAFT");
        }

        // If creating directly as PUBLISHED, set timestamp
        if ("PUBLISHED".equalsIgnoreCase(announcement.getStatus())) {
            announcement.setPublishTime(LocalDateTime.now());
        }

        announcement.setViewCount(0);
        boolean saved = this.save(announcement);

        if (!saved) {
            return BaseResponse.error("Failed to create announcement", 500);
        }
        return BaseResponse.success("Announcement created", announcement);
    }

    @Override
    public BaseResponse<Announcement> updateAnnouncement(Announcement announcement) {
        Announcement existing = this.getById(announcement.getId());
        if (existing == null) {
            return BaseResponse.error("Announcement not found", 404);
        }

        // Update fields
        existing.setTitle(announcement.getTitle());
        existing.setContent(announcement.getContent());
        if (StringUtils.hasText(announcement.getCategory())) {
            existing.setCategory(announcement.getCategory());
        }

        // Handle status change to PUBLISHED
        if ("PUBLISHED".equalsIgnoreCase(announcement.getStatus())
                && !"PUBLISHED".equalsIgnoreCase(existing.getStatus())) {
            existing.setStatus("PUBLISHED");
            existing.setPublishTime(LocalDateTime.now());
        } else if (StringUtils.hasText(announcement.getStatus())) {
            existing.setStatus(announcement.getStatus());
        }

        this.updateById(existing);
        return BaseResponse.success("Updated successfully", existing);
    }

    @Override
    public BaseResponse<Boolean> publishAnnouncement(Long id) {
        Announcement existing = this.getById(id);
        if (existing == null) {
            return BaseResponse.error("Not found", 404);
        }

        existing.setStatus("PUBLISHED");
        existing.setPublishTime(LocalDateTime.now());

        return BaseResponse.success("Published successfully", this.updateById(existing));
    }

    @Override
    public BaseResponse<Boolean> deleteAnnouncement(Long id) {
        // Logical delete handled by MyBatis Plus @TableLogic
        return BaseResponse.success("Deleted successfully", this.removeById(id));
    }
}