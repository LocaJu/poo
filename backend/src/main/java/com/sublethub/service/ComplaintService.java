package com.sublethub.service;

import com.sublethub.entity.Complaint;
import com.sublethub.mapper.ComplaintMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComplaintService {

  private final ComplaintMapper complaintMapper;

  @Transactional
  public Complaint create(Complaint complaint) {
    complaint.setStatus("pending");
    complaintMapper.insert(complaint);
    return complaint;
  }
}
