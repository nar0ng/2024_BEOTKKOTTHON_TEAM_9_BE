package com.example.bommeong.biz.content.repository;

import com.example.bommeong.biz.content.dto.HistoryEntity;
import com.example.bommeong.common.dao.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryRepository extends BaseRepository<HistoryEntity, Long> {

    @Query("SELECT new HistoryEntity(h.contentId, c.title) " +
           "FROM HistoryEntity h " +
           "JOIN ContentEntity c ON h.contentId = c.contentId " +
           "GROUP BY h.contentId ORDER BY count(h.contentId) desc limit 1")
    List<HistoryEntity> countContentIdOccurrences();

    @Query(nativeQuery = true, value = "SELECT h.content_id as contentId, c.title as title " +
            "FROM (" +
            "    SELECT content_id " +
            "    FROM History " +
            "    ORDER BY created_at DESC LIMIT 100" +
            ") h " +
            "JOIN content c ON h.content_id = c.content_id " +
            "GROUP BY h.content_id ORDER BY count(h.content_id) desc")
    List<HistoryEntityInterface> countContentIdOccurrence1s();
}
