package com.sublethub.mapper;

import com.sublethub.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper {

  Favorite selectByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId") Long houseId);

  List<Favorite> selectByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);

  long countByUserId(Long userId);

  int insert(Favorite favorite);

  int deleteByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId") Long houseId);
}
