package com.sublethub.service;

import com.sublethub.common.PageResult;
import com.sublethub.entity.Favorite;
import com.sublethub.entity.House;
import com.sublethub.exception.BusinessException;
import com.sublethub.mapper.FavoriteMapper;
import com.sublethub.mapper.HouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

  private final FavoriteMapper favoriteMapper;
  private final HouseMapper houseMapper;

  @Transactional
  public void add(Long userId, Long houseId) {
    if (favoriteMapper.selectByUserIdAndHouseId(userId, houseId) != null) {
      return;
    }
    House house = houseMapper.selectById(houseId);
    if (house == null) {
      throw new BusinessException(404, "房源不存在");
    }
    Favorite f = new Favorite();
    f.setUserId(userId);
    f.setHouseId(houseId);
    favoriteMapper.insert(f);
    house.setFavoriteCount(house.getFavoriteCount() == null ? 1 : house.getFavoriteCount() + 1);
    houseMapper.updateById(house);
  }

  @Transactional
  public void remove(Long userId, Long houseId) {
    favoriteMapper.deleteByUserIdAndHouseId(userId, houseId);
    House house = houseMapper.selectById(houseId);
    if (house != null && house.getFavoriteCount() != null && house.getFavoriteCount() > 0) {
      house.setFavoriteCount(house.getFavoriteCount() - 1);
      houseMapper.updateById(house);
    }
  }

  public boolean isFavorited(Long userId, Long houseId) {
    return favoriteMapper.selectByUserIdAndHouseId(userId, houseId) != null;
  }

  public PageResult<House> list(Long userId, int page, int pageSize) {
    int offset = (page - 1) * pageSize;
    List<Favorite> favList = favoriteMapper.selectByUserId(userId, offset, pageSize);
    if (favList.isEmpty()) {
      return PageResult.of(Collections.emptyList(), 0L, page, pageSize);
    }
    List<Long> houseIds = favList.stream().map(Favorite::getHouseId).collect(Collectors.toList());
    List<House> houses = houseMapper.selectByIds(houseIds);
    long total = favoriteMapper.countByUserId(userId);
    return PageResult.of(houses, total, page, pageSize);
  }
}
