package com.isep.ffa.mapper;

import com.isep.ffa.entity.DocumentType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Document Type Mapper Interface
 */
@Mapper
public interface DocumentTypeMapper extends CustomBaseMapper<DocumentType> {

  /**
   * Find document types by project ID
   */
  @Select("SELECT * FROM document_type WHERE project_id = #{projectId} AND is_deleted = false")
  List<DocumentType> findByProjectId(Long projectId);

  /**
   * Find document type by name and project ID
   */
  @Select("SELECT * FROM document_type WHERE name = #{name} AND project_id = #{projectId} AND is_deleted = false")
  DocumentType findByNameAndProjectId(String name, Long projectId);
}

