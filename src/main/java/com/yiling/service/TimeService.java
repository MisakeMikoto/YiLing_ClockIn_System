package com.yiling.service;

import com.yiling.domain.Time;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/3/1 21:12
 */
public interface TimeService {
    List<Time> getAllTimes();

    Time getTime(Time time);
}
