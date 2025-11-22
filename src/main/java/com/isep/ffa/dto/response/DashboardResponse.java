package com.isep.ffa.dto.response;

import com.isep.ffa.entity.Person;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dashboard response payload containing statistics and recent activities
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dashboard statistics and recent activities for intervener")
public class DashboardResponse {

    @Schema(description = "Current user information")
    private Person user;

    @Schema(description = "User's organization name", example = "French Embassy - Ottawa")
    private String organizationName;

    @Schema(description = "Statistics cards data")
    private Statistics statistics;

    @Schema(description = "Recent activities list")
    private List<ActivityItem> recentActivities;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Dashboard statistics")
    public static class Statistics {
        @Schema(description = "My projects count", example = "12")
        private Long myProjectsCount;

        @Schema(description = "My projects increase this month", example = "2")
        private Long myProjectsIncreaseThisMonth;

        @Schema(description = "Pending approvals count", example = "3")
        private Long pendingApprovalsCount;

        @Schema(description = "Applications in review count", example = "24")
        private Long applicationsInReviewCount;

        @Schema(description = "Applications in review increase this week", example = "12")
        private Long applicationsInReviewIncreaseThisWeek;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Recent activity item")
    public static class ActivityItem {
        @Schema(description = "Activity title", example = "Youth Entrepreneurship Program 2024")
        private String title;

        @Schema(description = "Activity date", example = "2024-01-15")
        private String date;

        @Schema(description = "Activity status", example = "Pending Approval")
        private String status;

        @Schema(description = "Activity type", example = "PROJECT", allowableValues = {"PROJECT", "APPLICATION"})
        private String type;

        @Schema(description = "Related entity ID")
        private Long entityId;
    }
}
