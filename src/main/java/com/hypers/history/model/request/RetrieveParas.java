package com.hypers.history.model.request;

import com.hypers.history.enums.EnumsRecordStatus;

/**
 * @author long
 * @since 17-9-1
 */
public class RetrieveParas {
  private Long userId;
  private long offset;
  private Long num;
  private EnumsRecordStatus status;
  private Long id;

  public RetrieveParas() {}

  public RetrieveParas(Long userId, Long offset, Long num, EnumsRecordStatus status) {
    this.userId = userId;
    this.num = num;
    this.status = status;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setOffset(long offset) {
    this.offset = offset;
  }

  public long getOffset() {
    return offset;
  }

  public void setNum(Long num) {
    this.num = num;
  }

  public Long getNum() {
    return num;
  }

  public void setStatus(EnumsRecordStatus status) {
    this.status = status;
  }

  public EnumsRecordStatus getStatus() {
    return status;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "RetrieveParas [userId=" + userId + ", offset=" + offset + ", num=" + num + ", status="
        + status + ",id=" + id + "]";
  }

}