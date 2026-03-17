package com.sublethub.service;

import com.sublethub.common.PageResult;
import com.sublethub.dto.HouseCreateDto;
import com.sublethub.dto.HouseQueryDto;
import com.sublethub.entity.House;
import com.sublethub.entity.HouseLocation;
import com.sublethub.exception.BusinessException;
import com.sublethub.mapper.HouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseService {

  private final HouseMapper houseMapper;

  @Transactional
  public House create(Long userId, HouseCreateDto dto) {
    House house = new House();
    house.setUserId(userId);
    house.setTitle(dto.getTitle());
    house.setType(dto.getType());
    house.setArea(dto.getArea());
    house.setPrice(dto.getPrice());
    house.setDeposit(dto.getDeposit());
    house.setStartDate(dto.getStartDate());
    house.setEndDate(dto.getEndDate());
    house.setAvailableDate(dto.getAvailableDate());
    house.setDescription(dto.getDescription());
    house.setImages(dto.getImages() != null ? String.join(",", dto.getImages()) : null);
    if (dto.getLocation() != null) {
      HouseLocation loc = new HouseLocation();
      loc.setProvince(dto.getLocation().getProvince());
      loc.setCity(dto.getLocation().getCity());
      loc.setDistrict(dto.getLocation().getDistrict());
      loc.setAddress(dto.getLocation().getAddress());
      loc.setLatitude(dto.getLocation().getLatitude());
      loc.setLongitude(dto.getLocation().getLongitude());
      house.setLocation(loc);
    }
    house.setStatus("pending");
    houseMapper.insert(house);
    return house;
  }

  public House getById(Long id) {
    House house = houseMapper.selectById(id);
    if (house == null) {
      throw new BusinessException(404, "房源不存在");
    }
    house.setViewCount(house.getViewCount() == null ? 1 : house.getViewCount() + 1);
    houseMapper.updateById(house);
    return house;
  }

  public PageResult<House> query(HouseQueryDto query) {
    int offset = (query.getPage() - 1) * query.getPageSize();
    String orderBy = query.getOrderBy() != null ? query.getOrderBy() : "default";
    List<House> list = houseMapper.selectApprovedForQuery(
        query.getKeyword(),
        query.getDistricts(),
        query.getPriceMin(),
        query.getPriceMax(),
        query.getType(),
        query.getStartDate(),
        query.getEndDate(),
        orderBy,
        offset,
        query.getPageSize()
    );
    long total = houseMapper.countApprovedForQuery(
        query.getKeyword(),
        query.getDistricts(),
        query.getPriceMin(),
        query.getPriceMax(),
        query.getType(),
        query.getStartDate(),
        query.getEndDate()
    );
    return PageResult.of(list, total, query.getPage(), query.getPageSize());
  }

  public PageResult<House> myHouses(Long userId, int page, int pageSize) {
    int offset = (page - 1) * pageSize;
    List<House> list = houseMapper.selectByUserId(userId, offset, pageSize);
    long total = houseMapper.countByUserId(userId);
    return PageResult.of(list, total, page, pageSize);
  }

  @Transactional
  public House update(Long userId, Long houseId, HouseCreateDto dto) {
    House house = houseMapper.selectById(houseId);
    if (house == null) {
      throw new BusinessException(404, "房源不存在");
    }
    if (!house.getUserId().equals(userId)) {
      throw new BusinessException(403, "无权限修改");
    }
    house.setTitle(dto.getTitle());
    house.setType(dto.getType());
    house.setArea(dto.getArea());
    house.setPrice(dto.getPrice());
    house.setDeposit(dto.getDeposit());
    house.setStartDate(dto.getStartDate());
    house.setEndDate(dto.getEndDate());
    house.setAvailableDate(dto.getAvailableDate());
    house.setDescription(dto.getDescription());
    house.setImages(dto.getImages() != null ? String.join(",", dto.getImages()) : null);
    if (dto.getLocation() != null) {
      HouseLocation loc = house.getLocation() != null ? house.getLocation() : new HouseLocation();
      loc.setProvince(dto.getLocation().getProvince());
      loc.setCity(dto.getLocation().getCity());
      loc.setDistrict(dto.getLocation().getDistrict());
      loc.setAddress(dto.getLocation().getAddress());
      loc.setLatitude(dto.getLocation().getLatitude());
      loc.setLongitude(dto.getLocation().getLongitude());
      house.setLocation(loc);
    }
    houseMapper.updateById(house);
    return house;
  }

  @Transactional
  public void offline(Long userId, Long houseId) {
    House house = houseMapper.selectById(houseId);
    if (house == null) {
      throw new BusinessException(404, "房源不存在");
    }
    if (!house.getUserId().equals(userId)) {
      throw new BusinessException(403, "无权限");
    }
    house.setStatus("offline");
    houseMapper.updateById(house);
  }
}
