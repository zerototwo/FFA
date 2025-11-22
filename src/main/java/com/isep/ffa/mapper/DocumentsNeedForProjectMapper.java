package com.isep.ffa.mapper;

import com.isep.ffa.entity.DocumentsNeedForProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Documents Need For Project Mapper Interface
 */
@Mapper
public interface DocumentsNeedForProjectMapper extends CustomBaseMapper<DocumentsNeedForProject> {

  /**
   * Find document requirements by project ID
   */
  @Select("SELECT * FROM documents_need_for_project WHERE project_id = #{projectId} AND is_deleted = false")
  List<DocumentsNeedForProject> findByProjectId(Long projectId);

  /**
   * Find mandatory document requirements by project ID
   */
  @Select("SELECT * FROM documents_need_for_project WHERE project_id = #{projectId} AND is_mandatory = true AND is_deleted = false")
  List<DocumentsNeedForProject> findMandatoryByProjectId(Long projectId);

  /**
   * Find document requirement by project ID and document type ID
   */
  @Select("SELECT * FROM documents_need_for_project WHERE project_id = #{projectId} AND document_type_id = #{documentTypeId} AND is_deleted = false")
  DocumentsNeedForProject findByProjectIdAndDocumentTypeId(Long projectId, Long documentTypeId);
}

