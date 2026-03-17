package com.sublethub.mapper;

import com.sublethub.entity.House;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface HouseMapper {

  House selectById(Long id);

  List<House> selectByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);

  long countByUserId(Long userId);

  List<House> selectByStatus(@Param("status") String status, @Param("offset") int offset, @Param("limit") int limit);

  long countByStatus(@Param("status") String status);

  List<House> selectPage(@Param("offset") int offset, @Param("limit") int limit);

  long countAll();

  List<House> selectApprovedForQuery(
      @Param("keyword") String keyword,
      @Param("districts") List<String> districts,
      @Param("priceMin") BigDecimal priceMin,
      @Param("priceMax") BigDecimal priceMax,
      @Param("type") String type,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("orderBy") String orderBy,
      @Param("offset") int offset,
      @Param("limit") int limit
  );

  long countApprovedForQuery(
      @Param("keyword") String keyword,
      @Param("districts") List<String> districts,
      @Param("priceMin") BigDecimal priceMin,
      @Param("priceMax") BigDecimal priceMax,
      @Param("type") String type,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate
  );

  List<House> selectByIds(@Param("ids") List<Long> ids);

  long countPending();

  long countTodayNewApproved();

  int insert(House house);

  int updateById(House house);
}
