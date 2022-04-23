package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.BattleRequest;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BattleRequestController {
    GenericRepository<BattleRequest> battleRequestGenericRepository;

    @Inject
    public BattleRequestController(
            GenericRepository<BattleRequest> battleRequestGenericRepository) {
        this.battleRequestGenericRepository = battleRequestGenericRepository;
    }

    public BattleRequest getBattleRequestByReceiverUserId(String receiverUserId) {
        Collection<BattleRequest> battleRequests = battleRequestGenericRepository.getAll();
        for (BattleRequest battleRequest : battleRequests) {
            if (battleRequest.getReceiverUserId().equals(receiverUserId)) {
                return battleRequest;
            }
        }
        return null;
    }

    @Nonnull
    public BattleRequest getBattleRequestByUserId(String receiverUserId, String initiatorUserId) {
        Collection<BattleRequest> battleRequests = battleRequestGenericRepository.getAll();
        for (BattleRequest battleRequest : battleRequests) {
            if (battleRequest.getReceiverUserId() == null
                    || battleRequest.getInitiatorUserId() == null) {
                battleRequestGenericRepository.delete(battleRequest.getId());
                continue;
            }
            if (battleRequest.getReceiverUserId().equals(receiverUserId)
                    && battleRequest.getInitiatorUserId().equals(initiatorUserId)) {
                return battleRequest;
            }
        }
        BattleRequest battleRequest = new BattleRequest();
        battleRequest.setReceiverUserId(receiverUserId);
        battleRequest.setInitiatorUserId(initiatorUserId);
        battleRequest.setDate(new Date());
        battleRequest.setIsBattle(false);
        battleRequestGenericRepository.add(battleRequest);
        return battleRequest;
    }

    public void deleteRequestById(String receiverUserId) {
        BattleRequest battleRequest = this.getBattleRequestByReceiverUserId(receiverUserId);
        battleRequestGenericRepository.delete(battleRequest.getId());
    }

    public void setBattleRequestByUserId(String receiverUserId, String initiatorUserId) {
        BattleRequest battleRequest =
                this.getBattleRequestByUserId(receiverUserId, initiatorUserId);
        Date date = new Date();
        Date oldDate = battleRequest.getDate();
        long diff = date.getTime() - oldDate.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        if (seconds > 60) {
            battleRequest.setIsBattle(false);
        }
        battleRequest.setDate(date);
        battleRequestGenericRepository.update(battleRequest);
    }

    public void setHasBattled(String receiverUserId) {
        BattleRequest battleRequest = this.getBattleRequestByReceiverUserId(receiverUserId);
        if (battleRequest == null) return;
        battleRequest.setIsBattle(true);
        battleRequestGenericRepository.update(battleRequest);
    }
}
