package com.yiling.service;

import com.yiling.domain.SupplementCard;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/3/2 15:31
 */
public interface SupplementCardService {
    boolean acceptMsg(SupplementCard supplementCard);
    boolean modifyTime(SupplementCard supplementCard);
    List<SupplementCard> getAllSupplementCardMsg();
    List<SupplementCard> getAllNoSolvedSupplementCardMsg();
    List<SupplementCard> getSupplementCardMsgByCondition(SupplementCard supplementCard);

}
