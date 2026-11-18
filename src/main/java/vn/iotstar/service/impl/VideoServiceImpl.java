package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.VideoDao;
import vn.iotstar.dao.impl.VideoDaoImpl;
import vn.iotstar.entity.Video;
import vn.iotstar.service.VideoService;

public class VideoServiceImpl implements VideoService {

    private final VideoDao dao = new VideoDaoImpl();

    @Override
    public void insert(Video video) {
        dao.insert(video);
    }

    @Override
    public void update(Video video) {
        dao.update(video);
    }

    @Override
    public void delete(int id) {
        dao.delete(id);
    }

    @Override
    public Video findById(int id) {
        return dao.findById(id);
    }

    @Override
    public List<Video> findAll() {
        return dao.findAll();
    }

    @Override
    public List<Video> findLatest(int limit) {
        return dao.findLatest(limit);
    }

    @Override
    public List<Video> findByUserId(int userId, int page, int pageSize) {
        return dao.findByUserId(userId, page, pageSize);
    }

    @Override
    public long countByUserId(int userId) {
        return dao.countByUserId(userId);
    }
}