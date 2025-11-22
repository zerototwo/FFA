package com.isep.ffa.mapper;

import com.isep.ffa.entity.DocumentsSubmitted;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Documents Submitted Mapper Interface
 */
@Mapper
public interface DocumentsSubmittedMapper extends CustomBaseMapper<DocumentsSubmitted> {

  /**
   * Find documents by application ID
   */
  @Select("SELECT * FROM documents_submitted WHERE application_id = #{applicationId} AND is_deleted = false")
  List<DocumentsSubmitted> findByApplicationId(Long applicationId);

  /**
   * Find documents by document type ID
   */
  @Select("SELECT * FROM documents_submitted WHERE document_type_id = #{documentTypeId} AND is_deleted = false")
  List<DocumentsSubmitted> findByDocumentTypeId(Long documentTypeId);

  /**
   * Count documents by application ID
   */
  @Select("SELECT COUNT(*) FROM documents_submitted WHERE application_id = #{applicationId} AND is_deleted = false")
  Long countByApplicationId(Long applicationId);

  /**
   * Count documents by application ID and document type ID
   */
  @Select("SELECT COUNT(*) FROM documents_submitted WHERE application_id = #{applicationId} AND document_type_id = #{documentTypeId} AND is_deleted = false")
  Long countByApplicationIdAndDocumentTypeId(Long applicationId, Long documentTypeId);
}

