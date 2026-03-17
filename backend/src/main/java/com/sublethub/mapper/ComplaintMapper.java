package com.sublethub.mapper;

import com.sublethub.entity.Complaint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ComplaintMapper {

  Complaint selectById(Long id);

  List<Complaint> selectByStatus(@Param("status") String status, @Param("offset") int offset, @Param("limit") int limit);

  List<Complaint> selectPage(@Param("offset") int offset, @Param("limit") int limit);

  long count();

  long countByStatus(String status);

  int insert(Complaint complaint);

  int updateById(Complaint complaint);
}
