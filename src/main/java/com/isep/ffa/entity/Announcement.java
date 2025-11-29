package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("announcement")
public class Announcement {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;


    private String category;


    private String status;

    @TableField("author_id")
    private Long authorId;

    @TableField("author_name")
    private String authorName;

    @TableField("publish_time")
    private LocalDateTime publishTime;

    @TableField("view_count")
    private Integer viewCount;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("is_deleted")
    private Boolean isDeleted;
}