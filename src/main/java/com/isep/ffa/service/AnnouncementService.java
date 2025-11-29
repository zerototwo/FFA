package com.isep.ffa.service;

import com.isep.ffa.entity.Announcement;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;

/**
 * Announcement Service Interface
 */
public interface AnnouncementService extends BaseService<Announcement> {

    /**
     * Search announcements with pagination
     * @param keyword Title keyword
     * @param status Status filter (DRAFT, PUBLISHED)
     * @param page Page number
     * @param size Page size
     * @return Paged result
     */
    BaseResponse<PagedResponse<Announcement>> searchAnnouncements(String keyword, String status, int page, int size);

    /**
     * Create a new announcement (Draft or Published)
     * @param announcement Announcement data
     * @return Created announcement
     */
    BaseResponse<Announcement> createAnnouncement(Announcement announcement);

    /**
     * Update an existing announcement
     * @param announcement Announcement data
     * @return Updated announcement
     */
    BaseResponse<Announcement> updateAnnouncement(Announcement announcement);

    /**
     * Publish a specific announcement
     * @param id Announcement ID
     * @return Success status
     */
    BaseResponse<Boolean> publishAnnouncement(Long id);

    /**
     * Delete an announcement
     * @param id Announcement ID
     * @return Success status
     */
    BaseResponse<Boolean> deleteAnnouncement(Long id);
}