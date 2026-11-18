package vn.iotstar.dao;

import java.util.List;

import vn.iotstar.entity.Video;

public interface VideoDao {

    void insert(Video video);

    void update(Video video);

    void delete(int id);

    Video findById(int id);

    List<Video> findAll();

    List<Video> findLatest(int limit);

    List<Video> findByUserId(int userId, int page, int pageSize);

    long countByUserId(int userId);
}