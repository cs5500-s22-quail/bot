package edu.northeastern.cs5500.starterbot.model;

import java.util.Date;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class BattleRequest implements Model {
    ObjectId id;

    String initiatorUserId;
    String receiverUserId;
    Boolean isBattle;
    Date date;
}
